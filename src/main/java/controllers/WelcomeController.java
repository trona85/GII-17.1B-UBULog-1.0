/**
 * 
 */
package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Course;

/**
 * Clase controlador de la pantalla de bienvenida en la que se muestran los
 * cursos del usuario logueado.
 * 
 * @author oscar Fernández Armengol
 * @author Claudia Martínez Herrero
 * 
 * @version 1.1
 *
 */
public class WelcomeController implements Initializable {

	@FXML
	private Label lblUser;
	@FXML
	private ListView<String> listCourses;
	private ObservableList<String> list;
	@FXML
	private Label lblNoSelect;

	static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);

	/**
	 * Función initialize. Muestra la lista de cursos del usuario introducido.
	 */
	public void initialize(URL location, ResourceBundle resources) {
		try {
			lblUser.setText(UBULog.getUser().getFullName());
			logger.info("Cargando cursos...");
			ArrayList<String> nameCourses = new ArrayList<>();
			for (int i = 0; i < UBULog.getUser().getCourses().size(); i++) {
				nameCourses.add(UBULog.getUser().getCourses().get(i).getFullName());
			}
			Collections.sort(nameCourses);

			list = FXCollections.observableArrayList(nameCourses);
		} catch (Exception e) {
			logger.error("Error en la carga de cursos");
		}
		listCourses.setItems(list);
	}

	/**
	 * Botón entrar, accede a la siguiente ventana
	 * 
	 */
	public void enterCourse() {
		try {
			UBULog.getInit().getScene().setCursor(Cursor.WAIT);

			// Guardamos en una variable el curso seleccionado por el usuario
			String selectedCourse = listCourses.getSelectionModel().getSelectedItem();
			UBULog.getSession().setActualCourse(Course.getCourseByString(selectedCourse));
			logger.info(" Curso seleccionado: " + UBULog.getSession().getActualCourse().getFullName());

			// Accedemos a la siguiente ventana:
			FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/view/MainNew.fxml"));

			UBULog.getStage().close();
			UBULog.setStage(new Stage());
			Parent root = loader.load();
			Scene scene = new Scene(root);
			UBULog.getStage().setScene(scene);
			UBULog.getStage().getIcons().add(new Image("/img/logo_min.png"));
			UBULog.getStage().setTitle("UBULog 1.3");
			UBULog.getStage().setResizable(true);
			UBULog.getStage().show();
			UBULog.getInit().getScene().setCursor(Cursor.DEFAULT);
			lblNoSelect.setText("");
			 logger.info("-- Entrando al curso");
		} catch (Exception e) {
			lblNoSelect.setText("Debe seleccionar un curso");
			logger.info("Debe seleccionar un curso");
		}
	}
}
