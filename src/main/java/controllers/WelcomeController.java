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

import controllers.UBULog;
import controllers.WelcomeController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Course;

/**
 * @author oscar
 *
 */
public class WelcomeController {

	@FXML
	private Label lblUser;
	@FXML
	private ListView<String> listCourses;
	private ObservableList<String> list;
	@FXML
	private Label lblNoSelect;

	static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);

	/**
	 * Funci�n initialize. Muestra la lista de cursos del usuario introducido.
	 */
	public void initialize(URL location, ResourceBundle resources) {
		try {
			lblUser.setText(UBULog.user.getFullName());
			logger.info("Cargando cursos...");
			ArrayList<String> nameCourses = new ArrayList<String>();
			for (int i = 0; i < UBULog.user.getCourses().size(); i++) {
				nameCourses.add(UBULog.user.getCourses().get(i).getFullName());
			}
			Collections.sort(nameCourses);

			list = FXCollections.observableArrayList(nameCourses);
		} catch (Exception e) {
			e.printStackTrace();
		}
		listCourses.setItems(list);
	}

	/**
	 * Bot�n entrar, accede a la siguiente ventana
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void enterCourse(ActionEvent event) throws Exception {
		try {
			UBULog.init.getScene().setCursor(Cursor.WAIT);

			// Guardamos en una variable el curso seleccionado por el usuario
			String selectedCourse = listCourses.getSelectionModel().getSelectedItem();
			UBULog.session.setActualCourse(Course.getCourseByString(selectedCourse));
			logger.info(" Curso seleccionado: " + UBULog.session.getActualCourse().getFullName());

			// Accedemos a la siguiente ventana:
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Main.fxml"));

			UBULog.stage.close();
			UBULog.stage = new Stage();
			Parent root = loader.load();
			Scene scene = new Scene(root);
			UBULog.stage.setScene(scene);
			UBULog.stage.getIcons().add(new Image("/img/logo_min.png"));
			UBULog.stage.setTitle("UBULog");
			UBULog.stage.setResizable(true);
			UBULog.stage.show();
			UBULog.init.getScene().setCursor(Cursor.DEFAULT);
			lblNoSelect.setText("");
			// logger.info("-- Entrando al curso");
		} catch (Exception e) {
			lblNoSelect.setText("Debe seleccionar un curso");
			logger.info("Debe seleccionar un curso");
		}
	}
}
