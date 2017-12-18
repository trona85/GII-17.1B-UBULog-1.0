package controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

/**
 * clase LoginController. Inicio de sesión.
 * @author oscar Fernández Armengol
 * @author Claudia Martínez Herrero
 * 
 * @version 1.0
 */
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
	 * Hace el login de usuario al pulsar el botón Entrar. Si el usuario es
	 * incorrecto, muestra un mensaje de error.
	 * 
	 * @param event, evento.
	 * @throws Exception, excepcion.
	 */
	public void login(ActionEvent event) {

		if(txtHost.getText().toString().isEmpty()){
			UBULog.host = "https://ubuvirtual.ubu.es/";;
		}else{
			UBULog.host = txtHost.getText();
		}
		System.out.println(UBULog.host);
		
		UBULog.session = new Session(txtUsername.getText(), txtPassword.getText());

		Boolean correcto = true;
		progressBar.visibleProperty().set(false);

		try { // Establecemos el token
			UBULog.session.setToken();
			
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
							logger.error("Error en la carga de Welcome. {}", e);
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
				MoodleUserWS.setMoodleUser(UBULog.session.getToken(), UBULog.session.getUserName(),
						UBULog.user = new MoodleUser());
				updateProgress(1, 3);
				MoodleUserWS.setCourses(UBULog.session.getToken(), UBULog.user);
				updateProgress(2, 3);
				updateProgress(3, 3);
				Thread.sleep(50);
				updateMessage("end");
				return true;
			}
		};
	}

	/**
	 * Borra los parámetros introducidos en los campos
	 * 
	 * @param event, evento
	 * @throws Exception, excepción
	 */
	public void clear(ActionEvent event) throws Exception {
		txtUsername.setText("");
		txtPassword.setText("");
		txtHost.setText("");
	}
}
