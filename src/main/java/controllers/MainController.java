/**
 * 
 */
package controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import UBULogException.UBULogError;
import UBULogException.UBULogException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Chart;
import model.EnrolledUser;
import model.GradeReportLine;
import model.Group;
import model.Log;
import model.Role;
import parserdocument.CsvParser;
import webservice.CourseWS;

/**
 * Clase controlador de la ventana principal
 * 
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public class MainController implements Initializable {

	static final Logger logger = LoggerFactory.getLogger(MainController.class);

	@FXML
	public AnchorPane canvas;
	@FXML // Curso actual
	public Label lblActualCourse;
	@FXML // Usuario actual
	public Label lblActualUser;
	@FXML // Host actual
	public Label lblActualHost;

	@FXML // Numero de participantes
	public Label lblCountParticipants;
	@FXML // lista de participantes
	public ListView<EnrolledUser> listParticipants;
	ObservableList<EnrolledUser> enrList;

	@FXML // lista de eventos
	public ListView<model.Event> listEvents;
	ObservableList<model.Event> eventList;

	@FXML // lista de participantes
	public ListView<Log> listLogs;
	ObservableList<Log> enrLog;

	@FXML // Botón filtro por rol
	public MenuButton slcRole;
	MenuItem[] roleMenuItems;
	String filterRole = "Todos";

	@FXML // Botón filtro por grupo
	public MenuButton slcGroup;
	MenuItem[] groupMenuItems;
	String filterGroup = "Todos";

	@FXML // Entrada de filtro de usuarios por patrón
	public TextField tfdParticipants;
	String patternParticipants = "";

	@FXML // Entrada de filtro de usuarios por patrón
	public  Button btnchart;
	// @FXML // Vista en árbol de actividades
	// public TreeView<GradeReportLine> tvwGradeReport;
	// ArrayList<GradeReportLine> gradeReportList;

	@FXML // Entrada de filtro de actividades por patrón
	public TextField tfdEvents;
	String patternEvents = "";

	@FXML // Gráfico
	private LineChart<String, Number> lineChart;

	@FXML // chart
	private WebView chart;
	private WebEngine engine;

	private ArrayList<EnrolledUser> users;
	private CsvParser logs;
	private ArrayList<Log> filterLogs;
	private Chart viewchart;

	/**
	 * Muestra los usuarios matriculados en el curso, así como las actividades
	 * de las que se compone.
	 */
	public void initialize(URL location, ResourceBundle resources) {
		filterLogs = new ArrayList<>();

		try {
			logger.info(" Cargando curso '" + UBULog.session.getActualCourse().getFullName() + "'...");
			
			viewchart = new Chart();
			btnchart.setDisable(true);
			
			engine = chart.getEngine();
			viewchart.generarGrafica();
			
			viewChart();
			// Establecemos los usuarios matriculados
			CourseWS.setEnrolledUsers(UBULog.session.getToken(), UBULog.session.getActualCourse());

			// Establecemos calificador del curso
			// TODO quitando esto no abre la aplicación.
			CourseWS.setGradeReportLines(UBULog.session.getToken(),
					UBULog.session.getActualCourse().getEnrolledUsers().get(0).getId(),
					UBULog.session.getActualCourse());

			// Almacenamos todos los participantes en una lista
			users = (ArrayList<EnrolledUser>) UBULog.session.getActualCourse().getEnrolledUsers();

			// insertamos los usuarios ficticios.
			insertUserFicticios();
			enrList = FXCollections.observableArrayList(users);
			listParticipants.setItems(enrList);

			//////////////////////////////////////////////////////////////////////////
			// Manejo de roles
			manejoRoles();

			//////////////////////////////////////////////////////////////////////////
			// Manejo de grupos (MenuButton Grupo):
			manejoGrupos();

			// Mostramos número participantes
			lblCountParticipants.setText("Participantes: " + users.size());

			// Inicializamos el listener del textField de participantes
			tfdParticipants.setOnAction(inputParticipant());

			// Inicializamos el listener del textField del calificador
			tfdEvents.setOnAction(inputEvent());

		} catch (Exception e) {
			logger.error("Error en la inicialización. {}", e);
			e.printStackTrace();
		}

		// Asignamos el manejador de eventos de la lista
		// Al clickar en la lista, se recalcula el número de elementos
		// seleccionados de participantes.
		listParticipants.setOnMouseClicked(new EventHandler<Event>() {
			// Manejador que llama a la función de mostrar gráfico
			@Override
			public void handle(Event event) { // (1er click en participantes)
				/*
				 * ObservableList<EnrolledUser> selectedParticipants =
				 * listParticipants.getSelectionModel() .getSelectedItems();
				 * filterLogs.clear(); // Al seleccionar un participante
				 * reiniciamos el gráfico for (EnrolledUser actualUser :
				 * selectedParticipants) { for (Log actualLog : logs.getLogs())
				 * { if (actualLog.getUser().equals(actualUser)) {
				 * filterLogs.add(actualLog); } } } enrLog =
				 * FXCollections.observableArrayList(filterLogs);
				 * listLogs.setItems(enrLog);
				 */
				filterLogs();

			}
		});

		// Asignamos el manejador de eventos de la lista
		// Al clickar en la lista, se recalcula el número de elementos
		// seleccionados de eventos.
		listEvents.setOnMouseClicked(new EventHandler<Event>() {
			// Manejador que llama a la función de mostrar gráfico
			@Override
			public void handle(Event event) { // (1er click en participantes)
				/*
				 * ObservableList<model.Event> selectedEvents =
				 * listEvents.getSelectionModel().getSelectedItems();
				 * filterLogs.clear(); // Al seleccionar un participante
				 * reiniciamos el gráfico for (model.Event actualEvent :
				 * selectedEvents) {
				 * filterLogs.addAll(actualEvent.getLogsEvent());
				 * 
				 * } enrLog = FXCollections.observableArrayList(filterLogs);
				 * listLogs.setItems(enrLog);
				 */
				filterLogs();

			}
		});

		// Establecemos la estructura en árbol del calificador
		ArrayList<GradeReportLine> grcl = (ArrayList<GradeReportLine>) UBULog.session.getActualCourse()
				.getGradeReportLines();
		// Establecemos la raiz del Treeview
		TreeItem<GradeReportLine> root = new TreeItem<GradeReportLine>(grcl.get(0));
		MainController.setIcon(root);
		// Llamamos recursivamente para llenar el Treeview
		for (int k = 0; k < grcl.get(0).getChildren().size(); k++) {
			TreeItem<GradeReportLine> item = new TreeItem<GradeReportLine>(grcl.get(0).getChildren().get(k));
			MainController.setIcon(item);
			root.getChildren().add(item);
			root.setExpanded(true);
			setTreeview(item, grcl.get(0).getChildren().get(k));
		}

		dataUserLoger();
	}

	/**
	 * 
	 */
	private void viewChart() {
		engine.load(getClass().getResource("/chart/html/chart.html").toString());
	}

	private void filterLogs() {
		ObservableList<model.Event> selectedEvents = listEvents.getSelectionModel().getSelectedItems();
		ObservableList<EnrolledUser> selectedParticipants = listParticipants.getSelectionModel().getSelectedItems();
		
		filterLogs.clear();
		viewchart.getLabel().clear();
		

		if (!selectedEvents.isEmpty()) {
			for (model.Event actualEvent : selectedEvents) {
				filterLogs.addAll(actualEvent.getLogsEvent());

			}
			if (!selectedParticipants.isEmpty()) {
				boolean control = false;
				ArrayList<Log> filterAux = new ArrayList<>();
				filterAux.addAll(filterLogs);
				for (Log actualLog : filterAux) {
					for (EnrolledUser participant : selectedParticipants) {
						if (actualLog.getUser().equals(participant)) {
							control = true;
						}
					}
					if (control == false) {
						filterLogs.remove(actualLog);

					}
					control = false;
				}
			}

		} else {
			// TODO funciona bien si se selecciona solo usuario
			for (EnrolledUser actualUser : selectedParticipants) {
				for (Log actualLog : logs.getLogs()) {
					if (actualLog.getUser().equals(actualUser)) {
						filterLogs.add(actualLog);
					}
				}
			}

		}
		viewchart.setLabel(selectedParticipants, selectedEvents, filterLogs);
		viewchart.generarGrafica();
		
		enrLog = FXCollections.observableArrayList(filterLogs);
		listLogs.setItems(enrLog);
	}

	/**
	 * Establecemos los valores de los lavel que hacen referencia a los datos
	 * del usuario logeado.
	 */
	private void dataUserLoger() {
		// Mostramos Usuario logeado
		lblActualUser.setText("Usuario: " + UBULog.user.getFullName());

		// Mostramos Curso actual
		lblActualCourse.setText("Curso actual: " + UBULog.session.getActualCourse().getFullName());

		// Mostramos Host actual
		lblActualHost.setText("Host: " + UBULog.host);
	}

	/**
	 * Método para crear usuarios que no estan inscritos al curso, pero pueden
	 * tener interacciones en el.
	 */
	private void insertUserFicticios() {
		EnrolledUser userCreate = new EnrolledUser("Administrador", 2);
		userCreate.setlastName("Administrador");
		users.add(userCreate);

		userCreate = new EnrolledUser("Invitado", 1);
		userCreate.setlastName("Invitado");
		users.add(userCreate);

		userCreate = new EnrolledUser("Sistema", 0);
		userCreate.setlastName("Sistema");
		users.add(userCreate);

		userCreate = new EnrolledUser("Desconocido", -1);
		userCreate.setlastName("Desconocido");
		users.add(userCreate);
	}

	/**
	 * 
	 */
	private void manejoGrupos() {

		EventHandler<ActionEvent> actionGroup = selectGroup();
		// Cargamos una lista de los nombres de los grupos
		ArrayList<String> groupsList = UBULog.session.getActualCourse().getGroups();
		// Convertimos la lista a una lista de MenuItems para el MenuButton
		ArrayList<MenuItem> groupsItemsList = new ArrayList<MenuItem>();
		// En principio mostrarán todos los usuarios en cualquier grupo
		MenuItem mi = (new MenuItem("Todos"));
		// Añadimos el manejador de eventos al primer MenuItem
		mi.setOnAction(actionGroup);
		groupsItemsList.add(mi);

		for (int i = 0; i < groupsList.size(); i++) {
			String group = groupsList.get(i);
			mi = (new MenuItem(group));
			// Añadimos el manejador de eventos a cada MenuItem
			mi.setOnAction(actionGroup);
			groupsItemsList.add(mi);
		}
		// Asignamos la lista de MenuItems al MenuButton "Grupo"
		slcGroup.getItems().addAll(groupsItemsList);
		slcGroup.setText("Todos");
	}

	/**
	 * Manejo de roles
	 */
	private void manejoRoles() {
		EventHandler<ActionEvent> actionRole = selectRole();
		// Cargamos una lista con los nombres de los roles
		ArrayList<String> rolesList = UBULog.session.getActualCourse().getRoles();
		// Convertimos la lista a una lista de MenuItems para el MenuButton
		ArrayList<MenuItem> rolesItemsList = new ArrayList<MenuItem>();
		// En principio se mostrarón todos los usuarios con cualquier rol
		MenuItem mi = (new MenuItem("Todos"));
		// Añadimos el manejador de eventos al primer MenuItem
		mi.setOnAction(actionRole);
		rolesItemsList.add(mi);

		for (int i = 0; i < rolesList.size(); i++) {
			String rol = rolesList.get(i);
			mi = (new MenuItem(rol));
			mi.setOnAction(actionRole);
			// Añadimos el manejador de eventos a cada MenuItem
			rolesItemsList.add(mi);
		}

		// Asignamos la lista de MenuItems al MenuButton "Rol"
		slcRole.getItems().addAll(rolesItemsList);
		slcRole.setText("Todos");
	}

	/**
	 * Manejador de eventos para el botón de filtro por roles. Devuelve un
	 * manejador de eventos para cada item.
	 * 
	 * @return manejador de eventos de roles
	 */
	private EventHandler<ActionEvent> selectRole() {
		return new EventHandler<ActionEvent>() {
			/**
			 * Recibe un evento (relacionado con un MenuItem) y responde en
			 * consecuencia. El usuario elige un menuItem y filtra la lista de
			 * participantes
			 */
			public void handle(ActionEvent event) {
				// Obtenemos el ítem que se ha seleccionado
				MenuItem mItem = (MenuItem) event.getSource();
				// Obtenemos el rol por el que se quiere filtrar
				filterRole = mItem.getText();
				logger.info("-> Filtrando participantes por rol: " + filterRole);
				filterParticipants();
				slcRole.setText(filterRole);
			}
		};
	}

	/**
	 * Manejador de eventos para el botón de filtro por grupos. Devuelve un
	 * manejador de eventos para cada item.
	 * 
	 * @return manejador de eventos de grupos
	 */
	private EventHandler<ActionEvent> selectGroup() {
		return new EventHandler<ActionEvent>() {
			/**
			 * Recibe un evento (relacionado con un MenuItem) y responde en
			 * consecuencia. El usuario elige un menuItem y filtra la lista de
			 * participantes
			 */
			public void handle(ActionEvent event) {
				// Obtenemos el ítem que se ha seleccionado
				MenuItem mItem = (MenuItem) event.getSource();
				// Obtenemos el grupo por el que se quire filtrar
				filterGroup = mItem.getText();
				logger.info("-> Filtrando participantes por grupo: " + filterGroup);
				filterParticipants();
				slcGroup.setText(filterGroup);
			}
		};
	}

	/**
	 * Manejador de eventos para el textField de filtro de participantes.
	 * 
	 * @return manejador de eventos para el patrón de participantes
	 */
	private EventHandler<ActionEvent> inputParticipant() {
		return new EventHandler<ActionEvent>() {
			/**
			 * Recibe un evento (relacionado con un MenuItem) y responde en
			 * consecuencia. El usuario elige un menuItem y filtra la lista de
			 * participantes
			 */
			public void handle(ActionEvent event) {
				patternParticipants = tfdParticipants.getText();
				logger.info("-> Filtrando participantes por nombre: " + patternParticipants);
				filterParticipants();
			}
		};
	}

	/**
	 * Filtra los participantes según el rol, el grupo y el patrón indicados
	 */
	public void filterParticipants() {
		try {
			clearData();
			boolean roleYes;
			boolean groupYes;
			boolean patternYes;
			users = (ArrayList<EnrolledUser>) UBULog.session.getActualCourse().getEnrolledUsers();
			// insertUserFicticios();
			// Cargamos la lista de los roles
			ArrayList<EnrolledUser> nameUsers = new ArrayList<EnrolledUser>();
			// Obtenemos los participantes que tienen el rol elegido
			for (int i = 0; i < users.size(); i++) {
				// Filtrado por rol:
				roleYes = false;
				ArrayList<Role> roles = users.get(i).getRoles();
				// Si no tiene rol
				if ((roles == null || roles.size() == 0) && filterRole.equals("Todos")) {
					roleYes = true;
				} else {
					for (int j = 0; j < roles.size(); j++) {
						// Comprobamos si el usuario pasa el filtro de "rol"
						if (roles.get(j).getName().equals(filterRole) || filterRole.equals("Todos")) {
							roleYes = true;
						}
					}
				}
				// Filtrado por grupo:
				groupYes = false;
				ArrayList<Group> groups = users.get(i).getGroups();
				if ((groups == null || groups.size() == 0) && filterGroup.equals("Todos")) {
					groupYes = true;
				} else {
					for (int k = 0; k < groups.size(); k++) {
						// Comprobamos si el usuario pasa el filtro de "grupo"
						if (groups.get(k).getName().equals(filterGroup) || filterGroup.equals("Todos")) {
							groupYes = true;
						}
					}
				}
				// Filtrado por patrón:
				patternYes = false;
				if (patternParticipants.equals("")) {
					patternYes = true;
				} else {
					Pattern pattern = Pattern.compile(patternParticipants);
					Matcher match = pattern.matcher(users.get(i).getFullName());
					if (match.find()) {
						patternYes = true;
					}
				}
				// Si el usuario se corresponde con los filtros
				if (groupYes && roleYes && patternYes)
					nameUsers.add(users.get(i));
			}
			enrList = FXCollections.observableArrayList(nameUsers);
			// Mostramos nievo número participantes
			lblCountParticipants.setText("Participantes: " + nameUsers.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
		listParticipants.setItems(enrList);
	}

	/**
	 * Manejador de eventos para las actividades. Devuelve un manejador de
	 * eventos para cada item.
	 * 
	 * @return manejador de eventos para las actividades
	 */
	public EventHandler<ActionEvent> inputEvent() {
		return new EventHandler<ActionEvent>() {
			/**
			 * Recibe un evento (relacionado con un TreeItem) y responde en
			 * consecuencia. El usuario elige un menuItem y filtra la lista de
			 * participantes
			 */
			public void handle(ActionEvent event) {
				patternEvents = tfdEvents.getText();
				logger.info("-> Filtrando calificador por nombre: " + patternEvents);
				filterEvents();
			}
		};
	}

	/**
	 * Filtra la lista de actividades del calificador según el tipo y el patrón
	 * introducidos.
	 */
	public void filterEvents() {
		try {
			clearData();
			boolean patternYes;
			ArrayList<model.Event> filterevents = new ArrayList<>();
			eventList = FXCollections.observableArrayList(logs.getEvents().values());

			// Obtenemos los participantes que tienen el rol elegido
			for (int i = 0; i < eventList.size(); i++) {
				// Filtrado por patrón:
				patternYes = false;
				if (patternEvents.equals("")) {
					patternYes = true;
				} else {
					Pattern pattern = Pattern.compile(patternEvents);
					Matcher match = pattern.matcher(eventList.get(i).getNameEvent());
					if (match.find()) {
						patternYes = true;
					}
				}
				// Si el usuario se corresponde con los filtros
				if (patternYes)
					filterevents.add(eventList.get(i));
			}
			eventList = FXCollections.observableArrayList(filterevents);

		} catch (Exception e) {
			e.printStackTrace();
		}
		listEvents.setItems(eventList);
	}

	/**
	 * Rellena el árbol de actividades (GradeReportLines). Obtiene los hijos de
	 * la línea pasada por parámetro, los transforma en treeitems y los
	 * establece como hijos del elemento treeItem equivalente de line
	 * 
	 * @param parent
	 * @param line
	 */
	public void setTreeview(TreeItem<GradeReportLine> parent, GradeReportLine line) {
		for (int j = 0; j < line.getChildren().size(); j++) {
			TreeItem<GradeReportLine> item = new TreeItem<GradeReportLine>(line.getChildren().get(j));
			MainController.setIcon(item);
			parent.getChildren().add(item);
			parent.setExpanded(true);
			setTreeview(item, line.getChildren().get(j));
		}
	}

	/**
	 * Añade un icono a cada elemento del árbol según su tipo de actividad
	 * 
	 * @param item
	 */
	public static void setIcon(TreeItem<GradeReportLine> item) {
		// logger.info(item.getValue().getNameType());
		switch (item.getValue().getNameType()) {
		case "Assignment":
			item.setGraphic((Node) new ImageView(new Image("/img/assignment.png")));
			break;
		case "Quiz":
			item.setGraphic((Node) new ImageView(new Image("/img/quiz.png")));
			break;
		case "ManualItem":
			item.setGraphic((Node) new ImageView(new Image("/img/manual_item.png")));
			break;
		case "Category":
			item.setGraphic((Node) new ImageView(new Image("/img/folder.png")));
			break;
		case "Forum":
			item.setGraphic((Node) new ImageView(new Image("/img/forum.png")));
		default:
			break;
		}
	}

	/**
	 * Cambia la asignatura actual y carga otra
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void changeCourse(ActionEvent actionEvent) throws Exception {
		logger.info("Cambiando de asignatura...");
		// Accedemos a la siguiente ventana
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Welcome.fxml"));
		// UBULog.stage.getScene() setCursor(Cursor.WAIT);
		UBULog.stage.close();
		logger.info("Accediendo a UBULog...");
		UBULog.stage = new Stage();
		Parent root = loader.load();
		// root.setCursor(Cursor.WAIT);
		Scene scene = new Scene(root);
		UBULog.stage.setScene(scene);
		UBULog.stage.getIcons().add(new Image("/img/logo_min.png"));
		UBULog.stage.setTitle("UBULog");
		UBULog.stage.show();
	}

	/**
	 * Exporta el gráfico. El usuario podrá elegir entre el formato .png o .jpg
	 * para guardar la imagen.
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void saveChart(ActionEvent actionEvent) throws Exception {
		WritableImage image = lineChart.snapshot(new SnapshotParameters(), null);

		File file = new File("chart.png");

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Guardar gráfico");

		fileChooser.setInitialFileName("chart");
		fileChooser.setInitialDirectory(file.getParentFile());
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(".png", "*.*"),
				new FileChooser.ExtensionFilter("*.jpg", "*.jpg"), new FileChooser.ExtensionFilter("*.png", "*.png"));
		try {
			file = fileChooser.showSaveDialog(UBULog.stage);
			if (file != null) {
				try {
					ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
				} catch (IOException ex) {
					logger.info(ex.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param actionEvent
	 * @throws Exception
	 */
	public void generateChart(ActionEvent actionEvent) throws Exception {

		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void saveTable(ActionEvent actionEvent) throws Exception {

		// TODO Arreglar para exportar el log resultante si se quiere.
		/*
		 * WritableImage image = webView.snapshot(new SnapshotParameters(),
		 * null);
		 * 
		 * File file = new File("table.png");
		 * 
		 * FileChooser fileChooser = new FileChooser();
		 * fileChooser.setTitle("Guardar tabla");
		 * fileChooser.setInitialFileName("table");
		 * fileChooser.getExtensionFilters().addAll(new
		 * FileChooser.ExtensionFilter(".png", "*.*"), new
		 * FileChooser.ExtensionFilter("*.jpg", "*.jpg"), new
		 * FileChooser.ExtensionFilter("*.png", "*.png")); try { file =
		 * fileChooser.showSaveDialog(UBULog.stage);
		 * logger.info(file.getAbsolutePath()); if (file != null) { try {
		 * ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file); }
		 * catch (IOException ex) { logger.info(ex.getMessage()); } } } catch
		 * (Exception e) { e.printStackTrace(); }
		 */
	}

	/**
	 * Vuelve a la ventana de login de usuario
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void logOut(ActionEvent actionEvent) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Login.fxml"));
		UBULog.stage.close();
		logger.info("Cerrando sesión de usuario");
		UBULog.stage = new Stage();
		Parent root = loader.load();
		Scene scene = new Scene(root);
		UBULog.stage.setScene(scene);
		UBULog.stage.getIcons().add(new Image("/img/logo_min.png"));
		UBULog.stage.setTitle("UBULog");
		UBULog.stage.show();
	}

	/**
	 * Deja de seleccionar los participantes/actividades y borra el gráfico.
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void clearSelection(ActionEvent actionEvent) throws Exception {
		listParticipants.getSelectionModel().clearSelection();
		listEvents.getSelectionModel().clearSelection();
		filterLogs.clear();

		// TODO asi recupero el log completo, quizas no haga falta ya que al
		// filtrar lo tengo que comprobar con el completo siempre.resistac
		enrLog = FXCollections.observableArrayList(logs.getLogs());
		listLogs.setItems(enrLog);

		clearData();
	}

	public void clearData() {
		// TODO eliminar referencias. no existe linechart
		//lineChart.getData().clear();
	}

	/**
	 * Abre en el navegador el repositorio del proyecto.
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void aboutUBULog(ActionEvent actionEvent) throws Exception {
		Desktop.getDesktop().browse(new URL("https://github.com/trona85/GII-17.1B-UBULog-1.0").toURI());
	}

	/**
	 * Boton para cargar documento
	 * 
	 * @param actionEvent
	 */
	public void cargaDocumento(ActionEvent actionEvent) {
		try {
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showOpenDialog(UBULog.stage);

			if (file == null) {
				throw new UBULogException(UBULogError.FICHERO_CANCELADO);
			}
			if (!file.toString().contains(".csv")) {
				throw new UBULogException(UBULogError.FICHERO_NO_VALIDO);
			}

			this.logs = new CsvParser(file.toString());
			logs.readDocument();

			for (int i = 0; i < logs.getLogs().size(); ++i) {
				for (int j = 0; j < users.size(); j++) {
					if (logs.getLogs().get(i).getIdUser() == users.get(j).getId()) {
						logs.getLogs().get(i).setUser(users.get(j));
						break;
					}
					// TODO comprobar meses con una cifra
					//System.err.println(logs.getLogs().get(i).getDate().substring(3, 5) + "\n");
					viewchart.setDate(logs.getLogs().get(i).getDate().get(Calendar.MONTH));
				}
			}

			initializeDataSet(logs);
		} catch (UBULogException e) {
			logger.info(e.getMessage());
			if (e.getError() != UBULogError.FICHERO_CANCELADO) {
				cargaDocumento(actionEvent);

			}

		}

	}

	/**
	 * Inicializamos los datos necesarios
	 * 
	 * @param logs
	 */
	private void initializeDataSet(CsvParser logs) {

		// dejamos seleccionar participantes
		listParticipants.setDisable(false);
		btnchart.setDisable(false);
		// Activamos la selección múltiple en la lista de participantes y
		// eventos

		listParticipants.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		listEvents.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		/// Mostramos la lista de participantes y eventos
		enrLog = FXCollections.observableArrayList(logs.getLogs());
		eventList = FXCollections.observableArrayList(logs.getEvents().values());

		listLogs.setItems(enrLog);
		// TODO vienen desordenados
		listEvents.setItems(eventList);

	}

	/**
	 * 
	 * Botón "Salir". Cierra la aplicación.
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void closeApplication(ActionEvent actionEvent) throws Exception {
		logger.info("Cerrando aplicación");
		UBULog.stage.close();
	}

	public static void errorDeConexion() {
		Alert alert = new Alert(AlertType.ERROR);

		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(UBULog.stage);
		alert.getDialogPane().setContentText("Su equipo ha perdido la conexión a Internet");

		logger.warn("Su equipo ha perdido la conexión a Internet");
		ButtonType buttonSalir = new ButtonType("Cerrar UBULog");
		alert.getButtonTypes().setAll(buttonSalir);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonSalir)
			UBULog.stage.close();
	}
}