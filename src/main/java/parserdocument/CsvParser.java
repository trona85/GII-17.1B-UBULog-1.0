/**
 * 
 */
package parserdocument;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author oscar
 *
 */
public class CsvParser extends DocumentParser {

	private HashMap<Integer, Log > logs;
	private Log log;
	
	public CsvParser(String file) {
		this.setFile(file);
		logs = new HashMap<>();
	}
	/* (non-Javadoc)
	 * @see parserdocument.DocumentParser#readDocument(java.lang.String)
	 */
	@Override
	public void readDocument() {
		BufferedReader br = null;
		Integer cont = 1;
	      
	      try {
	         br =new BufferedReader(new FileReader(this.getFile()));
	         String cabecera = br.readLine();
	         String line = br.readLine();
	         
	         while (null!=line) {
	            String [] fields = line.split(",");
	            
	            log = new Log(fields);
	            System.out.println(log.toString());
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
