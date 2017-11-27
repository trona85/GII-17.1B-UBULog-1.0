/**
 * 
 */
package webservice;

import java.util.ArrayList;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import controllers.UBULog;
import model.Course;
import model.EnrolledUser;

/**
 * Clase Course para webservices. Recoge funciones útiles para servicios web relacionados con un curso.
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public class CourseWS {
	/**
	 * Establece los usuarios que están matriculados en un curso junto con su
	 * rol y grupo.
	 * 
	 * @param token
	 *            token de usuario
	 * @param idCurso
	 *            id del curso
	 * @throws Exception
	 */
	public static void setEnrolledUsers(String token, Course course) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		ArrayList<EnrolledUser> eUsers = new ArrayList<EnrolledUser>();
		try {
			//TODO http://localhost/moodle//webservice/rest/server.php?wstoken=9a5e85d1e61c1c42509d77b34f26643a&moodlewsrestformat=json&wsfunction=core_enrol_get_enrolled_users&&courseid=3
			HttpGet httpget = new HttpGet(UBULog.host + "/webservice/rest/server.php?wstoken=" + token
					+ "&moodlewsrestformat=json&wsfunction=" + WebServiceOptions.OBTENER_USUARIOS_MATRICULADOS
					+ "&courseid=" + course.getId());
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				String respuesta = EntityUtils.toString(response.getEntity());
				JSONArray jsonArray = new JSONArray(respuesta);
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						if (jsonObject != null) {
							eUsers.add(new EnrolledUser(token, jsonObject));
						}
					}
				}
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		course.setEnrolledUsers(eUsers);
		course.setRoles(eUsers);
		course.setGroups(eUsers);
	}

}
