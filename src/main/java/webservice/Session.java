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

import controlador.UBULog;
import modelo.Course;

/**
 *  Clase sesión. Obtiene el token de usuario y guarda sus parámetros. Establece la sesión.
 *  
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public class Session {

	private String userName;
	private String password;
	private String tokenUser;
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
	 * @return
	 */
	public String getToken() {
		return this.tokenUser;
	}

	/**
	 * Establece el token del usuario a partir de usuario y contrase�a. Se
	 * realiza mediante una petici�n http al webservice de Moodle
	 * 
	 * @throws Exception
	 */
	public void setToken() throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(UBULog.host + "/login/token.php?username=" + this.userName + "&password="
					+ this.password + "&service=" + WebServiceOptions.SERVICIO_WEB_MOODLE);
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				String respuesta = EntityUtils.toString(response.getEntity());
				JSONObject jsonObject = new JSONObject(respuesta);
				if (jsonObject != null) {
					this.tokenUser = jsonObject.getString("token");
				}
			} finally {
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
	 * @param userName
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
	 * @param course
	 */
	public void setActualCourse(Course course) {
		this.actualCourse = course;
	}
}
