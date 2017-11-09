/**
 * 
 */
package parserdocument;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import UBULogException.UBULogError;
import UBULogException.UBULogException;
import model.Event;
import model.Log;

/**
 * 
 * Clase para el parseo de csv.
 * 
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public class CsvParser extends DocumentParser {

	static final Logger logger = LoggerFactory.getLogger(CsvParser.class);

	public CsvParser(String file) {
		super();
		this.setFile(file);
	}

	@Override
	public void readDocument() throws UBULogException {
		BufferedReader br = null;
		Integer cont = 0;

		try {
			br = new BufferedReader(new FileReader(this.getFile()));
			String line = br.readLine();
			String[] fields = line.split(",");

			if (!isDocumentValid(fields)) {
				throw new Exception();
			}
			line = br.readLine();

			while (null != line) {
				fields = line.split(",");
				Log log =new Log(fields);
				this.setLogs(log);
				
				// asignamos los eventos con sus log correspondientes
				asigEvents(log);

				line = br.readLine();
				cont += 1;
			}
			
		} catch (Exception e) {
			throw new UBULogException(UBULogError.FICHERO_CON_EXTENSION_CORRECTA_PERO_EXTRUCTURA_INCORRECTA);

		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * @param log
	 */
	private void asigEvents(Log log) {
		if(getEvents().get(log.getEvent()) == null){
			Event event = new Event(log.getEvent()); 
			event.setLogsEvent(log);
			getEvents().put(log.getEvent(), event);
			
		}else{
			getEvents().get(log.getEvent()).setLogsEvent(log);
			
		}
	}

	@Override
	public boolean isDocumentValid(String[] fields) {
		if (fields.length != 9) {
			return false;
		} else {
			for (int i = 0; i < fields.length; i++) {
				if (!(fields[i].contains("Hora") || fields[i].contains("Nombre completo del usuario")
						|| fields[i].contains("Usuario afectado") || fields[i].contains("Contexto del evento")
						|| fields[i].contains("Componente") || fields[i].contains("Nombre evento")
						|| fields[i].contains("Descripción") || fields[i].contains("Origen")
						|| fields[i].contains("Dirección IP"))) {
					return false;

				}
			}
		}

		return true;
	}
}
