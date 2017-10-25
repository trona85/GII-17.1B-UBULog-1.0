/**
 * 
 */
package parserdocument;

/**
 * Interfaz para documentos.
 * 
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public interface IDocumentParser {

	public void readDocument();
	public void filter(String [] fields, String [] vals);

}
