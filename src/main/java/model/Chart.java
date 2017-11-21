package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.ObservableList;

public class Chart {
	// TODO no estoy seguro que venga ordenado en orden de insercion
	private Set<String> date;
	private HashMap<String, ArrayList<Integer>> label;

	// TODO para generar los graficos, construiremos los html correspondientes

	public Chart() {
		date = new HashSet<>();
		label = new HashMap<String, ArrayList<Integer>>();
	}

	public Set<String> getDate() {
		return date;
	}

	public void setDate(String val) {

		switch (val) {
		case "1":
			this.date.add("Enero");
			break;
		case "2":
			this.date.add("Febrero");
			break;
		case "3":
			this.date.add("Marzo");
			break;
		case "4":
			this.date.add("Abril");
			break;
		case "5":
			this.date.add("Mayo");
			break;
		case "6":
			this.date.add("Junio");
			break;
		case "7":
			this.date.add("Julio");
			break;
		case "8":
			this.date.add("Agosto");
			break;
		case "9":
			this.date.add("Septiembre");
			break;
		case "10":
			this.date.add("Octubre");
			break;
		case "11":
			this.date.add("Noviembre");
			break;
		case "12":
			this.date.add("Diciembre");
			break;

		default:
			break;
		}
	}

	/**
	 * @return the label
	 */
	public HashMap<String, ArrayList<Integer>> getLabel() {
		return label;
	}

	/**
	 * Metodo para meter los diferentes eventos con participantes y el numero de interacciones con ellos.
	 * @param selectedParticipants
	 * @param selectedEvents
	 * @param filterLogs
	 */
	public void setLabel(ObservableList<EnrolledUser> selectedParticipants, ObservableList<Event> selectedEvents,
			ArrayList<Log> filterLogs) {
		//TODO falta contar por meses o semanas si se hace.
		int cont = 0;

		if (selectedEvents.isEmpty()) {
			for (EnrolledUser participant : selectedParticipants) {
				ArrayList<Integer> cantidad = new ArrayList<>();
				for (Log log : filterLogs) {
					if (log.getUser().getFullName().equals(participant.toString())) {
						cont += 1;
					}
				}
				cantidad.add(cont);
				cont = 0;
				this.label.put("evento " + participant.getFullName(), cantidad);

			}
		} else {
			if (selectedParticipants.isEmpty()) {

				for (Event event : selectedEvents) {
					ArrayList<Integer> cantidad = new ArrayList<>();
					for (Log log : filterLogs) {
						if (log.getEvent().equals(event.toString())) {
							cont += 1;
						}
					}
					cantidad.add(cont);
					cont = 0;
					this.label.put("evento " + event.getNameEvent(), cantidad);

				}
			} else {
				for (EnrolledUser participant : selectedParticipants) {
					for (Event event : selectedEvents) {
						ArrayList<Integer> cantidad = new ArrayList<>();
						for (Log log : filterLogs) {
							if (log.getEvent().equals(event.toString())
									&& log.getUser().getFullName().equals(participant.toString())) {
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "<html><head></head><body><canvas id=\"popChart\" width=\"600\" height=\"400\"></canvas>"
				+ "<script src=\"Chart.js-2.7.1/dist/Chart.bundle.js\"></script> "
				+ "<canvas id=\"popChart\" width=\"600\" height=\"400\">asdfasdfa</canvas>" + "<script>"
				+ "var popCanvas = document.getElementById(\"popChart\");"
				+ "var popCanvas = document.getElementById(\"popChart\").getContext(\"2d\");"
				+ "var barChart = new Chart(popCanvas, {" + "type: 'bar'," + "data: { "
				+ " labels: [\"agosto\", \"India\", \"United States\", \"Indonesia\", \"Brazil\", \"Pakistan\", \"Nigeria\", \"Bangladesh\", \"Russia\", \"Japan\"], "
				+ "datasets: [{ " + " label: 'Population', "
				+ "data: [1379302771, 1281935911, 326625791, 260580739, 207353391, 204924861, 190632261, 157826578, 142257519, 126451398], "
				+ "backgroundColor: [ " + "'rgba(255, 99, 132, 0.6)', " + "'rgba(54, 162, 235, 0.6)',"
				+ "'rgba(255, 206, 86, 0.6)'," + "'rgba(75, 192, 192, 0.6)'," + "'rgba(153, 102, 255, 0.6)',"
				+ "'rgba(255, 159, 64, 0.6)'," + "'rgba(255, 99, 132, 0.6)'," + "'rgba(54, 162, 235, 0.6)',"
				+ "'rgba(255, 206, 86, 0.6)'," + "'rgba(75, 192, 192, 0.6)', " + "'rgba(153, 102, 255, 0.6)' " + "]"
				+ "}" + " ]" + "} " + "});" + "</script></body></html>";

		/*
		 * "<html>\n" + "<head>\n" +
		 * "<title>My first chart using FusionCharts Suite XT</title>\n" +
		 * "<script type=\"text/javascript\" src=\"http://static.fusioncharts.com/code/latest/fusioncharts.js?cacheBust=8232\"></script>\n"
		 * +
		 * "<script type=\"text/javascript\" src=\"fusioncharts/js/themes/fusioncharts.theme.fint.js\"></script>\n"
		 * + "<script type=\"text/javascript\">\n" +
		 * "FusionCharts.ready(function () {\n" +
		 * "    var revenueChart = new FusionCharts({\n" +
		 * "        type: 'doughnut2d',\n" +
		 * "        renderAt: 'chart-container',\n" + "        width: '450',\n"
		 * + "        height: '450',\n" + "        dataFormat: 'json',\n" +
		 * "        dataSource: {\n" + "            \"chart\": {\n" +
		 * "                \"caption\": \"Split of Revenue by Product Categories\",\n"
		 * + "                \"subCaption\": \"Last year\",\n" +
		 * "                \"numberPrefix\": \"$\",\n" +
		 * "                \"paletteColors\": \"#0075c2,#1aaf5d,#f2c500,#f45b00,#8e0000\",\n"
		 * + "                \"bgColor\": \"#ffffff\",\n" +
		 * "                \"showBorder\": \"0\",\n" +
		 * "                \"use3DLighting\": \"0\",\n" +
		 * "                \"showShadow\": \"0\",\n" +
		 * "                \"enableSmartLabels\": \"0\",\n" +
		 * "                \"startingAngle\": \"310\",\n" +
		 * "                \"showLabels\": \"0\",\n" +
		 * "                \"showPercentValues\": \"1\",\n" +
		 * "                \"showLegend\": \"1\",\n" +
		 * "                \"legendShadow\": \"0\",\n" +
		 * "                \"legendBorderAlpha\": \"0\",\n" +
		 * "                \"defaultCenterLabel\": \"Total revenue: $64.08K\",\n"
		 * +
		 * "                \"centerLabel\": \"Revenue from $label: $value\",\n"
		 * + "                \"centerLabelBold\": \"1\",\n" +
		 * "                \"showTooltip\": \"0\",\n" +
		 * "                \"decimals\": \"0\",\n" +
		 * "                \"captionFontSize\": \"14\",\n" +
		 * "                \"subcaptionFontSize\": \"14\",\n" +
		 * "                \"subcaptionFontBold\": \"0\"\n" +
		 * "            },\n" + "            \"data\": [\n" +
		 * "                {\n" + "                    \"label\": \"Food\",\n"
		 * + "                    \"value\": \"28504\"\n" +
		 * "                }, \n" + "                {\n" +
		 * "                    \"label\": \"Apparels\",\n" +
		 * "                    \"value\": \"14633\"\n" +
		 * "                }, \n" + "                {\n" +
		 * "                    \"label\": \"Electronics\",\n" +
		 * "                    \"value\": \"10507\"\n" +
		 * "                }, \n" + "                {\n" +
		 * "                    \"label\": \"Household\",\n" +
		 * "                    \"value\": \"4910\"\n" + "                }\n" +
		 * "            ]\n" + "        }\n" + "    }).render();\n" + "});\n" +
		 * "</script>\n" + "</head>\n" + "<body>\n" +
		 * "  <div id=\"chart-container\">FusionCharts XT will load here!</div>\n"
		 * + "<div> Loaded from a string </div>\n" + "</body>\n" + "</html>";
		 */
	}

}
