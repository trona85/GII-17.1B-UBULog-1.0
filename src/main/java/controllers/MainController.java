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

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

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
import model.TableLog;
import parserdocument.CsvParser;
import ubulogexception.UBULogError;
import ubulogexception.UBULogException;
import webservice.CourseWS;

/**
 * Clase controlador de la ventana principal
 * 
 * @author Oscar Fernández Armengol
 * @author Claudia Martínez Herrero
 * 
 * @version 1.0
 */
public class MainController implements Initializable {

	static final Logger logger = LoggerFactory.getLogger(MainController.class);

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

	@FXML // Botón filtro por rol
	public MenuButton slcRole;
	MenuItem[] roleMenuItems;
	String filterRole = "Todos";

	@FXML // Botón filtro por grupo
	public MenuButton slcGroup;
	MenuItem[] groupMenuItems;
	String filterGroup = "Todos";
	
	@FXML // Botón selector gráfico
	public MenuButton slcChart;

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

	private ArrayList<EnrolledUser> users;
	private CsvParser logs;
	private ArrayList<Log> filterLogs;
	private ArrayList<Log> filterTableLogs;
	private Chart viewchart;
	private TableLog viewTableLog;
	private ObservableList<model.Event> selectedEvents = null;
	private ObservableList<EnrolledUser> selectedParticipants = null;

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
			viewTableLog = new TableLog();
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
			
			EventHandler<ActionEvent> actionChart = selectChart();
			// Cargamos una lista de los nombres de los grupos
			//ArrayList<String> groupsList = UBULog.session.getActualCourse().getGroups();
			// Convertimos la lista a una lista de MenuItems para el MenuButton
			ArrayList<MenuItem> groupsItemsList = new ArrayList<>();
			// En principio mostrarán todos los usuarios en cualquier grupo
			MenuItem mi = (new MenuItem());
			// Añadimos el manejador de eventos al primer MenuItem
			
			//TODO añadimos cada tipo de grafico
			mi = typeChart(actionChart, groupsItemsList, mi, "Vertical", "bar");
			mi = typeChart(actionChart, groupsItemsList, mi, "Hotizontal", "horizontalBar");
			mi = typeChart(actionChart, groupsItemsList, mi, "Lineas basicas", "line");
			
			// Asignamos la lista de MenuItems al MenuButton "Grupo"
			slcChart.getItems().addAll(groupsItemsList);
			//
			viewchart.setTypeChart("bar");

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
	 * Metodo para añadir tipo de gráfico al selector.
	 * @param actionChart, actionChart.
	 * @param groupsItemsList, groupsItemsList.
	 * @param mi, item.
	 * @param value, valor.
	 * @param key, clave.
	 * @return mi
	 */
	private MenuItem typeChart(EventHandler<ActionEvent> actionChart, ArrayList<MenuItem> groupsItemsList,
			MenuItem mi, String value, String key) {
		mi = new MenuItem(value);
		mi.setId(key);
		mi.setOnAction(actionChart);
		groupsItemsList.add(mi);
		return mi;
	}

	/**
	 * Método que recarga de nuevo el gráfico y la tabla.
	 * @param l , lista de log.
	 */
	private void loadHTML(ArrayList<Log> l) {
		viewchart.generarGrafica();
		engineChart.reload();

		viewTableLog.generarTablaLogs(l);
		engineTableLogs.reload();
	}

	/**
	 * Método que carga los html de la gráfica y la tabla de logs.
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
					if (!control) {
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
	}

	/**
	 * Establecemos los valores de los lavel que hacen referencia a los datos
	 * del usuario logeado.
	 */
	private void dataUserLoger() {

		WebEngine engineImagen;
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
		ArrayList<MenuItem> groupsItemsList = new ArrayList<>();
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
		ArrayList<MenuItem> rolesItemsList = new ArrayList<>();
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
	 * Manejador de eventos para el botón selector de gráficos, selecciona un tipo de gráfico.
	 * 
	 * @return manejador de eventos de grupos
	 */
	private EventHandler<ActionEvent> selectChart() {
		return new EventHandler<ActionEvent>() {
			/**
			 * Recibe un evento (relacionado con un MenuItem) y responde en
			 * consecuencia. El usuario elige un menuItem y cambia el tipo de gráfico.
			 */
			public void handle(ActionEvent chart) {
				
				MenuItem mItem = (MenuItem) chart.getSource();
				viewchart.setTypeChart(mItem.getId());
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
			users = UBULog.session.getActualCourse().getEnrolledUsers();

			// Cargamos la lista de los roles
			ArrayList<EnrolledUser> nameUsers = new ArrayList<>();
			// Obtenemos los participantes que tienen el rol elegido
			for (int i = 0; i < users.size(); i++) {
				// Filtrado por rol:
				roleYes = false;
				ArrayList<Role> roles = users.get(i).getRoles();
				// Si no tiene rol
				if ((roles == null || roles.isEmpty()) && filterRole.equals("Todos")) {
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
				if ((groups == null || groups.isEmpty()) && filterGroup.equals("Todos")) {
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

		for (int i = 0; i < 9; ++i) {
			patterncomp.add(false);
		}
		filterTableLogs.clear();
		try {
			if (filterLogs.isEmpty()) {
				// buscar en log completo

				filtroTableLogs(patternFilter, patterncomp, logs.getLogs());

			} else {
				// buscar en filterlog
				filtroTableLogs(patternFilter, patterncomp, filterLogs);
			}

			viewTableLog.generarTablaLogs(filterTableLogs);
			engineTableLogs.reload();

		} catch (Exception e) {
			logger.error("Error en el filtro de tabla. {}", e);
		}

	}

	/**
	 * Método que filtra los log de la tabla.
	 * 
	 * @param patternFilter,
	 *            Lista del contenido puesto en los filtros
	 * @param patterncomp,
	 *            lista de booleanos.
	 * @param ftLogs,
	 *            logs filtrados.
	 */
	private void filtroTableLogs(ArrayList<String> patternFilter, ArrayList<Boolean> patterncomp,
			ArrayList<Log> ftLogs) {

		for (int i = 0; i < ftLogs.size(); i++) {
			for (int j = 0; j < patternFilter.size(); j++) {
				if (patternFilter.get(j).equals("")) {
					patterncomp.set(j, true);
				} else {

					Pattern pattern = Pattern.compile(patternFilter.get(j));
					Matcher match = null;
					switch (j) {
					case 0:
						match = pattern.matcher(ftLogs.get(i).getDate().get(Calendar.DAY_OF_MONTH) + "/"
								+ (ftLogs.get(i).getDate().get(Calendar.MONTH)+1) + "/"
								+ ftLogs.get(i).getDate().get(Calendar.YEAR) + " "
								+ ftLogs.get(i).getDate().get(Calendar.HOUR_OF_DAY) + ":"
								+ ftLogs.get(i).getDate().get(Calendar.MINUTE));

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
						match = pattern.matcher("*");
						break;
					}
					if (match.find()) {
						patterncomp.set(j, true);
					}
				}
			}
			if (patterncomp.get(0) && patterncomp.get(1) && patterncomp.get(2) && patterncomp.get(3)
					&& patterncomp.get(4) && patterncomp.get(5) && patterncomp.get(6) && patterncomp.get(7)
					&& patterncomp.get(8)) {
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
	 * @param actionEvent,
	 *            acción del evento.
	 * @throws Exception
	 *             , escepción.
	 */
	public void changeCourse(ActionEvent actionEvent) throws Exception {
		logger.info("Cambiando de asignatura...");
		// Accedemos a la siguiente ventana
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Welcome.fxml"));
		UBULog.stage.close();
		logger.info("Accediendo a UBULog...");
		UBULog.stage = new Stage();
		Parent root = loader.load();

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
	 * @param actionEvent,
	 *            acción del evento.
	 * @throws Exception
	 *             excepción
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
	 * Método para generar gráfica dependiente de la tabla.
	 * 
	 * @param actionEvent,
	 *            accion del evento.
	 */
	public void generateChart(ActionEvent actionEvent) {
		viewchart.setLabel(selectedParticipants, selectedEvents, filterTableLogs);
		viewchart.generarGrafica();
		engineChart.reload();
	}

	/**
	 * Vuelve a la ventana de login de usuario
	 * 
	 * @param actionEvent,
	 *            acción del evento.
	 * @throws Exception
	 *             excepción
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
	 * @param actionEvent,
	 *            acción del evento.
	 * @throws Exception
	 *             excepción
	 */
	public void clearSelection(ActionEvent actionEvent) throws Exception {
		if (!logs.getLogs().isEmpty()) {
			listParticipants.getSelectionModel().clearSelection();
			listEvents.getSelectionModel().clearSelection();
			filterLogs.clear();
			viewchart.getDate().clear();
			viewchart.getLabel().clear();

			loadHTML(logs.getLogs());
		}

	}

	/**
	 * Abre en el navegador el repositorio del proyecto.
	 * 
	 * @param actionEvent,
	 *            acción del evento.
	 * @throws Exception
	 *             excepción
	 */
	public void aboutUBULog(ActionEvent actionEvent) throws Exception {
		// TODO no parece funcionar
		Desktop.getDesktop().browse(new URL("https://github.com/trona85/GII-17.1B-UBULog-1.0").toURI());
	}

	/**
	 * Boton para cargar documento
	 * 
	 * @param actionEvent,
	 *            acción del evento.
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
			Alert alert = modalOpen();

			logs.setFile(file.toString());
			logs.readDocument();

			initializeDataSet(logs);
			alert.close();

		} catch (UBULogException e) {
			logger.info(e.getMessage());
			if (e.getError() != UBULogError.FICHERO_CANCELADO) {
				cargaDocumento(actionEvent);

			}

		}

	}

	/**
	 * Método que carga un modal.
	 * 
	 * @return alert.
	 */
	private Alert modalOpen() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeight(300);
		alert.setWidth(300);
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(UBULog.stage);

		alert.getDialogPane().setContentText("Se esta cargando el registro de la asignatura:\n"
				+ UBULog.session.getActualCourse().getFullName() + "\nPuede tardar unos minutos");
		alert.show();
		return alert;
	}

	/**
	 * Boton para cargar documento online.
	 * 
	 * @param actionEvent,
	 *            acción del evento.
	 * @throws IOException
	 *             excepción
	 */
	public void cargaDocumentoOnline(ActionEvent actionEvent) throws IOException {

		this.logs = new CsvParser();
		WebScripting webScripting = null;
		FileWriter fileWriter = null;
		PrintWriter pw = null;
		File file = null;
		try {

			Alert alert = modalOpen();
			webScripting = new WebScripting();
			webScripting.getResponsiveWeb();

			try {
				fileWriter = new FileWriter("./tempcsv.csv");

				pw = new PrintWriter(fileWriter);
				pw.print(webScripting.getResponsive());

			} finally {
				if (pw != null) {
					pw.close();

				}
				if (fileWriter != null)
					fileWriter.close();
			}

			file = new File("tempcsv.csv");
			logs.setFile(file.getAbsolutePath());
			logs.readDocument();

			initializeDataSet(logs);

			file.delete();

			alert.close();

		} catch (FailingHttpStatusCodeException e) {
			logger.error(e.getMessage());
		} catch (UBULogException e) {
			logger.info(e.getMessage());

		} finally {
			webScripting.close();
		}

	}

	/**
	 * Inicializamos los datos necesarios.
	 * 
	 * @param logs,
	 *            logs.
	 */
	private void initializeDataSet(CsvParser logs) {

		setDisableComponentInterfaz(false);

		listParticipants.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		listEvents.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		/// Mostramos la lista de participantes y eventos
		eventList = FXCollections.observableArrayList(logs.getEvents().values());
		Collections.sort(eventList, (o1, o2) -> o1.getNameEvent().compareTo(o2.getNameEvent()));
		listEvents.setItems(eventList);

		loadHTML(logs.getLogs());

	}

	/**
	 * Método para desactivar o activar botones de la interfaz.
	 * 
	 * @param disable,
	 *            booleano.
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

	/**
	 * 
	 * Botón "Salir". Cierra la aplicación.
	 * 
	 * @param actionEvent,
	 *            acción del evento.
	 * @throws Exception
	 *             excepción
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