/**
 * 
 */
package model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * clase MoodleUser. para guardar el usuario logeado.
 * @author oscar Fernández Armengol
 * @author Claudia Martínez Herrero
 * 
 * @version 1.1
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
	private List<Course> courses;

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
	 * @param department, department.
	 * 
	 * @param firstAccess
	 *            fecha de primer acceso
	 * @param lastAccess
	 *            fecha de último acceso
	 * @param auth, auth
	 * @param suspended, suspended.
 	 * @param confirme confirme.
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
		this.courses = new ArrayList<>();
	}

	/**
	 * Constuctor vacio.
	 */
	public MoodleUser() {
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
	 * @param id, id.
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
	 * @param userName, userName.
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
	 * @param fullName, fullName.
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
	 * @param email, email.
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
	 * @param firstAccess, firstAccess.
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
	 * @param lastAccess, lastAccess.
	 */
	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}

	/**
	 * Devuelve auth.
	 * 
	 * @return auth
	 */
	public String getAuth() {
		return auth;
	}

	/**
	 * Modifica auth.
	 * 
	 * @param auth, auth.
	 */
	public void setAuth(String auth) {
		this.auth = auth;
	}

	/**
	 * Devuelve suspended.
	 * 
	 * @return suspended
	 */
	public String getSuspended() {
		return suspended;
	}

	/**
	 * Modifica suspended.
	 * 
	 * @param suspended, suspended.
	 */
	public void setSuspended(String suspended) {
		this.suspended = suspended;
	}

	/**
	 * Devuelve department.
	 * 
	 * @return department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * Modifica department.
	 * 
	 * @param department, department.
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * Devuelve confirmed.
	 * 
	 * @return confirmed
	 */
	public String getConfirmed() {
		return confirmed;
	}

	/**
	 * Modifica confirmed.
	 * 
	 * @param confirmed, confirmed.
	 */
	public void setConfirmed(String confirmed) {
		this.confirmed = confirmed;
	}

	/**
	 * Devuelve lang.
	 * 
	 * @return lang
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * Modifica lang.
	 * 
	 * @param lang, lang.
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * Devuelve theme.
	 * 
	 * @return theme
	 */
	public String getTheme() {
		return theme;
	}

	/**
	 * Modifica theme.
	 * 
	 * @param theme, theme.
	 */
	public void setTheme(String theme) {
		this.theme = theme;
	}

	/**
	 * Devuelve description.
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Modifica description.
	 * 
	 * @param description, description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Devuelve profileImageUrl.
	 * 
	 * @return profileImageUrl
	 */
	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	/**
	 * Modifica profileImageUrl.
	 * 
	 * @param profileImageUrl, profileImageUrl.
	 */
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	/**
	 * Devuelve tokenSession.
	 * 
	 * @return tokenSession
	 */
	public String getTokenSession() {
		return tokenSession;
	}

	/**
	 * Modifica tokenSession.
	 * 
	 * @param tokenSession, tokenSession.
	 */
	public void setTokenSession(String tokenSession) {
		this.tokenSession = tokenSession;
	}

	/**
	 * Devuelve profileImageUrlSmall.
	 * 
	 * @return profileImageUrlSmall
	 */
	public String getProfileImageUrlSmall() {
		return profileImageUrlSmall;
	}

	/**
	 * Modifica profileImageUrlSmall.
	 * 
	 * @param profileImageUrlSmall, profileImageUrlSmall.
	 */
	public void setProfileImageUrlSmall(String profileImageUrlSmall) {
		this.profileImageUrlSmall = profileImageUrlSmall;
	}
	
	/**
	 * Devuelve la lista de cursos que en los que este matriculado el usuario
	 * 
	 * @return courses
	 */
	public List<Course> getCourses() {
		return this.courses;
	}

	/**
	 * Modifica la lista de cursos en los que este matriculado el usuario
	 * 
	 * @param courses, courses
	 */
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	/**
	 * Método para imprimir datos.
	 */
	public String toString(){
		return getFullName();
	}

}
