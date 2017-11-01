/**
 * 
 */
package controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.EnrolledUser;
import model.GradeReportLine;
import model.Group;
import model.Role;
import parserdocument.CsvParser;
import parserdocument.Log;
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

	@FXML // Vista en árbol de actividades
	public TreeView<GradeReportLine> tvwGradeReport;
	ArrayList<GradeReportLine> gradeReportList;

	@FXML // Entrada de filtro de actividades por patrón
	public TextField tfdItems;
	String patternCalifications = "";

	@FXML // Botón filtro por tipo de actividad
	public MenuButton slcType;
	MenuItem[] typeMenuItems;
	String filterType = "Todos";

	@FXML // Gráfico
	private LineChart<String, Number> lineChart;

	private ArrayList<EnrolledUser> users;

	/**
	 * Muestra los usuarios matriculados en el curso, así como las actividades
	 * de las que se compone.
	 */
	public void initialize(URL location, ResourceBundle resources) {

		try {
			logger.info(" Cargando curso '" + UBULog.session.getActualCourse().getFullName() + "'...");

			// Establecemos los usuarios matriculados
			CourseWS.setEnrolledUsers(UBULog.session.getToken(), UBULog.session.getActualCourse());
			// Establecemos calificador del curso
			CourseWS.setGradeReportLines(UBULog.session.getToken(),
					UBULog.session.getActualCourse().getEnrolledUsers().get(0).getId(),
					UBULog.session.getActualCourse());

			// Almacenamos todos los participantes en una lista
			users = (ArrayList<EnrolledUser>) UBULog.session.getActualCourse().getEnrolledUsers();
			// insertamos los usuarios ficticios.
			insertUserFicticios();

			ArrayList<EnrolledUser> nameUsers = new ArrayList<EnrolledUser>();

			//////////////////////////////////////////////////////////////////////////
			// Manejo de roles (MenuButton Rol):
			MenuItem mi;
			manejoRoles();

			//////////////////////////////////////////////////////////////////////////
			// Manejo de grupos (MenuButton Grupo):
			manejoGrupos();

			////////////////////////////////////////////////////////
			// Añadimos todos los participantes a la lista de visualización
			for (int j = 0; j < users.size(); j++) {
				nameUsers.add(users.get(j));
			}
			enrList = FXCollections.observableArrayList(nameUsers);

			//////////////////////////////////////////////////////////////////////////
			// Manejo de actividades (TreeView<GradeReportLine>):
			EventHandler<ActionEvent> actionActivity = selectNameActivity();
			// Cargamos una lista de los nombres de los grupos
			ArrayList<String> nameActivityList = UBULog.session.getActualCourse().getActivities();
			// Convertimos la lista a una lista de MenuItems para el MenuButton
			ArrayList<MenuItem> nameActivityItemsList = new ArrayList<MenuItem>();
			// En principio se van a mostrar todos los participantes en
			// cualquier grupo
			mi = (new MenuItem("Todos"));
			// A�adimos el manejador de eventos al primer MenuItem
			mi.setOnAction(actionActivity);
			nameActivityItemsList.add(mi);

			for (int i = 0; i < nameActivityList.size(); i++) {
				String nameActivity = nameActivityList.get(i);
				mi = (new MenuItem(nameActivity));
				// A�adimos el manejador de eventos a cada MenuItem
				mi.setOnAction(actionActivity);
				nameActivityItemsList.add(mi);
			}

			// Asignamos la lista de grupos al MenuButton "Grupo"
			slcType.getItems().addAll(nameActivityItemsList);
			slcType.setText("Todos");

			// Inicializamos el listener del textField de participantes
			tfdParticipants.setOnAction(inputParticipant());

			// Inicializamos el listener del textField del calificador
			tfdItems.setOnAction(inputCalification());

		} catch (Exception e) {
			logger.error("Error en la inicializaci�n. {}", e);
			e.printStackTrace();
		}

		// Activamos la selección múltiple en la lista de participantes
		listParticipants.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		// Asignamos el manejador de eventos de la lista
		// Al clickar en la lista, se recalcula el número de elementos
		// seleccionados
		listParticipants.setOnMouseClicked(new EventHandler<Event>() {
			// Manejador que llama a la función de mostrar gráfico
			@Override
			public void handle(Event event) { // (1er click en participantes)
				ObservableList<EnrolledUser> selectedParticipants = listParticipants.getSelectionModel()
						.getSelectedItems();
				ObservableList<TreeItem<GradeReportLine>> selectedGRL = tvwGradeReport.getSelectionModel()
						.getSelectedItems();
				// Al seleccionar un participante reiniciamos el gr�fico
				lineChart.getData().clear();

				// Recalculamos la tabla
				String htmlTitle = "<tr><th style='background:#066db3; border: 1.0 solid grey; color:white;'> Alumno </th>";
				String content = "";
				int countA = 0;
				// Por cada usuario seleccionado
				for (EnrolledUser actualUser : selectedParticipants) {
					// A�adimos el usuario a la tabla
					String htmlRow = "<th style='color:#066db3; background: white; border: 1.0 solid grey;'>"
							+ actualUser.getFullName() + " </th>";
					try {
						// Establecemos el calificador del curso con este
						// usuario
						CourseWS.setGradeReportLines(UBULog.session.getToken(), actualUser.getId(),
								UBULog.session.getActualCourse());
					} catch (Exception e) {
						logger.error("Error de conexión. {}", e);
						e.printStackTrace();
						errorDeConexion();
					}

					// Añadimos valores al gráfico
					XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
					series.setName(actualUser.getLastName() + ", " + actualUser.getFirstName());

					int countB = 1;
					// Por cada actividad seleccionada
					for (TreeItem<GradeReportLine> structTree : selectedGRL) {
						countA++;
						for (GradeReportLine actualLine : UBULog.session.getActualCourse().getGradeReportLines()) {

							// Buscamos la actividad en la estructura y
							// obtenemos
							// los valores necesarios
							if (structTree.getValue().getId() == actualLine.getId()) {
								String calculatedGrade = actualLine.getGrade();
								// logger.info(actualLine.getName() + ":" +
								// actualLine.getGrade());

								// Añadimos la actividad a la tabla
								if (countA == countB) {
									htmlTitle += "<th style='border: 1.0 solid grey'> " + actualLine.getName()
											+ " </th>";
									countB++;
								}
								// Si es numérico lo graficamos (calculamos
								// sobre 10) y lo mostramos en la tabla
								if (!Float.isNaN(CourseWS.getFloat(calculatedGrade))) {

									series.getData()
											.add(new XYChart.Data<String, Number>(actualLine.getName(),
													(CourseWS.getFloat(calculatedGrade)
															/ Float.valueOf(actualLine.getRangeMax())) * 10));

									htmlRow += "<td style='border: 1.0 solid grey'> "
											+ Math.round(CourseWS.getFloat(calculatedGrade) * 100.0) / 100.0
											+ "/<b style='color:#ab263c'>" + actualLine.getRangeMax() + "</b> </td>";
								} else { // Si no, sólo lo mostramos en la tabla
									htmlRow += "<td style='border: 1.0 solid grey'> " + calculatedGrade + " </td>";
								}
							}
						}
					}
					htmlTitle += "</tr>";
					// Mostramos el gráfico
					lineChart.getData().add(series);
					htmlRow += "</tr>";
					content += htmlRow;
				}
			}
		});

		/// Mostramos la lista de participantes
		listParticipants.setItems(enrList);

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
		// Establecemos la raiz en el TreeView
		tvwGradeReport.setRoot(root);
		tvwGradeReport.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		// Asignamos el manejador de eventos de la lista
		// Al clickar en la lista, se recalcula el número de elementos
		// seleccionados
		tvwGradeReport.setOnMouseClicked(new EventHandler<Event>() {
			// Manejador que llama a la función de mostrar gráfico
			@Override
			public void handle(Event event) { // (1� clik en el calificador)
				ObservableList<EnrolledUser> selectedParticipants = listParticipants.getSelectionModel()
						.getSelectedItems();
				ObservableList<TreeItem<GradeReportLine>> selectedGRL = tvwGradeReport.getSelectionModel()
						.getSelectedItems();
				// Se reinicia el gráfico por cada nuevo �tem seleccionado
				lineChart.getData().clear();
				String htmlTitle = "<tr><th style='background:#066db3; border: 1.0 solid grey; color:white;'> Alumno </th>";
				String content = "";
				int countA = 0;
				// Por cada usuario seleccionado
				for (EnrolledUser actualUser : selectedParticipants) {
					// Se añade el usuario a la tabla
					String htmlRow = "<th style='color:#066db3; background:white; border: 1.0 solid grey;'> "
							+ actualUser.getFullName() + " </th>";
					try {
						// Establecemos el calificador del curso con este
						// usuario
						CourseWS.setGradeReportLines(UBULog.session.getToken(), actualUser.getId(),
								UBULog.session.getActualCourse());
					} catch (Exception e) {
						logger.error("Error de conexi�n. {}", e);
						e.printStackTrace();
						errorDeConexion();
					}

					// A�adimos elementos al gráfico
					XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
					series.setName(actualUser.getLastName() + ", " + actualUser.getFirstName());
					int countB = 1;

					// Por cada ítem seleccionado
					for (TreeItem<GradeReportLine> structTree : selectedGRL) {
						countA++;
						for (GradeReportLine actualLine : UBULog.session.getActualCourse().getGradeReportLines()) {
							try {
								if (structTree.getValue().getId() == actualLine.getId()) {
									String calculatedGrade = actualLine.getGrade();
									// logger.info(actualLine.getName()+":
									// "+actualLine.getGrade());

									if (countA == countB) {
										// Añadimos la actividad a la tabla
										htmlTitle += "<th style='border: 1.0 solid grey'> " + actualLine.getName()
												+ " </th>";
										countB++;
									}
									// Si es numérico lo graficamos y lo
									// mostramos en la tabla
									if (!Float.isNaN(CourseWS.getFloat(calculatedGrade))) {
										series.getData()
												.add(new XYChart.Data<String, Number>(actualLine.getName(),
														(CourseWS.getFloat(calculatedGrade)
																/ Float.valueOf(actualLine.getRangeMax())) * 10));

										htmlRow += "<td style='border: 1.0 solid grey'> "
												+ Math.round(CourseWS.getFloat(calculatedGrade) * 100.0) / 100.0
												+ "/<b style='color:#ab263c'>" + actualLine.getRangeMax()
												+ "</b> </td>";
									} else { // Si no, sólo lo mostramos en la
												// tabla
										htmlRow += "<td style='border: 1.0 solid grey'> " + calculatedGrade + " </td>";
									}
								}
							} catch (Exception e) {
								logger.error("Error en la construcci�n del �rbol/tabla. {}", e);
							}
						}
					}
					htmlTitle += "</tr>";
					lineChart.getData().add(series);
					htmlRow += "</tr>";
					content += htmlRow;
				}
			}
		});

		// Mostramos número participantes
		lblCountParticipants
				.setText("Participantes: " + String.valueOf(UBULog.session.getActualCourse().getEnrolledUsersCount()));

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
		users.add(new EnrolledUser("Administrador", 2));

		users.add(new EnrolledUser("Invitado", 1));

		users.add(new EnrolledUser("Sistema", 0));
		users.add(new EnrolledUser("Desconocido", -1));
	}

	/**
	 * 
	 */
	private void manejoGrupos() {
		MenuItem mi;
		EventHandler<ActionEvent> actionGroup = selectGroup();
		// Cargamos una lista de los nombres de los grupos
		ArrayList<String> groupsList = UBULog.session.getActualCourse().getGroups();
		// Convertimos la lista a una lista de MenuItems para el MenuButton
		ArrayList<MenuItem> groupsItemsList = new ArrayList<MenuItem>();
		// En principio mostrarán todos los usuarios en cualquier grupo
		mi = (new MenuItem("Todos"));
		// A�adimos el manejador de eventos al primer MenuItem
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
	 * Manejador de eventos para el bot�n de filtro por roles. Devuelve un
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
	 * Manejador de eventos para el bot�n de filtro por grupos. Devuelve un
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
			// Cargamos la lista de los roles
			ArrayList<EnrolledUser> nameUsers = new ArrayList<EnrolledUser>();
			// Obtenemos los participantes que tienen el rol elegido
			for (int i = 0; i < users.size(); i++) {
				// Filtrado por rol:
				roleYes = false;
				ArrayList<Role> roles = users.get(i).getRoles();
				// Si no tiene rol
				if (roles.size() == 0 && filterRole.equals("Todos")) {
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
				if (groups.size() == 0 && filterGroup.equals("Todos")) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		listParticipants.setItems(enrList);
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
	 * Manejador de eventos para las actividades. Devuelve un manejador de
	 * eventos para cada item.
	 * 
	 * @return manejador de eventos para las actividades
	 */
	private EventHandler<ActionEvent> selectNameActivity() {
		return new EventHandler<ActionEvent>() {
			/**
			 * Recibe un evento (relacionado con un MenuItem) y responde en
			 * consecuencia. El usuario elige un menuItem y filtra la lista de
			 * participantes
			 */
			public void handle(ActionEvent event) {
				// Obtenemos el item que se ha seleccionado
				MenuItem mItem = (MenuItem) event.getSource();
				// Obtenemos el valor (rol) para filtrar la lista de
				// participantes
				filterType = mItem.getText();
				logger.info("-> Filtrando calificador por tipo: " + filterType);
				filterCalifications();
				slcType.setText(filterType);
			}
		};
	}

	/**
	 * Manejador de eventos para el textField de filtro de actividades.
	 * 
	 * @return manejador de eventos para el patrón de filtro de actividades
	 */
	public EventHandler<ActionEvent> inputCalification() {
		return new EventHandler<ActionEvent>() {
			/**
			 * Recibe un evento (relacionado con un TreeItem) y responde en
			 * consecuencia. El usuario elige un menuItem y filtra la lista de
			 * participantes
			 */
			public void handle(ActionEvent event) {
				patternCalifications = tfdItems.getText();
				logger.info("-> Filtrando calificador por nombre: " + patternCalifications);
				filterCalifications();
			}
		};
	}

	/**
	 * Filtra la lista de actividades del calificador según el tipo y el patrón
	 * introducidos.
	 */
	public void filterCalifications() {
		try {
			clearData();
			ArrayList<GradeReportLine> grcl = (ArrayList<GradeReportLine>) UBULog.session.getActualCourse()
					.getGradeReportLines();
			// Establecemos la raiz del Treeview
			TreeItem<GradeReportLine> root = new TreeItem<GradeReportLine>(grcl.get(0));
			MainController.setIcon(root);
			// Llamamos recursivamente para llenar el Treeview
			if (filterType.equals("Todos") && patternCalifications.equals("")) {
				// Sin filtro y sin patr�n
				for (int k = 0; k < grcl.get(0).getChildren().size(); k++) {
					TreeItem<GradeReportLine> item = new TreeItem<GradeReportLine>(grcl.get(0).getChildren().get(k));
					MainController.setIcon(item);
					root.getChildren().add(item);
					root.setExpanded(true);
					setTreeview(item, grcl.get(0).getChildren().get(k));
				}
			} else { // Con filtro
				for (int k = 0; k < grcl.get(0).getChildren().size(); k++) {
					TreeItem<GradeReportLine> item = new TreeItem<GradeReportLine>(grcl.get(0).getChildren().get(k));
					boolean activityYes = false;
					if (grcl.get(0).getChildren().get(k).getNameType().equals(filterType)
							|| filterType.equals("Todos")) {
						activityYes = true;
					}
					Pattern pattern = Pattern.compile(patternCalifications);
					// logger.info(grcl.get(0).getChildren().get(k).getName());
					Matcher match = pattern.matcher(grcl.get(0).getChildren().get(k).getName());
					boolean patternYes = false;
					if (patternCalifications.equals("") || match.find()) {
						patternYes = true;
					}
					if (activityYes && patternYes) {
						MainController.setIcon(item);
						root.getChildren().add(item);
					}
					root.setExpanded(true);
					setTreeviewFilter(root, item, grcl.get(0).getChildren().get(k));
				}
			}
			// Establecemos la raiz del treeview
			tvwGradeReport.setRoot(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
		listParticipants.setItems(enrList);
	}

	/**
	 * Crea un árbol filtrado en el que los hijos del root(raíz) son elementos
	 * de cualquier nivel que cumplen el filtro
	 * 
	 * @param root
	 * @param parent
	 * @param line
	 */
	public void setTreeviewFilter(TreeItem<GradeReportLine> root, TreeItem<GradeReportLine> parent,
			GradeReportLine line) {
		/*
		 * Obtiene los hijos de la linea pasada por parametro Los transforma en
		 * treeitems y los establece como hijos del elemento treeItem
		 * equivalente de line
		 */
		for (int j = 0; j < line.getChildren().size(); j++) {
			TreeItem<GradeReportLine> item = new TreeItem<GradeReportLine>(line.getChildren().get(j));
			boolean activityYes = false;
			if (line.getChildren().get(j).getNameType().equals(filterType) || filterType.equals("Todos")) {
				activityYes = true;
			}
			Pattern pattern = Pattern.compile(patternCalifications);
			Matcher match = pattern.matcher(line.getChildren().get(j).getName());
			boolean patternYes = false;
			if (patternCalifications.equals("") || match.find()) {
				patternYes = true;
			}
			if (activityYes && patternYes) {
				MainController.setIcon(item);
				root.getChildren().add(item);
			}

			parent.setExpanded(true);
			setTreeviewFilter(root, item, line.getChildren().get(j));
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
		fileChooser.setTitle("Guardar gr�fico");

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
	 * 
	 */
	public void saveTable(ActionEvent actionEvent) throws Exception {
		
		//TODO Arreglar para exportar el log resultante si se quiere.
		/*WritableImage image = webView.snapshot(new SnapshotParameters(), null);

		File file = new File("table.png");

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Guardar tabla");
		fileChooser.setInitialFileName("table");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(".png", "*.*"),
				new FileChooser.ExtensionFilter("*.jpg", "*.jpg"), new FileChooser.ExtensionFilter("*.png", "*.png"));
		try {
			file = fileChooser.showSaveDialog(UBULog.stage);
			logger.info(file.getAbsolutePath());
			if (file != null) {
				try {
					ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
				} catch (IOException ex) {
					logger.info(ex.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
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
		logger.info("Cerrando sesi�n de usuario");
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
		tvwGradeReport.getSelectionModel().clearSelection();
		clearData();
	}

	public void clearData() {
		lineChart.getData().clear();
	}

	/**
	 * Abre en el navegador el repositorio del proyecto.
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void aboutUBULog(ActionEvent actionEvent) throws Exception {
		Desktop.getDesktop().browse(new URL("https://github.com/claumartinezh/TFG_UBULog").toURI());
	}

	/**
	 * Boton para cargar documento
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void cargaDocumento(ActionEvent actionEvent) throws Exception {

		FileChooser fileChooser = new FileChooser();
		//// TODO comparar documentos
		//// System.err.println(getFile().contains(".csv")); saveTable puede
		//// ayidar al control errores
		File file = fileChooser.showOpenDialog(UBULog.stage);

		CsvParser logs = new CsvParser(file.toString());
		logs.readDocument();

		for (int i = 0; i < logs.getLogs().size(); ++i) {
			for (int j = 0; j < users.size(); j++) {
				if (logs.getLogs().get(i).getIdUser() == users.get(j).getId()) {
					logs.getLogs().get(i).setUser(users.get(j));
					break;
				}
			}
		}

		enrLog = FXCollections.observableArrayList(logs.getLogs());
		listLogs.setItems(enrLog);

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