/**
 * 
 */
package controlador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import modelo.MoodleUser;
import parserdocument.CsvParser;
import webservice.Session;

/**
 * @author oscar
 *
 */
public class UBULog {

	public static String host = "http://localhost/moodle/";
	public static Session session;
	public static MoodleUser user;

	//static final Logger logger = LoggerFactory.getLogger(UBULog.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//logger.info("[Bienvenido a UBUGrades]");

		session = new Session("profesor", "1Qwerty--");
		try {
			session.setToken();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("user: " + session.getUserName() + ", token: "+ session.getToken());
		
		//TODO
		//CsvParser p = new CsvParser("doc/docparser/logs_curso2_20171012-1005.csv");
		//p.readDocument();

	}

}
