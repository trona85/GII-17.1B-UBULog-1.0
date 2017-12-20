package model;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.ObservableList;

/**
 * Clase que genera el gráfico.
 * 
 * @author Oscar Fernández Armengol
 * 
 * @version 1.0
 */
public class Chart {

	static final Logger logger = LoggerFactory.getLogger(Chart.class);
	
	private String typeChart;

	/**
	 * lista de meses.
	 */
	private ArrayList<String> dates;
	/**
	 * HasMap de clave combinación selecionada, valor lista de cuantas veces aparece por cada mes.
	 */
	private HashMap<String, ArrayList<Integer>> label;
	/**
	 * Lista de meses del año.
	 */
	private final String[] MONTH = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto",
			"Septiembre", "Octubre", "Noviembre", "Diciembre" };

	/**
	 * Constructor de clase.
	 */
	public Chart() {
		dates = new ArrayList<>();
		label = new HashMap<>();
		setTypeChart("bar");
	}

	/**
	 * Método que devuelve lista de meses del log.
	 * @return date.
	 */
	public ArrayList<String> getDate() {
		return dates;
	}

	/**
	 * Método que añade un mes a la lista si no existe ya en la lista
	 * @param month, mes del log.
	 * @param year, año.
	 */
	private void setDate(int month, int year) {
		if (!dates.contains(MONTH[month] + " " + year)) {
			dates.add(MONTH[month] + " " + year);
		}
	}

	/**
	 * Devuelve HasMap de clave combinación selecionada, valor lista de cuantas veces aparece por cada mes.
	 * @return label
	 */
	public HashMap<String, ArrayList<Integer>> getLabel() {
		return label;
	}

	/**
	 * Método para asignamos los mese para la gráfica.
	 * @param logs, logs cargados.
	 */
	private void asignedUserMonth(ArrayList<Log> logs) {
		getDate().clear();
		for (int i = 0; i < logs.size(); ++i) {
			setDate(logs.get(i).getDate().get(Calendar.MONTH), logs.get(i).getDate().get(Calendar.YEAR));
		}
	}

	/**
	 * Metodo para meter los diferentes eventos con participantes y el numero de
	 * interacciones con ellos.
	 * 
	 * @param selectedParticipants, participantes seleccionados.
	 * @param selectedEvents, eventos seleccionados.
	 * @param filterLogs, logs.
	 */
	public void setLabel(ObservableList<EnrolledUser> selectedParticipants, ObservableList<Event> selectedEvents,
			ArrayList<Log> filterLogs) {
		int cont = 0;
		String fechaLog= null;
		asignedUserMonth(filterLogs);
		
		if (selectedEvents.isEmpty()) {
			for (EnrolledUser participant : selectedParticipants) {
				ArrayList<Integer> cantidad = new ArrayList<>();
				for (int i = 0; i < dates.size(); i++) {
					for (Log log : filterLogs) {
						fechaLog = MONTH[log.getDate().get(Calendar.MONTH)]+ " "+ log.getDate().get(Calendar.YEAR);
						if (log.getUser().getFullName().equals(participant.toString())
								&& fechaLog.equals(dates.get(i)) ) {
							cont += 1;
						}
					}
					cantidad.add(cont);
					cont = 0;
					this.label.put(participant.getFullName(), cantidad);

				}

			}
		} else {
			if (selectedParticipants.isEmpty()) {

				for (Event event : selectedEvents) {
					ArrayList<Integer> cantidad = new ArrayList<>();
					for (int i = 0; i < dates.size(); i++) {
						for (Log log : filterLogs) {
							fechaLog = MONTH[log.getDate().get(Calendar.MONTH)]+ " "+ log.getDate().get(Calendar.YEAR);
							if (log.getEvent().equals(event.toString())
									&& fechaLog.equals(dates.get(i)) ) {
								cont += 1;
							}
						}
						cantidad.add(cont);
						cont = 0;
						this.label.put(event.getNameEvent(), cantidad);

					}

				}
			} else {
				for (EnrolledUser participant : selectedParticipants) {
					for (Event event : selectedEvents) {
						ArrayList<Integer> cantidad = new ArrayList<>();
						for (int i = 0; i < dates.size(); i++) {
							for (Log log : filterLogs) {
								fechaLog = MONTH[log.getDate().get(Calendar.MONTH)]+ " "+ log.getDate().get(Calendar.YEAR);
								if (log.getEvent().equals(event.toString())
										&& log.getUser().getFullName().equals(participant.toString())
										&& fechaLog.equals(dates.get(i)) ) {
									cont += 1;
								}
							}
							cantidad.add(cont);
							cont = 0;
							this.label.put(participant.getFullName() + "/" + event.getNameEvent(), cantidad);

						}

					}
				}
			}
		}
	}

	/**
	 * Método para crear el javaScript que nos cargara la gráfica.
	 */
	public void generarGrafica() {

		FileWriter ficheroJS = null;
		PrintWriter pw = null;
		try {
			ficheroJS = new FileWriter("bin/chart/js/Chart.js");
			pw = new PrintWriter(ficheroJS);
			pw.println(
					"var MONTHS = [\"January\", \"February\", \"March\", \"April\", \"May\", \"June\", \"July\", \"August\", \"September\", \"October\", \"November\", \"December\"];");
			pw.println("var colorNames = Object.keys(window.chartColors);");
			pw.println("var color = Chart.helpers.color;");
			pw.println("var barChartData = {");
			// Fechas

			pw.print("\tlabels: [");
			for (int i = dates.size(); i > 0; i--) {
				if (0 != i - 1) {
					pw.print("\"" + dates.get(i - 1) + "\",");

				} else {
					pw.print("\"" + dates.get(i - 1) + "\"");
				}

			}
			pw.println("],");
			pw.print("\t\tdatasets: [");
			// Valores de los datos

			setDataSetJavaScript(pw);
			pw.println("\t]");
			pw.println("};");

			pw.println("window.onload = function() {");
			pw.println("\tvar ctx = document.getElementById(\"canvas\").getContext(\"2d\");");
			pw.println("\twindow.myBar = new Chart(ctx, {");
			pw.println("\t\ttype: '" + typeChart + "',");
			pw.println("\t\tdata: barChartData,");
			pw.println("\t\toptions: {");
			pw.println("\t\t\tresponsive: true,");
			pw.println("\t\t\tlegend: {");
			pw.println("\t\t\t\tposition: 'top',");
			pw.println("\t\t\t},");
			pw.println("\t\t\ttitle: {");
			pw.println("\t\t\t\tdisplay: true,");
			pw.println("\t\t\t\ttext: 'Gráfico de interacción en UbuVirtual'");
			pw.println("\t\t\t}");
			pw.println("\t\t}");
			pw.println("\t});");
			pw.println("};");

			pw.println("function colorDataSet( num){");
			pw.println("\treturn colorNames[num % colorNames.length];");
			pw.println("};");

		} catch (Exception e) {
			logger.error("Error al generar gráfica. {}", e);
		} finally {
			if (pw != null) {
				pw.close();
			}
			try {
				if (null != ficheroJS)
					ficheroJS.close();
			} catch (Exception e2) {
				logger.error(e2.getMessage());
			}
		}
	}

	/**
	 * Método para añadir los datos al javaScript.
	 * @param pw, para escribir en el fichero.
	 */
	private void setDataSetJavaScript(PrintWriter pw) {
		int tam = label.size();
		int cont = 0;
		for (String dataset : label.keySet()) {
			pw.println("{");
			pw.print("\t\t\tlabel:");
			pw.println("'" + dataset + "',");

			pw.println("\t\t\tbackgroundColor: color(window.chartColors[colorDataSet(" + cont
					+ ")]).alpha(0.5).rgbString(),");
			pw.println("\t\t\tborderColor: window.chartColors.red,");
			pw.println("\t\t\tborderWidth: 1,");

			pw.println("\t\t\tdata: [");
			for (int i = label.get(dataset).size(); i > 0; i--) {
				if (0 != i - 1) {
					pw.println("\t\t\t\t" + label.get(dataset).get(i - 1) + ",");

				} else {
					pw.println("\t\t\t\t" + label.get(dataset).get(i - 1));
				}

			}
			cont++;
			pw.println("\t\t\t]");
			if (cont != tam) {
				pw.print("\t\t},");

			} else {
				pw.print("\t\t}");
			}
		}
	}

	/**
	 * Metodo que recupera el tipo de gráfico.
	 * @return typeChart
	 */
	public String getTypeChart() {
		return typeChart;
	}

	/**
	 * Método que asigna el tipo de gráfico.
	 * @param typeChart, tipo de gráfico.
	 */
	public void setTypeChart(String typeChart) {
		this.typeChart = typeChart;
	}

}
