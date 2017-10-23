/**
 * 
 */
package controlador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import modelo.Course;
import modelo.MoodleUser;
import webservice.CourseWS;
import webservice.MoodleUserWS;
import webservice.Session;

/**
 * Clase main.
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public class UBULog {

	public static String host = "http://localhost/moodle/";
	public static Session session;
	public static MoodleUser user;

	static final Logger logger = LoggerFactory.getLogger(UBULog.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("[Bienvenido a UBUGrades]");

		session = new Session("profesor", "1Qwerty--");
		try {
			session.setToken();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("user: " + session.getUserName() + ", token: "+ session.getToken());
		
		MoodleUser user = new MoodleUser();
		MoodleUserWS userPro = new MoodleUserWS();
		CourseWS cws = new CourseWS();
		try {
			userPro.setMoodleUser(session.getToken(), session.getUserName(), user);
			System.out.println(user.getId() + " "+ user.getEmail());
			userPro.setCourses(session.getToken(), user);
			for (Course c : user.getCourses()) {
				CourseWS.setEnrolledUsers(session.getToken(), c);
				System.out.println(c);
				
			}
			
			//TODO meter los nuevos campos, mirar el rol para saber las asignaturas necesitaremos una lista para poder acceder a los usuarios mas rapidamente por su id
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO
		//CsvParser p = new CsvParser("doc/docparser/logs_curso2_20171012-1005.csv");
		//p.readDocument();

	}

}
