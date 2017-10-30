/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controllers.UBULog;

/**
 * Clase curso. Tiene las propiedades de curso.
 * 
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public class Course {
	// TODO
	// http://localhost/moodle//webservice/rest/server.php?wstoken=9a5e85d1e61c1c42509d77b34f26643a&moodlewsrestformat=json&wsfunction=core_enrol_get_users_courses&userid=6

	private int id;
	private String shortName;
	private String fullName;
	private int enrolledUsersCount;
	private String idNumber;
	private String summary;
	private String lang;
	// private Date startDate;
	// private Date endDate;
	public ArrayList<EnrolledUser> enrolledUsers;
	public Set<String> roles; // roles que hay en el curso
	public Set<String> groups; // grupos que hay en el curso
	public ArrayList<GradeReportLine> gradeReportLines;
	public Set<String> typeActivities;

	static final Logger logger = LoggerFactory.getLogger(Course.class);

	/**
	 * Constructor de un curso a partir de contenido JSON. Establece los
	 * parámetros de un curso.
	 * 
	 * @param obj
	 *            objeto JSON con la información del curso
	 * @throws Exception
	 */
	public Course(JSONObject obj) throws Exception {

		this.id = obj.getInt("id");
		this.shortName = obj.getString("shortname");
		this.fullName = obj.getString("fullname");
		this.enrolledUsersCount = obj.getInt("enrolledusercount");
		this.idNumber = obj.getString("idnumber");
		this.summary = obj.getString("summary");
		this.lang = obj.getString("lang");
		this.enrolledUsers = new ArrayList<EnrolledUser>();

	}

	/**
	 * Devuelve el id del curso.
	 * 
	 * @return id del curso
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Modifica el id del curso
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Devuelve el nombre corto del curso
	 * 
	 * @return shortName
	 */
	public String getShortName() {
		return this.shortName;
	}

	/**
	 * Modifica el nombre corto del curso
	 * 
	 * @param shortName
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * Devuelve el nombre del curso
	 * 
	 * @return fullName
	 */
	public String getFullName() {
		return this.fullName;
	}

	/**
	 * Modifica el nombre del curso
	 * 
	 * @param fullName
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Devuelve el no de usuarios del curso
	 * 
	 * @return enrolledUsersCount
	 */
	public int getEnrolledUsersCount() {
		return this.enrolledUsersCount;
	}

	/**
	 * Modifica el no de usuarios del curso
	 * 
	 * @param enrolledUserCount
	 */
	public void setEnrolledUsersCount(int enrolledUserCount) {
		this.enrolledUsersCount = enrolledUserCount;
	}

	/**
	 * Devuelve el idNumber del curso
	 * 
	 * @return idNumber
	 */
	public String getIdNumber() {
		return this.idNumber;
	}

	/**
	 * Modifica el idNumber del curso
	 * 
	 * @param idNumber
	 */
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	/**
	 * Devuelve el resumen del curso
	 * 
	 * @return summary
	 */
	public String getSummary() {
		return this.summary;
	}

	/**
	 * Modifica el resumen del curso
	 * 
	 * @param summary
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * Devuelve una lista de los usuarios matriculados en el curso.
	 * 
	 * @return lista de usuarios
	 */
	public ArrayList<EnrolledUser> getEnrolledUsers() {
		Collections.sort(this.enrolledUsers, (o1, o2) -> o1.getLastName().compareTo(o2.getLastName()));
		return this.enrolledUsers;
	}

	/**
	 * Modifica la lista de usuarios matriculados en el curso
	 * 
	 * @param eUsers
	 */
	public void setEnrolledUsers(ArrayList<EnrolledUser> eUsers) {
		this.enrolledUsers.clear();
		for (EnrolledUser eUser : eUsers) {
			this.enrolledUsers.add(eUser);
		}
	}

	/**
	 * Devuelve los roles que hay en el curso.
	 * 
	 * @return lista de roles del curso
	 */
	public ArrayList<String> getRoles() {
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> roleIt = this.roles.iterator();
		while (roleIt.hasNext()) {
			String data = roleIt.next();
			if (data != null && !data.trim().equals(""))
				result.add(data);
		}
		return result;
	}

	/**
	 * Almacena en un set los roles que hay en el curso.
	 * 
	 * @param users
	 *            usuarios matriculados en el curso
	 */
	public void setRoles(ArrayList<EnrolledUser> users) {
		// Creamos el set de roles
		roles = new HashSet<String>();
		// Recorremos la lista de usuarios matriculados en el curso
		for (int i = 0; i < users.size(); i++) {
			// sacamos el rol del usuario
			ArrayList<Role> roleArray = users.get(i).getRoles();
			// cada rol nuevo se a�ade al set roles
			for (int j = 0; j < roleArray.size(); j++) {
				roles.add(roleArray.get(j).getName());
			}
		}
	}

	/**
	 * Devuelve los grupos que hay en el curso.
	 * 
	 * @return lista de grupos del curso
	 */
	public ArrayList<String> getGroups() {
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> groupsIt = this.groups.iterator();
		while (groupsIt.hasNext()) {
			String data = groupsIt.next();
			if (data != null && !data.trim().equals(""))
				result.add(data);
		}
		return result;
	}

	/**
	 * Almacena en una lista los grupos que hay en un curso, a partir de los
	 * usuarios que est�n matriculados.
	 * 
	 * @param users
	 *            usuarios del curso
	 */
	public void setGroups(ArrayList<EnrolledUser> users) {
		// Creamos el set de grupos
		groups = new HashSet<String>();
		// Recorremos la lista de usuarios matriculados en el curso
		for (int i = 0; i < users.size(); i++) {
			// Sacamos el grupo del usuario
			ArrayList<Group> groupsArray = users.get(i).getGroups();
			// Cada grupo nuevo se a�ade al set de grupos
			for (int j = 0; j < groupsArray.size(); j++) {
				groups.add(groupsArray.get(j).getName());
			}
		}
	}

	/**
	 * Devuelve la lista de gradeReportLines que hay en el curso. (El
	 * calificador)
	 * 
	 * @return lista de gradeReportConfigurationLines
	 */
	public ArrayList<GradeReportLine> getGradeReportLines() {
		return this.gradeReportLines;
	}

	/**
	 * Establece la lista de gradeReportLines del curso (el calificador)
	 * 
	 * @param grcl
	 */
	public void setGradeReportLines(ArrayList<GradeReportLine> grcl) {
		this.gradeReportLines.clear();
		for (GradeReportLine gl : grcl) {
			this.gradeReportLines.add(gl);
		}
	}

	/**
	 * Devuelve las actividades que hay en el curso.
	 * 
	 * @return lista de actividades
	 */
	public ArrayList<String> getActivities() {
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> grclIt = this.typeActivities.iterator();
		while (grclIt.hasNext()) {
			String data = grclIt.next();
			if (data != null && !data.trim().equals(""))
				result.add(data);
		}
		return result;
	}

	/**
	 * Almacena en un set las actividades que hay en el curso.
	 * 
	 * @param grcl
	 *            gradeReportConfigurationLines
	 */
	public void setActivities(ArrayList<GradeReportLine> grcl) {
		// Creamos el set de roles
		typeActivities = new HashSet<String>();
		// Recorremos la lista de usuarios matriculados en el curso
		for (int i = 0; i < grcl.size(); i++) {
			typeActivities.add(grcl.get(i).getNameType());
		}
	}

	/**
	 * Sustituimos el elemento de la lista que es una cabecera por el elemento
	 * que es una categoria completa con todos sus atributos
	 * 
	 * @param line
	 */
	public void updateGRLList(GradeReportLine line) {
		for (int i = 0; i < this.gradeReportLines.size(); i++) {
			if (this.gradeReportLines.get(i).getId() == line.getId()) {
				this.gradeReportLines.set(i, line);
			}
		}
	}

	/**
	 * Devuelve el id de un curso a partir de su nombre
	 * 
	 * @param courseName
	 * @return
	 */
	public static Course getCourseByString(String courseName) {
		Course course = null;

		ArrayList<Course> courses = (ArrayList<Course>) UBULog.user.getCourses();
		// logger.info(" N� de cursos: " + courses.size());
		for (int i = 0; i < courses.size(); i++) {
			if (courses.get(i).getFullName().equals(courseName)) {
				course = courses.get(i);
			}
		}

		return course;
	}

	// TODO algo no funciona bien
	public String toString() {
		return getFullName();
				/*"id: " + getId() 
								 * + "\n" + "shortName: " + getShortName() +
								 * "\n" + "fullName: " + getFullName() + "\n" +
								 * "enrolledUsersCount: " +
								 * getEnrolledUsersCount() + "\n" + "idNumber: "
								 * + getIdNumber() + "\n" + "summary: " +
								 * getSummary() + "\n" + "lang: " + getLang() +
								 * "\n" + "enrolledUsers: " + getEnrolledUsers()
								 * + "\n" + "roles: " + getRoles() + "\n" +
								 * "groups: " + getGroups() + "\n" +
								 * "typeActivities: " + getActivities() + "\n"
								 */
	}

}
