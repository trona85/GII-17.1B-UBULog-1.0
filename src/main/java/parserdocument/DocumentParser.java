/**
 * 
 */
package parserdocument;

/**
 * @author oscar
 *
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
