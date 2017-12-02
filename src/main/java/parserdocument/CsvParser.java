/**
 * 
 */
package parserdocument;

import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import UBULogPersonalException.UBULogError;
import UBULogPersonalException.UBULogException;
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

	/**
	 * Constructor.
	 * 
	 * @param file,
	 *            Fichero.
	 */
	public CsvParser(String file) {
		super();
		this.setFile(file);
	}

	/**
	 * Leemos el documento cargado y generamos los logs
	 */
	@Override
	public void readDocument() throws UBULogException {

		FileReader fileReader = null;
		try {

			fileReader = new FileReader(getFile());
			CSVParser parser = CSVParser.parse(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

			for (CSVRecord csvRecord : parser) {

				Log log = new Log(csvRecord);
				this.setLogs(log);
				asigEvents(log);
			}

		} catch (Exception e) {
			throw new UBULogException(UBULogError.FICHERO_CON_EXTENSION_CORRECTA_PERO_EXTRUCTURA_INCORRECTA);

		} finally {

			if (null != fileReader) {
				try {
					fileReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}


	/**
	 * Asignamos los eventos
	 * 
	 * @param log
	 */
	private void asigEvents(Log log) {
		// TODO igual no deberia esta aqui
		if (getEvents().get(log.getEvent()) == null) {
			Event event = new Event(log.getEvent());
			event.setLogsEvent(log);
			getEvents().put(log.getEvent(), event);

		} else {
			getEvents().get(log.getEvent()).setLogsEvent(log);

		}
	}
}
