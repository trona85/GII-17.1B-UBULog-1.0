/**
 * 
 */
package parserdocument;

import java.util.HashMap;

/**
 * Clase abstracta para documentos.
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public abstract class DocumentParser implements IDocumentParser {

	private String file;
	private HashMap<Integer, Log > logs;
	private HashMap<Integer, Log > logsFilter;
	private Log log;

	public DocumentParser() {
		logs = new HashMap<>();
		logsFilter = new HashMap<>();
		log = new Log();
	}
	
	public abstract void readDocument();

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public HashMap<Integer, Log > getLogs() {
		return logs;
	}

	public void setLogs(int id, Log log) {
		this.logs.put(id, log);
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}
	
	public HashMap<Integer, Log> getLogFilter() {
		return logsFilter;
	}

	public void setLogFilter(HashMap<Integer, Log > logsFilter) {
		this.logsFilter = logsFilter;
	}
	
	public void filter(String [] fields, String [] vals){
		boolean comp=true;
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
		}
		for (int i = 0 ; i< logsFilter.size() ; i++) {
			System.out.println(logsFilter.get(i) + "salida total"); //TODO
			
		}
		
	}

}
