package controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.MoodleUser;
import webservice.Session;

/**
 * Clase main. Inicializa la ventana de login
 * 
 * @author Oscar Fernández Armengol
 * @author Claudia Martínez Herrero
 * 
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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			init = primaryStage;
			init.setScene(scene);
			init.getIcons().add(new Image("/img/logo_min.png"));
			UBULog.init.setTitle("UBULog");
			init.show();
		} catch (Exception e) {
			logger.error("Error al iniciar UBULog");
		}
	}

	// Main comando
	public static void main(String[] args) {
		launch(args);
	}
}