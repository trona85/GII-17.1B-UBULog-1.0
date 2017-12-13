package webservice;

/**
 * Clase para las funciones proporcionadas por el web service de moodle 3.3
 * 
 * @author Oscar Fernández Armengol
 *  @author Claudia Martínez Herrero
 * 
 * @version 1.0
 */
public class WebServiceOptions {

	//TODO hay servicios que no se utilizan, revisar
	// Devuelve token de usuario
	public static final String SERVICIO_WEB_MOODLE = "moodle_mobile_app";
	// Devuelve id de usuario y atributos
	public static final String OBTENER_INFO_USUARIO = "core_user_get_users_by_field";
	// Devuelve los cursos en los que está matriculado el usuario
	public static final String OBTENER_CURSOS = "core_enrol_get_users_courses";
	// Devuelve los usuarios matriculados en un curso
	public static final String OBTENER_USUARIOS_MATRICULADOS = "core_enrol_get_enrolled_users";
	// Devuelve todas las categorías y elementos evaluables de un curso
	public static final String OBTENER_TABLA_NOTAS = "gradereport_user_get_grades_table";
	// Devuelve información sobre los assignments (tareas) que hay en un curso
	public static final String OBTENER_ASSIGNMENTS = "mod_assign_get_assignments";
	// Devuelve información sobre los quizs (cuestionarios) que hay en un curso
	public static final String OBTENER_QUIZZES = "mod_quiz_get_quizzes_by_courses";

	// TODO los tipos de rol, no filtra por ellos
}
