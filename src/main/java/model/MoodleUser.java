/**
 * 
 */
package model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Course;

/**
 * clase MoodleUser. para guardar el usuario logeado.
 * @author oscar Fernández Armengol
 * 
 * @version 1.0
 */
public class MoodleUser {
	private int id;
	private String userName;
	private String fullName;
	private String email;
	private String department;
	private Date firstAccess;
	private Date lastAccess;
	private String auth;
	private String suspended;
	private String confirmed;
	private String lang;
	private String theme;
	private String description;
	private String profileImageUrlSmall;
	private String profileImageUrl;
	private String tokenSession;
	private ArrayList<Course> courses;

	/**
	 * Constructor de MoodleUser
	 * 
	 * @param id
	 *            id del usuario logueado
	 * @param userName
	 *            nombre de usuario
	 * @param fullName
	 *            nombre completo
	 * @param eMail
	 *            correo electrónico
	 * @param department
	 * 
	 * @param firstAccess
	 *            fecha de primer acceso
	 * @param lastAccess
	 *            fecha de último acceso
	 * @param auth
	 * @param suspended
	 * @param confirme
	 * @param lang
	 *            idioma del usuario
	 * @param theme
	 *            plantilla
	 * @param description
	 *            descripción
	 * @param profileImageUrlSmall
	 *            imagen pequeña
	 * @param profileImageUrl
	 *            imagen normal
	 * @param tokenSession
	 *            token de la sesion del usuario
	 */
	public MoodleUser(int id, String userName, String fullName, String eMail, String department, Date firstAccess,
			Date lastAccess, String auth, String suspended, String confirme, String lang, String theme,
			String description, String profileImageUrlSmall, String profileImageUrl) {
		this.setId(id);
		this.setUserName(userName);
		this.setFullName(fullName);
		this.setEmail(eMail);
		this.setDepartment(department);
		this.setFirstAccess(firstAccess);
		this.setLastAccess(lastAccess);
		this.setAuth(auth);
		this.setSuspended(suspended);
		this.setConfirmed(confirme);
		this.setLang(lang);
		this.setTheme(theme);
		this.setDescription(description);
		this.setProfileImageUrlSmall(profileImageUrlSmall);
		this.setProfileImageUrl(profileImageUrl);
		this.courses = new ArrayList<Course>();
	}

	public MoodleUser() {
		// TODO generado para primer login revisar otra manera.
	}

	/**
	 * Devuelve el id del usuario
	 * 
	 * @return id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Modifica el id del usuario
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Devuelve el nombre de usuario
	 * 
	 * @return userName
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * Modifica el nombre del usuario
	 * 
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Devuelve el nombre completo del usuario
	 * 
	 * @return fullName
	 */
	public String getFullName() {
		return this.fullName;
	}

	/**
	 * Modifica el nombre completo del usuario
	 * 
	 * @param fullName
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Devuelve el email del usuario
	 * 
	 * @return email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Modifica el email del usuario
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Devuelve la fecha de primer acceso
	 * 
	 * @return firstAccess
	 */
	public Date getFirstAccess() {
		return this.firstAccess;
	}

	/**
	 * Modifica la fecha de primer acceso
	 * 
	 * @param firstAccess
	 */
	public void setFirstAccess(Date firstAccess) {
		this.firstAccess = firstAccess;
	}

	/**
	 * Devuelve la fecha de ultimo acceso
	 * 
	 * @return lastAccess
	 */
	public Date getLastAccess() {
		return this.lastAccess;
	}

	/**
	 * Modifica la fecha de ultimo acceso
	 * 
	 * @param lastAccess
	 */
	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getSuspended() {
		return suspended;
	}

	public void setSuspended(String suspended) {
		this.suspended = suspended;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(String confirmed) {
		this.confirmed = confirmed;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getTokenSession() {
		return tokenSession;
	}

	public void setTokenSession(String tokenSession) {
		this.tokenSession = tokenSession;
	}

	public String getProfileImageUrlSmall() {
		return profileImageUrlSmall;
	}

	public void setProfileImageUrlSmall(String profileImageUrlSmall) {
		this.profileImageUrlSmall = profileImageUrlSmall;
	}
	
	/**
	 * Devuelve la lista de cursos que en los que este matriculado el usuario
	 * 
	 * @return lista de cursos
	 */
	public List<Course> getCourses() {
		return this.courses;
	}

	/**
	 * Modifica la lista de cursos en los que este matriculado el usuario
	 * 
	 * @param courses
	 */
	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}
	
	public String toString(){
		return getFullName();
	}

}
