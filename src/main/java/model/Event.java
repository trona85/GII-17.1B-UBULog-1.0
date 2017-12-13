/**
 * 
 */
package model;

import java.util.ArrayList;

/**
 * Clase eventos.
 * 
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public class Event {
	/**
	 * Nombre del evento.
	 */
	private String nameEvent;
	/**
	 * Logs asociados al evento.
	 */
	private ArrayList<Log> logsEvent;
	
	/**
	 * Constructor de clase.
	 * @param nameEvent, nombre del evento.
	 */
	public Event(String nameEvent) {
		this.setNameEvent(nameEvent);
		logsEvent = new ArrayList<Log>();
	}

	/**
	 * Metodo que devuelve nameEvent.
	 * @return nameEvent.
	 */
	public String getNameEvent() {
		return nameEvent;
	}

	/**
	 * Método que asigna un valor a nameEvent.
	 * @param nameEvent, nombre del evento.
	 */
	public void setNameEvent(String nameEvent) {
		//TODO si se lo paso por el constructor no me hara falta, comprobar
		this.nameEvent = nameEvent;
	}

	/**
	 * Metodo que devuelve logsEvent.
	 * @return logsEvent.
	 */
	public ArrayList<Log> getLogsEvent() {
		return logsEvent;
	}

	/**
	 * Método en el que si el log asociado al evento no esta metido, lo metemos.
	 * @param log, log del documento.
	 */
	public void setLogsEvent(Log log) {
		
		if(!logsEvent.contains(log)){
			logsEvent.add(log);
		}
	}
	
	/**
	 * Método que imprime el nombre del evento.
	 */
	@Override
	public String toString() {
		
		return getNameEvent();
	}

}
