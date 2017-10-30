/**
 * 
 */
package parserdocument;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controllers.UBULog;

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
	public void readDocument() {
		BufferedReader br = null;
		Integer cont = 0;
	      
	      try {
	         br =new BufferedReader(new FileReader(this.getFile()));
	         String line = br.readLine();
	         line = br.readLine();
	         
	         while (null!=line) {
	            String [] fields = line.split(",");

	            this.setLogs(cont, new Log(fields));
	            
	            line = br.readLine();
	            cont +=1;
	         }
	         for (int i=1;i < getLogs().size() ; ++i) {
				logger.info(getLogs().get(i).toString() + i);
			}
	         
	      } catch (Exception e) {
	         System.err.println("no existe");
	         e.printStackTrace();

	      } finally {
	         if (null!=br) {
	            try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         }
	      }

	}
}
