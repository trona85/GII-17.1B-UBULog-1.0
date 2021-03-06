/**
 * 
 */
package webservice;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controllers.UBULog;
import model.Course;
import ubulogexception.UBULogError;
import ubulogexception.UBULogException;

/**
 *  Clase sesión. Obtiene el token de usuario y guarda sus parámetros. Establece la sesión.
 *  
 * @author Oscar Fernández Armengol
 *  @author Claudia Martínez Herrero
 * 
 * @version 1.1
 */
public class Session {
	
	static final Logger logger = LoggerFactory.getLogger(Session.class);

	/**
	 * Nombre usuario.
	 */
	private String userName;
	/**
	 * Password.
	 */
	private String password;
	/**
	 * Token
	 */
	private String tokenUser;
	/**
	 * Curso actual.
	 */
	private Course actualCourse;

	/**
	 * Constructor de la clase Session
	 * 
	 * @param userName
	 *            correo del usuario
	 * @param pass
	 *            contraseña de usuario
	 */
	public Session(String userName, String pass) {
		this.userName = userName;
		this.password = pass;
	}

	/**
	 * Obtiene el token de usuario
	 * 
	 * @return tokenUser
	 */
	public String getToken() {
		return this.tokenUser;
	}

	/**
	 * Establece el token del usuario a partir de usuario y contraseña. Se
	 * realiza mediante una petición http al webservice de Moodle
	 * 
	 * @throws Exception excepción
	 * @throws UBULogException usuario o contraseña no existente.
	 */
	public void setToken() throws Exception, UBULogException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(UBULog.getHost() + "/login/token.php?username=" + this.userName + "&password="
					+ this.password + "&service=" + WebServiceOptions.SERVICIO_WEB_MOODLE);
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				String respuesta = EntityUtils.toString(response.getEntity());
				JSONObject jsonObject = new JSONObject(respuesta);
				if (jsonObject.has("token")) {
					
					this.tokenUser = jsonObject.getString("token");
				}else{
					throw new UBULogException(UBULogError.USER_PASS_INCORRECTO);
				}
			}finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}

	/**
	 * Devuelve el email del usuario
	 * 
	 * @return email
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * Modifica el email del usuario
	 * 
	 * @param userName, userName.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Devuelve el curso actual
	 * 
	 * @return actualCourse
	 */
	public Course getActualCourse() {
		return this.actualCourse;
	}

	/**
	 * Modifica el curso actual
	 * 
	 * @param course, course.
	 */
	public void setActualCourse(Course course) {
		this.actualCourse = course;
	}
	
	/**
	 * Método que devuelve password.
	 * @return password
	 */
	public String getPassword(){
		return this.password;
	}
}
