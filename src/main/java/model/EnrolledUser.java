/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Clase que representa un usuario matriculado en una asignatura.
 * 
 * @author Oscar Fernández Armengol
 * @author Claudia Martínez Herrero
 * 
 * @version 1.1
 */
public class EnrolledUser {
	/**
	 * Id usuario.
	 */
	private int id;
	/**
	 * Nombre.
	 */
	private String firstName;
	/**
	 * Apellido.
	 */
	private String lastName;
	/**
	 * Nombre completo.
	 */
	private String fullName;
	/**
	 * Primer acceso.
	 */
	private Date firstAccess;
	/**
	 * Ultimo acceso.
	 */
	private Date lastAccess;
	/**
	 * Descripción.
	 */
	private String description;
	/**
	 * Ciudad.
	 */
	private String city;
	/**
	 * País
	 */
	private String country;
	/**
	 * Imagen del usuario pequeña.
	 */
	private String profileImageUrlSmall;
	/**
	 * Imagen del usuario.
	 */
	private String profileImageUrl;
	/**
	 * Roles del usuario.
	 */
	private ArrayList<Role> roles;
	/**
	 * Grupos del usuario.
	 */
	private ArrayList<Group> groups;
	/**
	 * Cursos del usuario.
	 */
	private ArrayList<Integer> courses;

	/**
	 * Constructor de EnrolledUser
	 * 
	 * @param obj
	 *            objeto JSON con la información del usuario
	 * @throws Exception
	 *             excepción
	 */
	public EnrolledUser(JSONObject obj) throws Exception {
		this.id = obj.getInt("id");

		if (obj.getString("firstname") != null)
			this.firstName = obj.getString("firstname");
		if (obj.getString("lastname") != null)
			this.lastName = obj.getString("lastname");
		if (obj.getString("fullname") != null)
			this.fullName = obj.getString("fullname");
		if (new Date(obj.getLong("firstaccess")) != null)
			this.firstAccess = new Date(obj.getLong("firstaccess") * 1000);
		if (new Date(obj.getLong("lastaccess")) != null)
			this.lastAccess = new Date(obj.getLong("lastaccess") * 1000);
		if (obj.getString("profileimageurl") != null)
			this.profileImageUrl = obj.getString("profileimageurl");
		if (obj.getJSONArray("roles") != null) {
			JSONArray roleArray = obj.getJSONArray("roles");
			roles = new ArrayList<>();
			for (int i = 0; i < roleArray.length(); i++) {
				// Establece un rol con el id, name y shortname obtenido de cada
				// JSONObject del JSONArray
				Role rol = new Role(roleArray.getJSONObject(i).getInt("roleid"),
						roleArray.getJSONObject(i).getString("name"),
						roleArray.getJSONObject(i).getString("shortname"));

				roles.add(rol);
			}
		}
		if (obj.optJSONArray("groups") != null) {
			JSONArray groupArray = obj.getJSONArray("groups");
			groups = new ArrayList<>();
			for (int i = 0; i < groupArray.length(); i++) {
				// Establece un grupo con el id, name y description obtenido de
				// cada JSONObject del JSONArray
				Group group = new Group(groupArray.getJSONObject(i).getInt("id"),
						groupArray.getJSONObject(i).getString("name"),
						groupArray.getJSONObject(i).getString("description"));
				groups.add(group);
			}
		} else {
			groups = new ArrayList<>(); // to have an empty list, not a null
		}
		this.courses = new ArrayList<>();
	}

	/**
	 * Constructor para crear usuarios ficticios
	 * 
	 * @param nombreCompleto,
	 *            nombre completo.
	 * @param id,
	 *            id.
	 */
	public EnrolledUser(String nombreCompleto, int id) {
		setFullName(nombreCompleto);
		setId(id);
	}

	/**
	 * Devuelve el id del usuario
	 * 
	 * @return id de usuario matriculado
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Modifica el id del usuario
	 * 
	 * @param id,
	 *            id.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Devuelve el nombre del usuario
	 * 
	 * @return firstName
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Modifica el nombre del usuario
	 * 
	 * @param firstName,
	 *            firstName.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Devuelve el apellido del usuario
	 * 
	 * @return lastName
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Modifica el apellido del usuario
	 * 
	 * @param lastName,
	 *            lastName.
	 */
	public void setlastName(String lastName) {
		this.lastName = lastName;
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
	 * @param fullName,
	 *            fullName.
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Devuelve el primer acceso del usuario a la plataforma
	 * 
	 * @return firstAccess
	 */
	public Date getFirstAccess() {
		return this.firstAccess;
	}

	/**
	 * Modifica el primer acceso del usuario a la plataforma
	 * 
	 * @param firstAccess,
	 *            firstAccess.
	 */
	public void setFirstAccess(Date firstAccess) {
		this.firstAccess = firstAccess;
	}

	/**
	 * Devuelve la última fecha de acceso a la plataforma
	 * 
	 * @return lastAccess
	 */
	public Date getLastAccess() {
		return this.lastAccess;
	}

	/**
	 * Modifica la última fecha de acceso a la plataforma
	 * 
	 * @param lastAccess,
	 *            lastAccess.
	 */
	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}

	/**
	 * Devuelve la descripción del usuario
	 * 
	 * @return description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Modifica la descripción del usuario
	 * 
	 * @param description,
	 *            description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Devuelve la ciudad del usuario
	 * 
	 * @return city
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * Modifica la ciudad del usuario
	 * 
	 * @param city,
	 *            city.
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Devuelve el país del usuario
	 * 
	 * @return country
	 */
	public String getCountry() {
		return this.country;
	}

	/**
	 * Modifica el país del usuario
	 * 
	 * @param country,
	 *            country.
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * Devuelve la url de la foto de usuario en icono
	 * 
	 * @return profileImageUrlSmall
	 */
	public String getProfileImageUrlSmall() {
		return this.profileImageUrlSmall;
	}

	/**
	 * Modifica la url de la foto de usuario en icono
	 * 
	 * @param profileImageUrlSmall,
	 *            profileImageUrlSmall.
	 */
	public void setProfileImageUrlSmall(String profileImageUrlSmall) {
		this.profileImageUrlSmall = profileImageUrlSmall;
	}

	/**
	 * Devuelve la url de la foto del usuario
	 * 
	 * @return profileImageUrl
	 */
	public String getProfileImageUrl() {
		return this.profileImageUrl;
	}

	/**
	 * Modifica la url de la foto del usuario
	 * 
	 * @param profileImageUrl,
	 *            profileImageUrl.
	 */
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	/**
	 * Devuelve la lista de roles que tiene el usuario
	 * 
	 * @return roles
	 */
	public List<Role> getRoles() {
		return this.roles;
	}

	/**
	 * Modifica la lista de roles que tiene el usuario
	 * 
	 * @param roles,
	 *            roles.
	 */
	public void setRoles(List<Role> roles) {
		this.roles.clear();
		for (Role role : roles) {
			this.roles.add(role);
		}
	}

	/**
	 * Devuelve la lista de grupos en los que esté el usuario
	 * 
	 * @return groups
	 */
	public List<Group> getGroups() {
		return this.groups;
	}

	/**
	 * Modifica la lista de grupos en los que está el usuario
	 * 
	 * @param groups,
	 *            groups.
	 */
	public void setGroups(List<Group> groups) {
		this.groups.clear();
		for (Group group : groups) {
			this.groups.add(group);
		}
	}

	/**
	 * Devuelve la lista de cursos en los que está matriculado el usuario
	 * 
	 * @return courses
	 */
	public List<Integer> getEnrolledCourses() {
		return this.courses;
	}

	/**
	 * Modifica la lista de cursos en los que está matriculado el usuario
	 * 
	 * @param courses,
	 *            courses.
	 */
	public void setEnrolledCourses(List<Integer> courses) {
		this.courses.clear();
		for (Integer course : courses) {
			this.courses.add(course);
		}
	}

	/**
	 * Método que imprime el nombre completo del usuario.
	 */
	public String toString() {
		return getFullName();
	}

}