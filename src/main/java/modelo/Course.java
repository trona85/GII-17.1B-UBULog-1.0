/**
 * 
 */
package modelo;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author oscar
 *
 */
public class Course {
	// TODO http://localhost/moodle//webservice/rest/server.php?wstoken=9a5e85d1e61c1c42509d77b34f26643a&moodlewsrestformat=json&wsfunction=core_enrol_get_users_courses&userid=6

	private int id;
	private String shortName;
	private String fullName;
	private int enrolledUsersCount;
	private String idNumber;
	private String summary;
	private String showGrades;
	private String lang;
	private String progress;
	private Date startDate;
	private Date endDate;
	//public ArrayList<EnrolledUser> enrolledUsers;
	//public Set<String> roles; // roles que hay en el curso
	//public Set<String> groups; // grupos que hay en el curso
	//public ArrayList<GradeReportLine> gradeReportLines;
	//public Set<String> typeActivities;

	static final Logger logger = LoggerFactory.getLogger(Course.class);
	/**
	 * Constructor de un curso a partir de contenido JSON. Establece los
	 * parámetros de un curso.
	 * 
	 * @param obj
	 *            objeto JSON con la información del curso
	 * @throws Exception
	 */
	public Course(/*JSONObject obj*/) throws Exception {
		/*this.id = obj.getInt("id");
		if (obj.getString("shortname") != null)
			this.shortName = obj.getString("shortname");
		if (obj.getString("fullname") != null)
			this.fullName = obj.getString("fullname");
		if (obj.getInt("enrolledusercount") != 0)
			this.enrolledUsersCount = obj.getInt("enrolledusercount");
		if (obj.getString("idnumber") != null)
			this.idNumber = obj.getString("idnumber");
		if (obj.getString("summary") != null)
			this.summary = obj.getString("summary");*/
		//this.enrolledUsers = new ArrayList<EnrolledUser>();
		//this.gradeReportLines = new ArrayList<GradeReportLine>();
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
	 * Devuelve el n� de usuarios del curso
	 * 
	 * @return enrolledUsersCount
	 */
	public int getEnrolledUsersCount() {
		return this.enrolledUsersCount;
	}

	/**
	 * Modifica el n� de usuarios del curso
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

	public String getShowGrades() {
		return showGrades;
	}

	public void setShowGrades(String showGrades) {
		this.showGrades = showGrades;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
