/**
 * 
 */
package controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
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
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Chart;
import model.EnrolledUser;
import model.Group;
import model.Log;
import model.Role;
import parserdocument.CsvParser;
import ubulogexception.UBULogError;
import ubulogexception.UBULogException;
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
	public Button btnchart;

	@FXML // Entrada de filtro de actividades por patrón
	public TextField tfdEvents;
	String patternEvents = "";

	@FXML // Entrada de filtro de actividades por patrón
	public TextField tfdDate;
	String patternDate = "";

	@FXML
	public TextField tfdNameUser;
	String patternNameUser = "";

	@FXML
	public TextField tfdUserAffected;
	String patternUserAffected = "";

	@FXML
	public TextField tfdContext;
	String patternContext = "";

	@FXML
	public TextField tfdComponent;
	String patternComponent = "";

	@FXML
	public TextField tfdEvent;
	String patternEvent = "";

	@FXML
	public TextField tfdDescription;
	String patternDescription = "";

	@FXML
	public TextField tfdPOrigin;
	String patternOrigin = "";

	@FXML
	public TextField tfdIp;
	String patternIp = "";

	@FXML // chart ,imagen log
	private WebView chart;
	private WebEngine engineChart;

	@FXML // chart ,imagen log
	private WebView tableLogs;
	private WebEngine engineTableLogs;
	@FXML
	private WebView imageLoger;
	private WebEngine engineImagen;

	private ArrayList<EnrolledUser> users;
	private CsvParser logs;
	private ArrayList<Log> filterLogs;
	private ArrayList<Log> filterTableLogs;
	private Chart viewchart;
	private ObservableList<model.Event> selectedEvents = null;
	private ObservableList<EnrolledUser> selectedParticipants = null;

	private EnrolledUser userDesconocido;

	/**
	 * Muestra los usuarios matriculados en el curso, así como las actividades
	 * de las que se compone.
	 */
	public void initialize(URL location, ResourceBundle resources) {
		filterLogs = new ArrayList<>();
		filterTableLogs = new ArrayList<>();

		try {
			logger.info(" Cargando curso '" + UBULog.session.getActualCourse().getFullName() + "'...");

			viewchart = new Chart();
			setDisableComponentInterfaz(true);

			engineChart = chart.getEngine();
			engineTableLogs = tableLogs.getEngine();

			loadHTML(new ArrayList<Log>());

			viewHTML();
			// Establecemos los usuarios matriculados
			CourseWS.setEnrolledUsers(UBULog.session.getToken(), UBULog.session.getActualCourse());

			// Almacenamos todos los participantes en una lista
			users = UBULog.session.getActualCourse().getEnrolledUsers();

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

			// Inicializamos el listener del textField filtros de la tabla log
			tfdDate.setOnAction(inputTable());
			tfdNameUser.setOnAction(inputTable());
			tfdUserAffected.setOnAction(inputTable());
			tfdContext.setOnAction(inputTable());
			tfdComponent.setOnAction(inputTable());
			tfdEvent.setOnAction(inputTable());
			tfdDescription.setOnAction(inputTable());
			tfdPOrigin.setOnAction(inputTable());
			tfdIp.setOnAction(inputTable());

			dataUserLoger();

		} catch (Exception e) {
			logger.error("Error en la inicialización. {}", e);
		}

		// Asignamos el manejador de eventos de la lista
		// Al clickar en la lista, se recalcula el número de elementos
		// seleccionados de participantes.
		listParticipants.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				filterLogs();

			}
		});

		// Asignamos el manejador de eventos de la lista
		// Al clickar en la lista, se recalcula el número de elementos
		// seleccionados de eventos.
		listEvents.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {

				filterLogs();

			}
		});
	}

	/**
	 * 
	 */
	private void loadHTML(ArrayList<Log> l) {
		viewchart.generarGrafica();
		engineChart.reload();

		generarTablaLogs(l);
		engineTableLogs.reload();
	}

	/**
	 * 
	 */
	private void viewHTML() {
		engineChart.load(getClass().getResource("/chart/html/chart.html").toString());
		engineTableLogs.load(getClass().getResource("/tablelogs/html/tablelogs.html").toString());
	}

	private void filterLogs() {
		selectedEvents = listEvents.getSelectionModel().getSelectedItems();
		selectedParticipants = listParticipants.getSelectionModel().getSelectedItems();

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
			for (EnrolledUser actualUser : selectedParticipants) {
				for (Log actualLog : logs.getLogs()) {
					if (actualLog.getUser().equals(actualUser)) {
						filterLogs.add(actualLog);
					}
				}
			}

		}
		viewchart.setLabel(selectedParticipants, selectedEvents, filterLogs);
		loadHTML(filterLogs);

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

		// imagen del logeado
		// Revisas no me pasa la imagen correspondiente al logeado
		// imageLoger = new WebView();

		// TODO falta si no existe ponerle un icono nuestro
		engineImagen = imageLoger.getEngine();
		engineImagen.load(UBULog.user.getProfileImageUrlSmall());

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

		userDesconocido = new EnrolledUser("Desconocido", -1);
		userDesconocido.setlastName("Desconocido");
		users.add(userDesconocido);
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
			logger.error("Error en el filtro participantes. {}", e);
		}
		listParticipants.setItems(enrList);
	}

	public EventHandler<ActionEvent> inputTable() {
		return new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				ArrayList<String> patternFilter = new ArrayList<>();
				patternFilter.add(tfdDate.getText());
				patternFilter.add(tfdNameUser.getText());
				patternFilter.add(tfdUserAffected.getText());
				patternFilter.add(tfdContext.getText());
				patternFilter.add(tfdComponent.getText());
				patternFilter.add(tfdEvent.getText());
				patternFilter.add(tfdDescription.getText());
				patternFilter.add(tfdPOrigin.getText());
				patternFilter.add(tfdIp.getText());
				logger.info("-> Filtrando tabla: \n Fecha :" + patternFilter.get(0) + "\n Usuario afectado: "
						+ patternFilter.get(1) + "\n usuario afectado: " + patternFilter.get(2) + "\n contexto: "
						+ patternFilter.get(3) + "\n Componente: " + patternFilter.get(4) + "\n Evento: "
						+ patternFilter.get(5) + "\n Descripción: " + patternFilter.get(6) + "\n Origen: "
						+ patternFilter.get(7) + "\n ip: " + patternFilter.get(8));
				filterTable(patternFilter);
			}
		};
	}

	protected void filterTable(ArrayList<String> patternFilter) {
		ArrayList<Boolean> patterncomp = new ArrayList<>();
		// TODO mejorar código
		patterncomp.add(false);
		patterncomp.add(false);
		patterncomp.add(false);
		patterncomp.add(false);
		patterncomp.add(false);
		patterncomp.add(false);
		patterncomp.add(false);
		patterncomp.add(false);
		patterncomp.add(false);
		filterTableLogs.clear();
		try {
			if (filterLogs.isEmpty()) {
				// buscar en log completo

				filtroTableLogs(patternFilter, patterncomp, logs.getLogs());

			} else {
				// buscar en filterlog
				filtroTableLogs(patternFilter, patterncomp, filterLogs);
			}

			generarTablaLogs(filterTableLogs);
			engineTableLogs.reload();

		} catch (Exception e) {
			logger.error("Error en el filtro de tabla. {}", e);
		}

	}

	/**
	 * @param patternFilter
	 * @param patterncomp
	 * @param ftLogs
	 */
	private void filtroTableLogs(ArrayList<String> patternFilter, ArrayList<Boolean> patterncomp,
			ArrayList<Log> ftLogs) {

		for (int i = 0; i < ftLogs.size(); i++) {
			for (int j = 0; j < patternFilter.size(); j++) {
				if (patternFilter.get(j).equals("")) {
					// igual no funciona
					patterncomp.set(j, true);
				} else {

					Pattern pattern = Pattern.compile(patternFilter.get(j));
					Matcher match = null;
					switch (j) {
					case 0:
						match = pattern.matcher(ftLogs.get(i).getDate().get(Calendar.DAY_OF_MONTH) + "/"
								+ ftLogs.get(i).getDate().get(Calendar.MONTH) + "/"
								+ ftLogs.get(i).getDate().get(Calendar.YEAR));

						break;
					case 1:
						match = pattern.matcher(ftLogs.get(i).getNameUser());

						break;
					case 2:
						match = pattern.matcher(ftLogs.get(i).getUserAffected());
						break;
					case 3:
						match = pattern.matcher(ftLogs.get(i).getContext());
						break;
					case 4:
						match = pattern.matcher(ftLogs.get(i).getComponent());
						break;
					case 5:
						match = pattern.matcher(ftLogs.get(i).getEvent());
						break;
					case 6:
						match = pattern.matcher(ftLogs.get(i).getDescription());
						break;
					case 7:
						match = pattern.matcher(ftLogs.get(i).getOrigin());
						break;
					case 8:
						match = pattern.matcher(ftLogs.get(i).getIp());
						break;

					default:
						break;
					}
					if (match.find()) {
						patterncomp.set(j, true);
					}
				}
			}
			if (patterncomp.get(0) == true && patterncomp.get(1) == true && patterncomp.get(2) == true
					&& patterncomp.get(3) == true && patterncomp.get(4) == true && patterncomp.get(5) == true
					&& patterncomp.get(6) == true && patterncomp.get(7) == true && patterncomp.get(8) == true) {
				filterTableLogs.add(ftLogs.get(i));
			}
			for (int k = 0; k < patterncomp.size(); k++) {
				patterncomp.set(k, false);
			}
		}
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
			logger.error("Error en filtro de eventos {}", e);
		}
		listEvents.setItems(eventList);
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

		WritableImage image = chart.snapshot(new SnapshotParameters(), null);
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
			logger.error("Error en guardado de gráfico. {}", e);
		}

	}

	/**
	 * @param actionEvent
	 * @throws Exception
	 */
	public void generateChart(ActionEvent actionEvent) {
		viewchart.setLabel(selectedParticipants, selectedEvents, filterTableLogs);
		viewchart.generarGrafica();
		engineChart.reload();
	}

	/**
	 * 
	 */
	public void saveTable(ActionEvent actionEvent) throws Exception {

		// TODO revisar en java fx antes de eliminar.
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
		if (!logs.getLogs().isEmpty()) {
			listParticipants.getSelectionModel().clearSelection();
			listEvents.getSelectionModel().clearSelection();
			filterLogs.clear();
			viewchart.getDate().clear();
			viewchart.getLabel().clear();
			loadHTML(new ArrayList<Log>());
			// TODO asi recupero el log completo, quizas no haga falta ya que al
			// filtrar lo tengo que comprobar con el completo siempre.resistac
			enrLog = FXCollections.observableArrayList(logs.getLogs());
			listLogs.setItems(enrLog);
		}

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
			this.logs = new CsvParser();
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showOpenDialog(UBULog.stage);
			if (file == null) {
				throw new UBULogException(UBULogError.FICHERO_CANCELADO);
			}
			if (!file.toString().contains(".csv")) {
				throw new UBULogException(UBULogError.FICHERO_NO_VALIDO);
			}

			// leemos csv y lo parseamos

			logs.setFile(file.toString());
			logs.readDocument();

			asignedUserMonth();

			initializeDataSet(logs);

		} catch (UBULogException e) {
			logger.info(e.getMessage());
			if (e.getError() != UBULogError.FICHERO_CANCELADO) {
				cargaDocumento(actionEvent);

			}

		}

	}

	/**
	 * 
	 */
	private void asignedUserMonth() {
		for (int i = 0; i < logs.getLogs().size(); ++i) {
			// insetamos fecha
			viewchart.setDate(logs.getLogs().get(i).getDate().get(Calendar.MONTH));

			// comprobamos la existencia de usaer y la insertamos
			for (int j = 0; j < users.size(); j++) {
				if (logs.getLogs().get(i).getIdUser() == users.get(j).getId()) {
					logs.getLogs().get(i).setUser(users.get(j));
				}

				if (j == users.size() - 1 && logs.getLogs().get(i).getUser() == null) {
					logs.getLogs().get(i).setUser(userDesconocido);
				}
			}
		}
	}

	/**
	 * Boton para cargar documento online
	 * 
	 * @param actionEvent
	 * @throws IOException
	 */
	public void cargaDocumentoOnline(ActionEvent actionEvent) throws IOException {
		WebClient client = null;
		this.logs = new CsvParser();

		HtmlPage page = null;
		FileWriter fileWriter = null;
		PrintWriter pw = null;
		File file = null;
		try {
			client = new WebClient(BrowserVersion.getDefault());
			page = client.getPage(UBULog.host + "/login/index.php");
			HtmlForm form = (HtmlForm) page.getElementById("login");

			form.getInputByName("username").setValueAttribute(UBULog.session.getUserName());
			form.getInputByName("password").setValueAttribute(UBULog.session.getPassword());

			page.getElementById("loginbtn").click();
			page = client.getPage(UBULog.host + "/report/log/index.php?chooselog=1&showusers=0&showcourses=0&id="
					+ UBULog.session.getActualCourse().getId()
					+ "&user=&date=&modid=&modaction=&origin=&edulevel=-1&logreader=logstore_standard");

			page.getElementsByTagName("button").get(1).click().getWebResponse().getContentAsStream();
			WebResponse dataDownload = page.getElementsByTagName("button").get(1).click().getWebResponse();
			String csvtxt = dataDownload.getContentAsString();
			
			try {
				fileWriter = new FileWriter("./tempcsv.csv");

				pw = new PrintWriter(fileWriter);
				// TODO el String esta completo pero no lo copia en el
				// fichero....

				pw.print(csvtxt);
				
			} finally {
				if (pw != null){
					pw.close();
					
				}
				if(fileWriter != null)
					fileWriter.close();
			}

			file = new File("tempcsv.csv");
			logs.setFile(file.getAbsolutePath());
			logs.readDocument();
			file.delete();
			asignedUserMonth();

			initializeDataSet(logs);

		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UBULogException e) {
			logger.info(e.getMessage());

		} finally {
			if (client != null) {
				client.close();
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
		setDisableComponentInterfaz(false);
		// Activamos la selección múltiple en la lista de participantes y
		// eventos

		listParticipants.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		listEvents.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		/// Mostramos la lista de participantes y eventos
		enrLog = FXCollections.observableArrayList(logs.getLogs());

		eventList = FXCollections.observableArrayList(logs.getEvents().values());
		loadHTML(logs.getLogs());
		Collections.sort(eventList, (o1, o2) -> o1.getNameEvent().compareTo(o2.getNameEvent()));
		listLogs.setItems(enrLog);
		listEvents.setItems(eventList);

	}

	/**
	 * @param disable
	 * 
	 */
	private void setDisableComponentInterfaz(boolean disable) {
		listParticipants.setDisable(disable);
		tfdParticipants.setDisable(disable);
		tfdEvents.setDisable(disable);
		listEvents.setDisable(disable);
		btnchart.setDisable(disable);
		tfdDate.setDisable(disable);
		tfdNameUser.setDisable(disable);
		tfdUserAffected.setDisable(disable);
		tfdContext.setDisable(disable);
		tfdComponent.setDisable(disable);
		tfdEvent.setDisable(disable);
		tfdDescription.setDisable(disable);
		tfdPOrigin.setDisable(disable);
		tfdIp.setDisable(disable);
	}

	private void generarTablaLogs(ArrayList<Log> generateLogs) {

		FileWriter ficheroHTML = null;
		PrintWriter pw = null;
		try {
			ficheroHTML = new FileWriter("bin/tablelogs/html/tablelogs.html");
			pw = new PrintWriter(ficheroHTML);
			pw.println("<!DOCTYPE html> \n " + "<html> \n " + "<title>tabla logs</title> \n"
					+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> \n"
					+ "<meta charset=\"utf-8\"> \n"
					+ "<link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">\n" + "<body>\n"
					+ "<div class=\"w3-container\"> \n" + "\t<h2>Tabla de logs</h2>");

			pw.println("\t<table class=\"w3-table-all w3-margin-top\" id=\"myTable\">");
			pw.println("\t\t<tr> \n" + "\t\t<th>Fecha </th>");
			pw.println("\t\t<th>Nombre completo del usuario</th>");
			pw.println("\t\t<th>Usuario afectado</th>");
			pw.println("\t\t<th>Contexto del evento</th>");
			pw.println("\t\t<th>Componente</th>");
			pw.println("\t\t<th>Nombre evento</th>");
			pw.println("\t\t<th>Descripción</th>");
			pw.println("\t\t<th>Origen</th>");
			pw.println("\t\t<th>Dirección IP</th>");
			pw.println("\t</tr>");

			for (Log log : generateLogs) {
				dataTableLog(pw, log);
			}

			pw.println("\t</table>");
			pw.println("</div> \n" + "</body>\n" + "</html>");

		} catch (Exception e) {
			logger.error("Error al generar tabla logs. {}", e);
		} finally {
			if (pw != null) {
				pw.close();
			}
			try {
				if (null != ficheroHTML)
					ficheroHTML.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * @param pw
	 * @param log
	 */
	private void dataTableLog(PrintWriter pw, Log log) {
		pw.println("\t<tr>");

		pw.println("\t\t<td>" + log.getDate().get(Calendar.DAY_OF_MONTH) + "/" + log.getDate().get(Calendar.MONTH) + "/"
				+ log.getDate().get(Calendar.YEAR) + "</td>");
		pw.println("\t\t<td>" + log.getNameUser() + "</td>");
		pw.println("\t\t<td>" + log.getUserAffected() + "</td>");
		pw.println("\t\t<td>" + log.getContext() + "</td>");
		pw.println("\t\t<td>" + log.getComponent() + "</td>");
		pw.println("\t\t<td>" + log.getEvent() + "</td>");
		pw.println("\t\t<td>" + log.getDescription() + "</td>");
		pw.println("\t\t<td>" + log.getOrigin() + "</td>");
		pw.println("\t\t<td>" + log.getIp() + "</td>");

		pw.println("\t</tr>");
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