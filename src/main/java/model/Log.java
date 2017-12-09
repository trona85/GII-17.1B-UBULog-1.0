/**
 * 
 */
package model;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.csv.CSVRecord;

/**
 * Clase para generar cada tipo de log.
 * 
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public class Log {
	private Calendar date;
	private String nameUser;
	private String userAffected;
	private String context;
	private String component;
	private String event;
	private String description;
	private String origin;
	private String ip;
	private EnrolledUser user = null;
	private int idUser;

	public Log(CSVRecord csvRecord) {
		if (csvRecord.size() > 0) {
			String[] fecha = csvRecord.get(0).split("/");
			date = GregorianCalendar.getInstance();
			date.set(Integer.parseInt(fecha[2].split(" ")[0]), Integer.parseInt(fecha[1]) - 1,
					Integer.parseInt(fecha[0]));
			this.setDate(date);
			setNameUser(csvRecord.get("Nombre completo del usuario"));
			setUserAffected(csvRecord.get("Usuario afectado"));
			setContext(csvRecord.get("Contexto del evento"));
			setComponent(csvRecord.get("Componente"));
			setEvent(csvRecord.get("Nombre evento"));
			setDescription(csvRecord.get("Descripción"));
			setOrigin(csvRecord.get("Origen"));
			setIp(csvRecord.get("Dirección IP"));
			String[] fieldDescription = getDescription().split("'");
			if (fieldDescription[0].contains("The user with id")) {
				setIdUser(Integer.parseInt(fieldDescription[1]));
			} else {
				setIdUser(-1);
			}

		}

	}

	public Log() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {

		return "hora: " + getDate() + "\n nombre del usuario: " + getNameUser() + "\n usuario afectado: "
				+ getUserAffected() + "\n contexto del evento: " + getContext() + "\n componentes: " + getComponent()
				+ "\n nombre del evento: " + getEvent() + "\n descripcion: " + getDescription() + "\n origen: "
				+ getOrigin() + "\n ip: " + getIp() + "\n usuario: " + getUser();
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar string) {
		this.date = string;
	}

	public String getNameUser() {
		return nameUser;
	}

	public void setNameUser(String fields) {
		this.nameUser = fields;
	}

	public String getUserAffected() {
		return userAffected;
	}

	public void setUserAffected(String fields) {
		this.userAffected = fields;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public EnrolledUser getUser() {
		return user;
	}

	public void setUser(EnrolledUser enrolledUser) {
		this.user = enrolledUser;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

}
