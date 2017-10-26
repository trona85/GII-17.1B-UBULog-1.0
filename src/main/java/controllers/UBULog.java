package controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.*;
import webservice.Session;

/**
 * Clase main. Inicializa la ventana de login
 * 
 * @author Oscar Fernández Armengol
 * @version 1.0
 *
 */
public class UBULog extends Application {
	public static String host = "";
	public static Stage stage;
	public static Stage init;
	public static Session session;
	public static MoodleUser user;

	static final Logger logger = LoggerFactory.getLogger(UBULog.class);

	@Override
	public void start(Stage primaryStage) {
		try {
			logger.info("[Bienvenido a UBULog]");
			FXMLLoader loader = new FXMLLoader(UBULog.class.getResource("/view/Login.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			init = primaryStage;
			init.setScene(scene);
			init.getIcons().add(new Image("/img/logo_min.png"));
			UBULog.init.setTitle("UBULog");
			init.show();
		} catch (Exception e) {
			logger.error("Error al iniciar UBULog");
			e.printStackTrace();
		}
	}

	// Main comando
	public static void main(String[] args) {
		launch(args);
	}
}
	/*public static void main(String[] args) {
		logger.info("[Bienvenido a UBULog]");
		session = new Session("profesor", "1Qwerty--");
		String [] field = {"nombre del usuario"};
		String [] var = {"profesor profesor"};
		try {
			session.setToken();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("user: " + session.getUserName() + ", token: "+ session.getToken());
		
		MoodleUser user = new MoodleUser();
		MoodleUserWS userPro = new MoodleUserWS();
		CourseWS cws = new CourseWS();
		try {
			// creo al usuario logeado con sus caracteristicas
			userPro.setMoodleUser(session.getToken(), session.getUserName(), user);
			//System.out.println(user.getId() + " "+ user.getEmail());
			// inserto sus cursos
			userPro.setCourses(session.getToken(), user);
			for (Course c : user.getCourses()) {
				CourseWS.setEnrolledUsers(session.getToken(), c);
				//System.out.println(c.toString());
				
			}
			
			//TODO meter los nuevos campos, mirar el rol para saber las asignaturas necesitaremos una lista para poder acceder a los usuarios mas rapidamente por su id
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO
		CsvParser p = new CsvParser("doc/docparser/logs_curso2_20171012-1005.csv");
		p.readDocument();
		
		//System.out.println(p.getLogs());
		p.filter(field, var);

		
		
	}*/

