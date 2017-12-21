/**
 * 
 */
package controllers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Clase que descarga archivo por webscripting.
 * 
 * @author Oscar Fernández Armengol
 * 
 * @version 1.1
 */
public class WebScripting {

	/**
	 * Logger
	 */
	static final Logger logger = LoggerFactory.getLogger(WebScripting.class);

	/**
	 * pagina web
	 */
	private HtmlPage page = null;
	/**
	 * Cliente web
	 */
	private WebClient client = null;
	/**
	 * Log.
	 */
	private String responsive;

	/**
	 * Constructor de clase.
	 */
	public WebScripting() {
		try {
			client = new WebClient(BrowserVersion.getDefault());
			client.getOptions().setTimeout(0);
			page = client.getPage(UBULog.getHost() + "/login/index.php");
			HtmlForm form = (HtmlForm) page.getElementById("login");

			form.getInputByName("username").setValueAttribute(UBULog.getSession().getUserName());
			form.getInputByName("password").setValueAttribute(UBULog.getSession().getPassword());

			page.getElementById("loginbtn").click();

		} catch (FailingHttpStatusCodeException | IOException e) {
			logger.error(e.getMessage());

		}
	}

	/**
	 * Metodo que pide y guarda la respuesta de log de la web.
	 */
	public void getResponsiveWeb() {
		try {
			page = client.getPage(UBULog.getHost() + "/report/log/index.php?chooselog=1&showusers=0&showcourses=0&id="
					+ UBULog.getSession().getActualCourse().getId()
					+ "&user=&date=&modid=&modaction=&origin=&edulevel=-1&logreader=logstore_standard");

			DomNodeList<DomElement> inputs = page.getElementsByTagName("input");
			int valbtn = -1;
			for (int i = 0; i < inputs.getLength(); i++) {
				if (inputs.get(i).getAttribute("type").equals("submit")) {
					valbtn = i;
				}
			}
			
			WebResponse dataDownload = inputs.get(valbtn).click().getWebResponse();
			setResponsive(dataDownload.getContentAsString());

		} catch (FailingHttpStatusCodeException | IOException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Metodo que cierra el cliente.
	 */
	public void close() {
		if (client != null) {
			client.close();
		}
	}

	/**
	 * Recoge responsive.
	 * @return responsive
	 */
	public String getResponsive() {
		return responsive;
	}

	/**
	 * Guarda responsive.
	 * @param responsive, respuesta.
	 */
	public void setResponsive(String responsive) {
		this.responsive = responsive;
	}

}
