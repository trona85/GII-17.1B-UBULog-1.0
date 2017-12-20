/**
 * 
 */
package controllers;

import java.io.IOException;
import java.net.MalformedURLException;

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
 * @author oscar
 *
 */
public class WebScripting {

	static final Logger logger = LoggerFactory.getLogger(WebScripting.class);

	private HtmlPage page = null;
	private WebClient client = null;
	private String responsive;

	public WebScripting() {
		try {
			client = new WebClient(BrowserVersion.getDefault());
			client.getOptions().setTimeout(0);
			page = client.getPage(UBULog.getHost() + "/login/index.php");
			HtmlForm form = (HtmlForm) page.getElementById("login");

			form.getInputByName("username").setValueAttribute(UBULog.getSession().getUserName());
			form.getInputByName("password").setValueAttribute(UBULog.getSession().getPassword());

			page.getElementById("loginbtn").click();

		} catch (FailingHttpStatusCodeException e) {
			logger.error(e.getMessage());

		} catch (MalformedURLException e) {
			logger.error(e.getMessage());

		} catch (IOException e) {
			logger.error(e.getMessage());

		}
	}

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

		} catch (FailingHttpStatusCodeException e) {
			logger.error(e.getMessage());
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public void close() {
		if (client != null) {
			client.close();
		}
	}

	public String getResponsive() {
		return responsive;
	}

	public void setResponsive(String responsive) {
		this.responsive = responsive;
	}

}
