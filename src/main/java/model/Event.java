/**
 * 
 */
package model;

import java.util.ArrayList;

/**
 * @author oscar
 *
 */
public class Event {
	private String nameEvent;
	private ArrayList<Log> logsEvent;
	
	public Event(String nameEvent) {
		this.setNameEvent(nameEvent);
		logsEvent = new ArrayList<Log>();
	}

	public String getNameEvent() {
		return nameEvent;
	}

	public void setNameEvent(String nameEvent) {
		this.nameEvent = nameEvent;
	}

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
	
	@Override
	public String toString() {
		
		return getNameEvent();
	}

}
