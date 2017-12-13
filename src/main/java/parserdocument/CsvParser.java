/**
 * 
 */
package parserdocument;

import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.Log;
import ubulogexception.UBULogError;
import ubulogexception.UBULogException;

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

	/**
	 * Constructor de clase.
	 * 
	 */
	public CsvParser() {
		super();
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
				this.asigEvents(log);
			}

		} catch (Exception e) {
			throw new UBULogException(UBULogError.FICHERO_CON_EXTENSION_CORRECTA_PERO_ESTRUCTURA_INCORRECTA);

		} finally {

			if (null != fileReader) {
				try {
					fileReader.close();
				} catch (IOException e) {
					logger.error("Error IOException \"FileReader\", {}", e);
				}
			}
		}

	}
}
