/**
 * 
 */
package parserdocument;

import java.util.HashMap;

/**
 * @author oscar
 *
 */
public abstract class DocumentParser implements IDocumentParser {

	private String file="doc/docparser/logs_DISOCIADOS_20170706.csv";
	private HashMap<String, String> log;
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see parserdocument.IDocumentParser#documentParser(java.lang.String)
	 */
	@Override
	public void documentParser(String file) {

		//this.file = file;
	}
	
	public abstract void readDocument(String file);

	public HashMap<String, String> getLog() {
		return log;
	}

	public void setLog(HashMap<String, String> log) {
		this.log = log;
	}

}
