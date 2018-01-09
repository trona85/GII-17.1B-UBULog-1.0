package controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
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
import ubulogexception.UBULogException;
import webservice.MoodleUserWS;
import webservice.Session;

/**
 * clase LoginController. Inicio de sesión.
 * @author oscar Fernández Armengol
 * @author Claudia Martínez Herrero
 * 
 * @version 1.2
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
	 */
	public void login() {

		if(txtHost.getText().isEmpty()){
			UBULog.setHost("https://ubuvirtual.ubu.es/");
		}else{
			UBULog.setHost(txtHost.getText());
		}
		
		UBULog.setSession(new Session(txtUsername.getText(), txtPassword.getText()));

		Boolean correcto = true;
		progressBar.visibleProperty().set(false);

		try {
			UBULog.getSession().setToken();
			
		} catch (UBULogException e) {
			correcto = false;
			logger.error("No se ha podido establecer el token d usuario. {}", e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		if (correcto) {
			logger.info("el token es: " + UBULog.getSession().getToken());
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
						
						try {
							
							FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Welcome.fxml"));
							
							UBULog.setStage(new Stage());

							Parent root = loader.load();
							Scene scene = new Scene(root);
							UBULog.getStage().setScene(scene);
							UBULog.getStage().getIcons().add(new Image("/img/logo_min.png"));
							UBULog.getStage().setTitle("UBULog 1.3");
							UBULog.getInit().close();
							UBULog.getStage().show();
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
			lblStatus.setText(" Usuario o contraseña incorrecto");
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
				MoodleUserWS.setMoodleUser(UBULog.getSession().getToken(), UBULog.getSession().getUserName(),
						UBULog.setUser(new MoodleUser()));
				updateProgress(1, 3);
				MoodleUserWS.setCourses(UBULog.getSession().getToken(), UBULog.getUser());
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
	 */
	public void clear(){
		txtUsername.setText("");
		txtPassword.setText("");
		txtHost.setText("");
	}
}