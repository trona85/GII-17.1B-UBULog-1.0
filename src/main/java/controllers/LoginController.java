package controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controllers.LoginController;
import controllers.UBULog;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.MoodleUser;
import webservice.MoodleUserWS;
import webservice.Session;

public class LoginController {

	static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@FXML
	private Label lblStatus;
	@FXML
	private TextField txtUsername;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private TextField txtHost;
	@FXML
	private Button btnLogin;
	@FXML
	private ProgressBar progressBar;
	
	/**
	 * Hace el login de usuario al pulsar el bot�n Entrar. Si el usuario es
	 * incorrecto, muestra un mensaje de error.
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void login(ActionEvent event) throws Exception {
		String nombre = "profesor";
		String pass = "1Qwerty--";

		// Almacenamos los par�metros introducidos por el usuario:
		UBULog.init.getScene().setCursor(Cursor.WAIT);
		UBULog.host = "http://localhost/moodle";
				//txtHost.getText(); //TODO cambiar para ubu virtual
		//UBULog.session = new Session(txtUsername.getText(), txtPassword.getText());
		UBULog.session = new Session(nombre, pass);
		Boolean correcto = true;
		progressBar.visibleProperty().set(false);

		try { // Establecemos el token
			UBULog.session.setToken();
			//System.err.println(UBULog.session.getToken());
			//System.err.println("aqi llego");
		} catch (Exception e) {
			correcto = false;
			logger.error("No se ha podido establecer el token d usuario. {}", e);
		}
		// Si el login es correcto
		if (correcto) {
			logger.info("el token es: " + UBULog.session.getToken().toString());
			logger.info(" Login Correcto");
			progressBar.setProgress(0.0);
			logger.info("progreso");
			Task<Object> task = createWorker();
			progressBar.progressProperty().unbind();
			progressBar.progressProperty().bind(task.progressProperty());
			progressBar.visibleProperty().set(true);
			task.messageProperty().addListener(new ChangeListener<String>() {
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (newValue.equals("end")) {
						// Load GUI
						try {
							// Accedemos a la siguiente ventana
							FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Welcome.fxml"));
							
							UBULog.stage = new Stage();

							Parent root = loader.load();
							Scene scene = new Scene(root);
							UBULog.stage.setScene(scene);
							UBULog.stage.getIcons().add(new Image("/img/logo_min.png"));
							UBULog.stage.setTitle("UBULog");
							UBULog.init.close();
							UBULog.stage.show();
							lblStatus.setText("");
						} catch (Exception e) {
							e.printStackTrace();
							throw new RuntimeException("Loading Welcome.fxml");
						}
					} else {
						logger.info(newValue);
					}
				}
			});
			Thread th = new Thread(task);
			th.start();
		} else {
			lblStatus.setText(" Usuario incorrecto");
			logger.info("Login Incorrecto");
			txtUsername.setText("");
			txtPassword.setText("");
		}
	}

	/**
	 * Realiza las tareas mientras carga la barra de progreso
	 * 
	 * @return tarea
	 */
	private Task<Object> createWorker() {
		return new Task<Object>() {
			@Override
			protected Object call() throws Exception {
				logger.info("entramos 1");
				MoodleUserWS.setMoodleUser(UBULog.session.getToken(), UBULog.session.getUserName(),
						UBULog.user = new MoodleUser());
				logger.info("entramos 2");
				updateProgress(1, 3);
				logger.info("entramos 3");
				MoodleUserWS.setCourses(UBULog.session.getToken(), UBULog.user);
				logger.info("entramos 4");
				updateProgress(2, 3);
				updateProgress(3, 3);
				logger.info("entramos 5");
				Thread.sleep(50);
				logger.info("entramos 6");
				updateMessage("end");
				return true;
			}
		};
	}

	/**
	 * Borra los par�metros introducidos en los campos
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void clear(ActionEvent event) throws Exception {
		txtUsername.setText("");
		txtPassword.setText("");
		txtHost.setText("");
	}
}
