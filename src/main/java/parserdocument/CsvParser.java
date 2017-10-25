/**
 * 
 */
package parserdocument;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * Clase para el parseo de csv.
 * 
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public class CsvParser extends DocumentParser {
	
	public CsvParser(String file) {
		super();
		this.setFile(file);
	}
	/* (non-Javadoc)
	 * @see parserdocument.DocumentParser#readDocument(java.lang.String)
	 */
	@Override
	public void readDocument() {
		BufferedReader br = null;
		Integer cont = 0;
	      
	      try {
	         br =new BufferedReader(new FileReader(this.getFile()));
	         String cabecera = br.readLine();
	         String line = br.readLine();
	         
	         while (null!=line) {
	            String [] fields = line.split(",");
	            
	            this.setLog( new Log(fields));
	            //System.out.println(getLog().toString());
	            this.setLogs(cont, getLog());
	            //getLogs().put(cont, getLog());
	            
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
