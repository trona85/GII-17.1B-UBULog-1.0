package webservice;

/**
 * Clase para las funciones proporcionadas por el web service de moodle 3.3
 * 
 * @author Oscar Fernández Armengol
 *  @author Claudia Martínez Herrero
 * 
 * @version 1.2
 */
public class WebServiceOptions {

	/**
	 * Constructor privado.
	 */
	private WebServiceOptions(){
		
	}
	/**
	 * Token de usuario
	 */
	public static final String SERVICIO_WEB_MOODLE = "moodle_mobile_app";
	/**
	 * Id de usuario y atributos
	 */
	public static final String OBTENER_INFO_USUARIO = "core_user_get_users_by_field";
	/**
	 * Cursos en los que está matriculado el usuario
	 */
	public static final String OBTENER_CURSOS = "core_enrol_get_users_courses";
	/**
	 * Usuarios matriculados en un curso
	 */
	public static final String OBTENER_USUARIOS_MATRICULADOS = "core_enrol_get_enrolled_users";
}
