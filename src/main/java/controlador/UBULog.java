/**
 * 
 */
package controlador;

import parserdocument.CsvParser;

/**
 * @author oscar
 *
 */
public class UBULog {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// IDocumentParser p = new CsvParser();
		System.out.println("empezamos");
		CsvParser p = new CsvParser("doc/docparser/logs_curso2_20171012-1005.csv");
		p.readDocument();

	}

}
