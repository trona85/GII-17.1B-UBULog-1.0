/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controllers.UBULog;

/**
 * Clase curso. Tiene las propiedades de curso.
 * 
 * @author Oscar Fernández Armengol
 * @author Claudia Martínez Herrero
 * 
 * @version 1.1
 */
public class Course {
	
	private int id;
	private String shortName;
	private String fullName;
	private int enrolledUsersCount;
	private String idNumber;
	private String summary;
	private ArrayList<EnrolledUser> enrolledUsers;
	private Set<String> roles; 
	private Set<String> groups;
	private Set<String> typeActivities;

	static final Logger logger = LoggerFactory.getLogger(Course.class);

	/**
	 * Constructor de un curso a partir de contenido JSON. Establece los
	 * parámetros de un curso.
	 * 
	 * @param obj
	 *            objeto JSON con la información del curso
	 */
	public Course(JSONObject obj) {

		try {
			this.id = obj.getInt("id");
			this.shortName = obj.getString("shortname");
			this.fullName = obj.getString("fullname");
			this.enrolledUsersCount = obj.getInt("enrolledusercount");
			this.idNumber = obj.getString("idnumber");
			this.summary = obj.getString("summary");
			this.enrolledUsers = new ArrayList<>();
		} catch (JSONException e) {
			logger.error(e.getMessage());
		}

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
	 * @param id, id
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
	 * @param shortName, shortName.
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
	 * @param fullName, fullName.
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
	 * @param enrolledUserCount, enrolledUserCount.
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
	 * @param idNumber, idNumber.
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
	 * @param summary, summary.
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * Devuelve una lista de los usuarios matriculados en el curso.
	 * 
	 * @return lista de usuarios
	 */
	public List<EnrolledUser> getEnrolledUsers() {
		Collections.sort(this.enrolledUsers, (o1, o2) -> o1.getLastName().compareTo(o2.getLastName()));
		return this.enrolledUsers;
	}

	/**
	 * Modifica la lista de usuarios matriculados en el curso
	 * 
	 * @param eUsers, eUsers.
	 */
	public void setEnrolledUsers(List<EnrolledUser> eUsers) {
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
	public List<String> getRoles() {
		ArrayList<String> result = new ArrayList<>();
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
	public void setRoles(List<EnrolledUser> users) {
		// Creamos el set de roles
		roles = new HashSet<>();
		// Recorremos la lista de usuarios matriculados en el curso
		for (int i = 0; i < users.size(); i++) {
			// sacamos el rol del usuario
			List<Role> roleArray = users.get(i).getRoles();
			// cada rol nuevo se añade al set roles
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
	public List<String> getGroups() {
		ArrayList<String> result = new ArrayList<>();
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
	 * usuarios que están matriculados.
	 * 
	 * @param users
	 *            usuarios del curso
	 */
	public void setGroups(List<EnrolledUser> users) {
		// Creamos el set de grupos
		groups = new HashSet<>();
		// Recorremos la lista de usuarios matriculados en el curso
		for (int i = 0; i < users.size(); i++) {
			// Sacamos el grupo del usuario
			List<Group> groupsArray = users.get(i).getGroups();
			// Cada grupo nuevo se añade al set de grupos
			for (int j = 0; j < groupsArray.size(); j++) {
				groups.add(groupsArray.get(j).getName());
			}
		}
	}

	/**
	 * Devuelve las actividades que hay en el curso.
	 * 
	 * @return lista de actividades
	 */
	public List<String> getActivities() {
		ArrayList<String> result = new ArrayList<>();
		Iterator<String> grclIt = this.typeActivities.iterator();
		while (grclIt.hasNext()) {
			String data = grclIt.next();
			if (data != null && !data.trim().equals(""))
				result.add(data);
		}
		return result;
	}

	/**
	 * Devuelve el id de un curso a partir de su nombre
	 * 
	 * @param courseName, courseName
	 * @return course
	 */
	public static Course getCourseByString(String courseName) {
		Course course = null;

		List<Course> courses = UBULog.getUser().getCourses();

		for (int i = 0; i < courses.size(); i++) {
			if (courses.get(i).getFullName().equals(courseName)) {
				course = courses.get(i);
			}
		}

		return course;
	}

	/**
	 * Método que imprime el nombre completo del curso.
	 */
	public String toString() {
		return getFullName();
	}

}
