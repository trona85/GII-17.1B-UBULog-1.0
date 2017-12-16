/**
 * 
 */
package model;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase que genera la tabla de logs.
 * 
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public class TableLog {
	
	static final Logger logger = LoggerFactory.getLogger(TableLog.class);
	
	public TableLog(){
		
	}
	
	public void generarTablaLogs(ArrayList<Log> generateLogs) {

		FileWriter ficheroHTML = null;
		PrintWriter pw = null;
		try {
			ficheroHTML = new FileWriter("bin/tablelogs/html/tablelogs.html");
			pw = new PrintWriter(ficheroHTML);
			pw.println("<!DOCTYPE html> \n " + "<html> \n " + "<title>tabla logs</title> \n"
					+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> \n"
					+ "<meta charset=\"utf-8\"> \n"
					+ "<link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">\n" + "<body>\n"
					+ "<div class=\"w3-container\"> \n" + "\t<h2>Tabla de logs</h2>");

			pw.println("\t<table class=\"w3-table-all w3-margin-top\" id=\"myTable\">");
			pw.println("\t\t<tr> \n" + "\t\t<th>Fecha </th>");
			pw.println("\t\t<th>Nombre completo del usuario</th>");
			pw.println("\t\t<th>Usuario afectado</th>");
			pw.println("\t\t<th>Contexto del evento</th>");
			pw.println("\t\t<th>Componente</th>");
			pw.println("\t\t<th>Nombre evento</th>");
			pw.println("\t\t<th>Descripción</th>");
			pw.println("\t\t<th>Origen</th>");
			pw.println("\t\t<th>Dirección IP</th>");
			pw.println("\t</tr>");

			for (Log log : generateLogs) {
				dataTableLog(pw, log);
			}

			pw.println("\t</table>");
			pw.println("</div> \n" + "</body>\n" + "</html>");

		} catch (Exception e) {
			logger.error("Error al generar tabla logs. {}", e);
		} finally {
			if (pw != null) {
				pw.close();
			}
			try {
				if (null != ficheroHTML)
					ficheroHTML.close();
			} catch (Exception e2) {
				logger.error("Error al cerrar fichero html {}", e2);
			}
		}
	}
	
	/**
	 * @param pw
	 * @param log
	 */
	private void dataTableLog(PrintWriter pw, Log log) {
		pw.println("\t<tr>");

		pw.println("\t\t<td>" + log.getDate().get(Calendar.DAY_OF_MONTH) + "/" + (log.getDate().get(Calendar.MONTH)+1) + "/"
				+ log.getDate().get(Calendar.YEAR) + " "+ log.getDate().get(Calendar.HOUR_OF_DAY)+ ":"+log.getDate().get(Calendar.MINUTE)+  "</td>");
		pw.println("\t\t<td>" + log.getNameUser() + "</td>");
		pw.println("\t\t<td>" + log.getUserAffected() + "</td>");
		pw.println("\t\t<td>" + log.getContext() + "</td>");
		pw.println("\t\t<td>" + log.getComponent() + "</td>");
		pw.println("\t\t<td>" + log.getEvent() + "</td>");
		pw.println("\t\t<td>" + log.getDescription() + "</td>");
		pw.println("\t\t<td>" + log.getOrigin() + "</td>");
		pw.println("\t\t<td>" + log.getIp() + "</td>");

		pw.println("\t</tr>");
	}

}
