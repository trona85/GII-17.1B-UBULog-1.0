/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.csv.CSVRecord;

import controllers.UBULog;

/**
 * Clase para generar cada tipo de log.
 * 
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public class Log {
	/**
	 * Fecha del log.
	 */
	private Calendar date;
	/**
	 * Usuario que realiza la acción.
	 */
	private String nameUser;
	/**
	 * Usuario afectado.
	 */
	private String userAffected;
	/**
	 * Contexto.
	 */
	private String context;
	/**
	 * Componente.
	 */
	private String component;
	/**
	 * Evento.
	 */
	private String event;
	/**
	 * Descripción.
	 */
	private String description;
	/**
	 * Origen de la conexión.
	 */
	private String origin;
	/**
	 * Ip de donde se realiza la acción.
	 */
	private String ip;
	/**
	 * objeto usuario, usuario que realiza la acción.
	 */
	private EnrolledUser user = null;
	/**
	 * id del usuario que realiza la acción.
	 */
	private int idUser;

	/**
	 * Constructor de clase. Construimos un log.
	 * 
	 * @param csvRecord,
	 *            fila del csv parseado.
	 */
	public Log(CSVRecord csvRecord) {
		if (csvRecord.size() > 0) {
			
			String[] fecha = csvRecord.get(0).split("/");
			date = GregorianCalendar.getInstance();
			date.set(Integer.parseInt(fecha[2].split(" ")[0]), Integer.parseInt(fecha[1]) - 1,
					Integer.parseInt(fecha[0]), Integer.parseInt(fecha[2].split(" ")[1].split(":")[0]),
					Integer.parseInt(fecha[2].split(" ")[1].split(":")[1]));
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
			EnrolledUser desconocido = null;
			ArrayList<EnrolledUser> listUsers =UBULog.session.getActualCourse().getEnrolledUsers();
			for (EnrolledUser us : listUsers) {
				if(us.getId() == -1){
					desconocido = us;
				}
				
			}
			for (int j = 0; j < listUsers.size(); j++) {
				if (getIdUser() == listUsers.get(j).getId()) {
					setUser(listUsers.get(j));
				}

				if (j == listUsers.size() - 1 && getUser() == null) {
					setUser(desconocido);
				}
			}

		}

	}

	// TODO ya no hace falta el toString
	@Override
	public String toString() {

		return "hora: " + getDate() + "\n nombre del usuario: " + getNameUser() + "\n usuario afectado: "
				+ getUserAffected() + "\n contexto del evento: " + getContext() + "\n componentes: " + getComponent()
				+ "\n nombre del evento: " + getEvent() + "\n descripcion: " + getDescription() + "\n origen: "
				+ getOrigin() + "\n ip: " + getIp() + "\n usuario: " + getUser();
	}

	/**
	 * Método que recoge la fecha.
	 * 
	 * @return date
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * Método que asigna un valor a date
	 * 
	 * @param date,
	 *            fecha.
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * Método que recoge el nameUser.
	 * 
	 * @return nameUser
	 */
	public String getNameUser() {
		return nameUser;
	}

	/**
	 * Método que asigna un valor a nameUser.
	 * 
	 * @param nameUser,
	 *            nombre usuario.
	 */
	public void setNameUser(String nameUser) {
		this.nameUser = nameUser;
	}

	/**
	 * Método que recoge el userAffected.
	 * 
	 * @return userAffected
	 */
	public String getUserAffected() {
		return userAffected;
	}

	/**
	 * Método que asigna un valor a userAffected
	 * 
	 * @param userAffected,
	 *            usuario afectado.
	 */
	public void setUserAffected(String userAffected) {
		this.userAffected = userAffected;
	}

	/**
	 * Método que recoge el context.
	 * 
	 * @return context
	 */
	public String getContext() {
		return context;
	}

	/**
	 * Método que asigna un valor a context.
	 * 
	 * @param context,
	 *            contexto.
	 */
	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * Método que recoge el component.
	 * 
	 * @return component
	 */
	public String getComponent() {
		return component;
	}

	/**
	 * Método que asigna un valor a component.
	 * 
	 * @param component,
	 *            componente.
	 */
	public void setComponent(String component) {
		this.component = component;
	}

	/**
	 * Método que recoge el event.
	 * 
	 * @return event
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * Método que asigna un valor a event.
	 * 
	 * @param event,
	 *            evento.
	 */
	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * Método que recoge el description.
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Método que asigna un valor a description.
	 * 
	 * @param description,
	 *            descripción.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Método que recoge el origin.
	 * 
	 * @return origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * Método que asigna un valor a origin
	 * 
	 * @param origin,
	 *            origen.
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * Método que recoge el ip.
	 * 
	 * @return ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * Método que asigna un valor a ip.
	 * 
	 * @param ip,
	 *            ip.
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * Método que recoge el user.
	 * 
	 * @return user
	 */
	public EnrolledUser getUser() {
		return user;
	}

	/**
	 * Método que asigna un valor a enrolledUser
	 * 
	 * @param enrolledUser,
	 *            usuario matriculado.
	 */
	public void setUser(EnrolledUser enrolledUser) {
		this.user = enrolledUser;
	}

	/**
	 * Método que recoge el idUser.
	 * 
	 * @return idUser
	 */
	public int getIdUser() {
		return idUser;
	}

	/**
	 * Método que asigna un valor a idUser
	 * 
	 * @param idUser,
	 *            id usuario.
	 */
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

}
