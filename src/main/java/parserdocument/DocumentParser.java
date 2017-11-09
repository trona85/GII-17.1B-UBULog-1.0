/**
 * 
 */
package parserdocument;

import java.util.ArrayList;
import java.util.HashMap;

import UBULogException.UBULogException;
import model.Event;
import model.Log;

/**
 * Clase abstracta para documentos.
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public abstract class DocumentParser implements IDocumentParser {

	private String file;
	private String extension;
	private ArrayList<Log> logs;
	//private ArrayList<Log> logsFilter;
	private HashMap<String,Event> events;
// TODO create evento metodo con su clase tal....
	public DocumentParser() {
		logs = new ArrayList<Log>();
		events =new HashMap<String,Event>();
		//logsFilter = new ArrayList<Log>();
	}
	
	public abstract void readDocument() throws UBULogException;
	public abstract boolean isDocumentValid(String [] fields);

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public ArrayList<Log> getLogs() {
		return logs;
	}

	public void setLogs( Log log) {
		this.logs.add( log);
	}
	
	/*public ArrayList<Log> getLogFilter() {
		return logsFilter;
	}

	public void setLogFilter(ArrayList<Log> logsFilter) {
		this.logsFilter = logsFilter;
	}*/
	
	public void filter(String [] fields, String [] vals){
		/*boolean comp=true;
		int contador = 0;

		for (int i = 0 ; i< logs.size() ; i++) {
			for (int j = 0 ; j< fields.length ; j++) {
				if(!logs.get(i).getLog().get( fields[j]).equals(vals[j])){
					comp=false;
				}
			}
			if(comp == true){
				System.out.println(logs.get(i)+ "prueba");
				logsFilter.put(contador, logs.get(i));
				contador++;
			}
			comp=true;
		}*/
		/*for (int i = 0 ; i< logsFilter.size() ; i++) {
			System.out.println(logsFilter.get(i) + "salida total"); //TODO
			
		}*/
		
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public HashMap<String,Event> getEvents() {
		return events;
	}

}
