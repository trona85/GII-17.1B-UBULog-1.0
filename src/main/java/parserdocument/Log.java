/**
 * 
 */
package parserdocument;

import java.util.HashMap;


/**
 * Clase para generar cada tipo de log.
 * 
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public class Log {
	private HashMap<String, String> log;
	
	public Log(String[] fields) {
		if (fields.length > 0){
			
			log =new HashMap<>();
			log.put("hora", fields[0]);
			log.put("nombre del usuario", fields[1]);
			log.put("usuario afectado", fields[2]);
			log.put("contexto del evento", fields[3]);
			log.put("componentes", fields[4]);
			log.put("nombre del evento", fields[5]);
			log.put("descripcion", fields[6]);
			log.put("origen", fields[7]);
			log.put("ip", fields[8]);
		}
		
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

}
