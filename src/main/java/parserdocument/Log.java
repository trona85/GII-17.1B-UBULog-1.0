/**
 * 
 */
package parserdocument;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Clase para generar cada tipo de log.
 * 
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public class Log {
	private String date;
	private String nameUser;
	private String userAffected;
	private String context;
	private String component;
	private String event;
	private String description;
	private String origin;
	private String ip;

	public Log(String[] fields) {
		if (fields.length > 0) {
			// TODO problemas con formateo de fecha dara igual asi?????
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy H:m");
			try {
				//System.out.println(formatter.format(formatter.parse(fields[0])).toString());
				this.setDate(formatter.format(formatter.parse(fields[0])));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setNameUser(fields[1]);
			setUserAffected(fields[2]);
			setContext(fields[3]);
			setComponent(fields[4]);
			setEvent(fields[5]);
			setDescription(fields[7]);
			setIp(fields[8]);
			
		}

	}

	public Log() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {

		return "hora: " + getDate() + "\n nombre del usuario: " + getNameUser()
				+ "\n usuario afectado: " + getUserAffected() + "\n contexto del evento: "
				+ getContext() + "\n componentes: " + getComponent()
				+ "\n nombre del evento: " + getEvent() + "\n descripcion: " + getDescription()
				+ "\n origen: " + getOrigin() + "\n ip: " + getIp();
	}

	public String getDate() {
		return date;
	}

	public void setDate(String string) {
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

}
