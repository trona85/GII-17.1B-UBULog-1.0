/**
 * 
 */
package parserdocument;

import java.util.ArrayList;
import java.util.HashMap;

import model.Event;
import model.Log;
import ubulogexception.UBULogException;

/**
 * Clase abstracta para documentos.
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public abstract class DocumentParser implements IDocumentParser {
	/**
	 * Ruta del fichero
	 */
	private String file;
	/**
	 * Lista de logs.
	 */
	private ArrayList<Log> logs;
	/**
	 * 
	 */
	private HashMap<String,Event> events;
	/**
	 * Constructor.
	 */
	public DocumentParser() {
		logs = new ArrayList<Log>();
		events =new HashMap<String,Event>();
	}
	
	public abstract void readDocument() throws UBULogException;
	
	/**
	 * Asignamos los eventos.
	 * 
	 * @param log, log.
	 */
	public void asigEvents(Log log) {

		if (getEvents().get(log.getEvent()) == null) {
			Event event = new Event(log.getEvent());
			event.setLogsEvent(log);
			getEvents().put(log.getEvent(), event);

		} else {
			getEvents().get(log.getEvent()).setLogsEvent(log);

		}
	}

	/**
	 * devuelve la ruta del fichero.
	 * @return file.
	 */
	public String getFile() {
		return file;
	}
	/**
	 * Asigna la ruta del fichero.
	 * @param file, ruta del fichero.
	 */

	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * Recoge los logs.
	 * @return logs.
	 */
	public ArrayList<Log> getLogs() {
		return logs;
	}

	/**
	 * Añade un log.
	 * @param log, log.
	 */
	public void setLogs( Log log) {
		this.logs.add( log);
	}

	/**
	 * Recoge los eventos diferentes que hay en el log.
	 * @return events.
	 */
	public HashMap<String,Event> getEvents() {
		return events;
	}

}
