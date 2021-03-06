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
 * @version 1.1
 *
 */
public class UBULog extends Application {
	private static String host = "";
	private static Stage stage;
	private static Stage initialize;
	private static Session session;
	private static MoodleUser user;

	static final Logger logger = LoggerFactory.getLogger(UBULog.class);

	@Override 
	public void start(Stage primaryStage) {
		try {
			logger.info("[Bienvenido a UBULog 1.4]");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			setInit(primaryStage);
			initialize.setScene(scene);
			initialize.getIcons().add(new Image("/img/logo_min.png"));
			UBULog.initialize.setTitle("UBULog 1.4");
			initialize.show();
		} catch (Exception e) {
			logger.error("Error al iniciar UBULog 1.4");
		}
	}
	
	

	// Main comando
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Recoge host.
	 * @return host
	 */
	public static String getHost() {
		return host;
	}
	
	/**
	 * Guarda host.
	 * @param host, host.
	 */
	public static void setHost(String host) {
		UBULog.host = host;
	}

	/**
	 * Recoge stage.
	 * @return stage
	 */
	public static Stage getStage() {
		return stage;
	}

	/**
	 * Guarda stage.
	 * @param stage, stage.
	 */
	public static void setStage(Stage stage) {
		UBULog.stage = stage;
	}
	
	/**
	 * Recoge init.
	 * @return init
	 */
	public static Stage getInit() {
		return initialize;
	}

	/**
	 * Guarda init.
	 * @param init, init.
	 */
	public static void setInit(Stage init) {
		UBULog.initialize = init;
	}
	
	/**
	 * Recoge session
	 * @return session
	 */
	public static Session getSession() {
		return session;
	}

	/**
	 * Guarda session.
	 * @param session, session.
	 */
	public static void setSession(Session session) {
		UBULog.session = session;
	}

	/**
	 * Recoge user
	 * @return user
	 */
	public static MoodleUser getUser() {
		return user;
	}

/**
 * Guarda el usuario.
 * @param user, user.
 * @return user
 */
	public static MoodleUser setUser(MoodleUser user) {
		UBULog.user = user;
		return user;
	}


}