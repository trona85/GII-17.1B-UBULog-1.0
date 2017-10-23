/**
 * 
 */
package parserdocument;

/**
 * Clase abstracta para documentos.
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public abstract class DocumentParser implements IDocumentParser {

	private String file;

	
	public abstract void readDocument();

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

}
