package model;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javafx.collections.ObservableList;

public class Chart {
	// TODO no estoy seguro que venga ordenado en orden de insercion
	private ArrayList<String> dates;
	private HashMap<String, ArrayList<Integer>> label;
	private final String[] MONTH = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto",
			"Septiembre", "Octubre", "Noviembre", "Diciembre" };

	// TODO para generar los graficos, construiremos los html correspondientes

	public Chart() {
		dates = new ArrayList<>();
		label = new HashMap<String, ArrayList<Integer>>();
	}

	public ArrayList<String> getDate() {
		return dates;
	}

	public void setDate(int month) {
		// Calenddar los meses los cuenta desde 0
		if (dates.contains(MONTH[month])) {
			return;
		} else {
			dates.add(MONTH[month]);
		}
	}

	/**
	 * @return the label
	 */
	public HashMap<String, ArrayList<Integer>> getLabel() {
		return label;
	}

	/**
	 * Metodo para meter los diferentes eventos con participantes y el numero de
	 * interacciones con ellos.
	 * 
	 * @param selectedParticipants
	 * @param selectedEvents
	 * @param filterLogs
	 */
	public void setLabel(ObservableList<EnrolledUser> selectedParticipants, ObservableList<Event> selectedEvents,
			ArrayList<Log> filterLogs) {
		int cont = 0;

		if (selectedEvents.isEmpty()) {
			for (EnrolledUser participant : selectedParticipants) {
				ArrayList<Integer> cantidad = new ArrayList<>();
				for (int i = 0; i < dates.size(); i++) {
					for (Log log : filterLogs) {
						if (log.getUser().getFullName().equals(participant.toString())
								&& MONTH[log.getDate().get(Calendar.MONTH)] == dates.get(i)) {
							cont += 1;
						}
					}
					cantidad.add(cont);
					cont = 0;
					this.label.put("evento " + participant.getFullName(), cantidad);

				}

			}
		} else {
			if (selectedParticipants.isEmpty()) {

				for (Event event : selectedEvents) {
					ArrayList<Integer> cantidad = new ArrayList<>();
					for (int i = 0; i < dates.size(); i++) {
						for (Log log : filterLogs) {
							if (log.getEvent().equals(event.toString())
									&& MONTH[log.getDate().get(Calendar.MONTH)] == dates.get(i)) {
								cont += 1;
							}
						}
						cantidad.add(cont);
						cont = 0;
						this.label.put("evento " + event.getNameEvent(), cantidad);

					}

				}
			} else {
				for (EnrolledUser participant : selectedParticipants) {
					for (Event event : selectedEvents) {
						ArrayList<Integer> cantidad = new ArrayList<>();
						for (int i = 0; i < dates.size(); i++) {
							for (Log log : filterLogs) {
								if (log.getEvent().equals(event.toString())
										&& log.getUser().getFullName().equals(participant.toString())
										&& MONTH[log.getDate().get(Calendar.MONTH)] == dates.get(i)) {
									cont += 1;
								}
							}
							cantidad.add(cont);
							cont = 0;
							this.label.put(participant.getFullName() + " en evento " + event.getNameEvent(), cantidad);

						}

					}
				}
			}
		}
	}

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
			for (int i = dates.size(); i > 0 ; i--) {
				if (0 != i - 1) {
					pw.print("\"" + dates.get(i -1) + "\",");

				} else {
					pw.print("\"" + dates.get(i -1) + "\"");
				}

			}
			pw.println("],");
			pw.print("\t\tdatasets: [");
			// Valores de los datos

			int tam = label.size();
			int cont = 0;
			for (String dataset : label.keySet()) {
				pw.println("{");
				pw.print("\t\t\tlabel:");
				pw.println("'" + dataset + "',");

				pw.println("\t\t\tbackgroundColor: color(window.chartColors[colorDataSet(" +cont+ ")]).alpha(0.5).rgbString(),");
				pw.println("\t\t\tborderColor: window.chartColors.red,");
				pw.println("\t\t\tborderWidth: 1,");

				pw.println("\t\t\tdata: [");
				for (int i = label.get(dataset).size(); i > 0; i--) {
					if (0 != i - 1) {
						pw.println("\t\t\t\t" + label.get(dataset).get(i -1) + ",");

					} else {
						pw.println("\t\t\t\t" + label.get(dataset).get(i -1));
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
			pw.println("\t]");
			pw.println("};");

			pw.println("window.onload = function() {");
			pw.println("\tvar ctx = document.getElementById(\"canvas\").getContext(\"2d\");");
			pw.println("\twindow.myBar = new Chart(ctx, {");
			pw.println("\t\ttype: 'bar',");
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
			e.printStackTrace();
		} finally {
			try {
				if (null != ficheroJS)
					ficheroJS.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

}