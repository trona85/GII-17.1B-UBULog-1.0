/**
 * 
 */
package parserdocument;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author oscar
 *
 */
public class CsvParser extends DocumentParser {

	private HashMap<Integer, Log > logs;
	private Log log;
	
	public CsvParser() {
		super();
		logs = new HashMap<>();
	}
	/* (non-Javadoc)
	 * @see parserdocument.DocumentParser#readDocument(java.lang.String)
	 */
	@Override
	public void readDocument(String file) {
		BufferedReader br = null;
		Integer cont = 1;
	      
	      try {
	         br =new BufferedReader(new FileReader("doc/docparser/logs_curso2_20171012-1005.csv"));
	         String cabecera = br.readLine();
	         String line = br.readLine();
	         
	         while (null!=line) {
	            String [] fields = line.split(",");
	            
	            log = new Log(fields);
	            log.toLog();
	            logs.put(cont, log);
	            
	            line = br.readLine();
	            cont +=1;
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
