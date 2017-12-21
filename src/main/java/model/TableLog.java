/**
 * 
 */
package model;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase que genera la tabla de logs.
 * 
 * @author Oscar Fernández Armengol
 * 
 * @version 1.1
 */
public class TableLog {
	
	static final Logger logger = LoggerFactory.getLogger(TableLog.class);
	
	public void generarTablaLogs(List<Log> list) {

		try(FileWriter ficheroHTML = new FileWriter(getClass().getResource("/tablelogs/html/tablelogs.html").getFile());
				PrintWriter pw = new PrintWriter(ficheroHTML); ) {
			String initRow ="\t\t<th>";
			String finalRow = "</th>";
			pw.println("<!DOCTYPE html> \n " + "<html> \n " + "<title>tabla logs</title> \n"
					+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> \n"
					+ "<meta charset=\"utf-8\"> \n"
					+ "<link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">\n" + "<body>\n"
					+ "<div class=\"w3-container\"> \n" + "\t<h2>Tabla de logs</h2>");

			pw.println("\t<table class=\"w3-table-all w3-margin-top\" id=\"myTable\">");
			pw.println("\t\t<tr> \n" + initRow + "Fecha " + finalRow + "");
			pw.println(initRow + "Nombre completo del usuario" + finalRow + "");
			pw.println(initRow + "Usuario afectado" + finalRow + "");
			pw.println(initRow + "Contexto del evento" + finalRow + "");
			pw.println(initRow + "Componente" + finalRow + "");
			pw.println(initRow + "Nombre evento" + finalRow + "");
			pw.println(initRow + "Descripción" + finalRow + "");
			pw.println(initRow + "Origen" + finalRow + "");
			pw.println(initRow + "Dirección IP" + finalRow + "");
			pw.println("\t</tr>");

			for (Log log : list) {
				dataTableLog(pw, log);
			}

			pw.println("\t</table>");
			pw.println("</div> \n" + "</body>\n" + "</html>");

		} catch (Exception e) {
			logger.error("Error al generar tabla logs. {}", e);
		} 
	}
	
	/**
	 * Método para construir la tabla de logs.
	 * @param pw, printerWriter.
	 * @param log, logs.
	 */
	private void dataTableLog(PrintWriter pw, Log log) {
		String initRow ="\t\t<td>";
		String finalRow = "</td>";
		pw.println("\t<tr>");

		pw.println(initRow + log.getDate().get(Calendar.DAY_OF_MONTH) + "/" + (log.getDate().get(Calendar.MONTH)+1) + "/"
				+ log.getDate().get(Calendar.YEAR) + " "+ log.getDate().get(Calendar.HOUR_OF_DAY)+ ":"+log.getDate().get(Calendar.MINUTE)+  finalRow);
		pw.println(initRow + log.getNameUser() + "</td>");
		pw.println(initRow + log.getUserAffected() + finalRow);
		pw.println(initRow + log.getContext() + finalRow);
		pw.println(initRow + log.getComponent() + finalRow);
		pw.println(initRow + log.getEvent() + finalRow);
		pw.println(initRow + log.getDescription() + finalRow);
		pw.println(initRow + log.getOrigin() + finalRow);
		pw.println(initRow + log.getIp() + finalRow);

		pw.println("\t</tr>");
	}

}
