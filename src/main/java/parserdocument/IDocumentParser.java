/**
 * 
 */
package parserdocument;

import ubulogexception.UBULogException;

/**
 * Interfaz para documentos.
 * 
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public interface IDocumentParser {

	public void readDocument() throws UBULogException;

}
