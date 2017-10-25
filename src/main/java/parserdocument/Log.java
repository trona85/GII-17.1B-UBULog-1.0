/**
 * 
 */
package parserdocument;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import model.MoodleUser;


/**
 * Clase para generar cada tipo de log.
 * 
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public class Log {
	private HashMap<String, String> log;
	private Date date = new Date();
	private MoodleUser nameUser;
	private MoodleUser userAffected;
	private String context;
	private String component;
	private String event;
	private String description;
	private String origin;
	private String ip;
	
	public Log(String[] fields) {
		if (fields.length > 0){
			//TODO problemas con formateo de fecha dara igual asi?????
			 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy H:m");
			log =new HashMap<>();
			
			try {
				System.out.println(formatter.format(formatter.parse(fields[0])).toString());
				//System.err.println(formatter.parse(fields[0]).toString() + "format");
				//this.setDate(formatter.parser(formatter.format(fields[0])));
				System.err.println(getDate());
			} catch (ParseException e) {
				// TODO Auto-generated catch block no es fecha valida........
				e.printStackTrace();
			}
			
			setLog("hora", fields[0]);
			setLog("nombre del usuario", fields[1]);
			//setNameUser(fields[1]);
			setLog("usuario afectado", fields[2]);
			//setUserAffected(fields[2]);
			setLog("contexto del evento", fields[3]);
			//setContext(fields[3]);
			setLog("componentes", fields[4]);
			//setComponent(fields[4]);
			setLog("nombre del evento", fields[5]);
			//setEvent(fields[5]);
			setLog("descripcion", fields[6]);
			//setDescription(fields[7]);
			setLog("origen", fields[7]);
			//setIp(fields[8]);
			setLog("ip", fields[8]);
		}
		
	}
	
	public Log() {
		// TODO Auto-generated constructor stub
	}

	public HashMap<String, String> getLog(){
		return this.log;
	}
	public void setLog(String field, String val){
		log.put(field,val);
	}
	@Override
	public String toString(){
		
		return "hora: " + log.get("hora") + 
		"\n nombre del usuario: "+ log.get("nombre del usuario") + 
		"\n usuario afectado: " + log.get("usuario afectado") + 
		"\n contexto del evento: " + log.get("contexto del evento") + 
		"\n componentes: " + log.get("componentes") + 
		"\n nombre del evento: " + log.get("nombre del evento") + 
		"\n descripcion: " + log.get("descripcion") + 
		"\n origen: " + log.get("origen") + 
		"\n ip: " + log.get("ip");
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public MoodleUser getNameUser() {
		return nameUser;
	}

	public void setNameUser(MoodleUser nameUser) {
		this.nameUser = nameUser;
	}

	public MoodleUser getUserAffected() {
		return userAffected;
	}

	public void setUserAffected(MoodleUser userAffected) {
		this.userAffected = userAffected;
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
