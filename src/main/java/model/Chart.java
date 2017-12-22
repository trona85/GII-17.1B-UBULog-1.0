package model;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.ObservableList;

/**
 * Clase que genera el gráfico.
 * 
 * @author Oscar Fernández Armengol
 * 
 * @version 1.1
 */
public class Chart {

	static final Logger logger = LoggerFactory.getLogger(Chart.class);

	private String typeChart;

	/**
	 * lista de meses.
	 */
	private ArrayList<String> dates;
	/**
	 * HasMap de clave combinación selecionada, valor lista de cuantas veces
	 * aparece por cada mes.
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
		createHTML();
		createCSS();
		createUtils();
		generarGrafica();
	}

	/**
	 * Crear utils.
	 */
	private void createUtils() {
		try (FileWriter ficheroCSS = new FileWriter("./utils.js"); PrintWriter pw = new PrintWriter(ficheroCSS)) {
			pw.println("'use strict';" + 
"'use strict';" + 
"" + 
"window.chartColors = {" + 
"	red: 'rgb(255, 99, 132)'," + 
"	orange: 'rgb(255, 159, 64)'," + 
"	yellow: 'rgb(255, 205, 86)'," + 
"	green: 'rgb(75, 192, 192)'," + 
"	blue: 'rgb(54, 162, 235)'," + 
"	purple: 'rgb(153, 102, 255)'," + 
"	grey: 'rgb(201, 203, 207)'" + 
"};" + 
"" + 
"(function(global) {" + 
"	var Months = [" + 
"		'January'," + 
"		'February'," + 
"		'March'," + 
"		'April'," + 
"		'May'," + 
"		'June'," + 
"		'July'," + 
"		'August'," + 
"		'September'," + 
"		'October'," + 
"		'November'," + 
"		'December'" + 
"	];" + 
"" + 
"	var COLORS = [" + 
"		'#4dc9f6'," + 
"		'#f67019'," + 
"		'#f53794'," + 
"		'#537bc4'," + 
"		'#acc236'," + 
"		'#166a8f'," + 
"		'#00a950'," + 
"		'#58595b'," + 
"		'#8549ba'" + 
"	];" + 
"" + 
"	var Samples = global.Samples || (global.Samples = {});" + 
"	var Color = global.Color;" + 
"" + 
"	Samples.utils = {" + 
"		// Adapted from http://indiegamr.com/generate-repeatable-random-numbers-in-js/" + 
"		srand: function(seed) {" + 
"			this._seed = seed;" + 
"		}," + 
"" + 
"		rand: function(min, max) {" + 
"			var seed = this._seed;" + 
"			min = min === undefined ? 0 : min;" + 
"			max = max === undefined ? 1 : max;" + 
"			this._seed = (seed * 9301 + 49297) % 233280;" + 
"			return min + (this._seed / 233280) * (max - min);" + 
"		}," + 
"" + 
"		numbers: function(config) {" + 
"			var cfg = config || {};" + 
"			var min = cfg.min || 0;" + 
"			var max = cfg.max || 1;" + 
"			var from = cfg.from || [];" + 
"			var count = cfg.count || 8;" + 
"			var decimals = cfg.decimals || 8;" + 
"			var continuity = cfg.continuity || 1;" + 
"			var dfactor = Math.pow(10, decimals) || 0;" + 
"			var data = [];" + 
"			var i, value;" + 
"" + 
"			for (i = 0; i < count; ++i) {" + 
"				value = (from[i] || 0) + this.rand(min, max);" + 
"				if (this.rand() <= continuity) {" + 
"					data.push(Math.round(dfactor * value) / dfactor);" + 
"				} else {" + 
"					data.push(null);" + 
"				}" + 
"			}" + 
"" + 
"			return data;" + 
"		}," + 
"" + 
"		labels: function(config) {" + 
"			var cfg = config || {};" + 
"			var min = cfg.min || 0;" + 
"			var max = cfg.max || 100;" + 
"			var count = cfg.count || 8;" + 
"			var step = (max - min) / count;" + 
"			var decimals = cfg.decimals || 8;" + 
"			var dfactor = Math.pow(10, decimals) || 0;" + 
"			var prefix = cfg.prefix || '';" + 
"			var values = [];" + 
"			var i;" + 
"" + 
"			for (i = min; i < max; i += step) {" + 
"				values.push(prefix + Math.round(dfactor * i) / dfactor);" + 
"			}" + 
"" + 
"			return values;" + 
"		}," + 
"" + 
"		months: function(config) {" + 
"			var cfg = config || {};" + 
"			var count = cfg.count || 12;" + 
"			var section = cfg.section;" + 
"			var values = [];" + 
"			var i, value;" + 
"" + 
"			for (i = 0; i < count; ++i) {" + 
"				value = Months[Math.ceil(i) % 12];" + 
"				values.push(value.substring(0, section));" + 
"			}" + 
"" + 
"			return values;" + 
"		}," + 
"" + 
"		color: function(index) {" + 
"			return COLORS[index % COLORS.length];" + 
"		}," + 
"		transparentize: function(color, opacity) {" + 
"			var alpha = opacity === undefined ? 0.5 : 1 - opacity;" + 
"			return Color(color).alpha(alpha).rgbString();" + 
"		}" + 
"	};" + 
"" + 
"	// DEPRECATED" + 
"	window.randomScalingFactor = function() {" + 
"		return Math.round(Samples.utils.rand(-100, 100));" + 
"	};" + 
"" + 
"	// INITIALIZATION" + 
"" + 
"	Samples.utils.srand(Date.now());" + 
"" + 
"	// Google Analytics" + 
"	/* eslint-disable */" + 
"	if (document.location.hostname.match(/^(www\\.)?chartjs\\.org$/)) {" + 
"		(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){" + 
"		(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o)," + 
"		m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)" + 
"		})(window,document,'script','//www.google-analytics.com/analytics.js','ga');" + 
"		ga('create', 'UA-28909194-3', 'auto');" + 
"		ga('send', 'pageview');" + 
"	}" + 
"	/* eslint-enable */" + 
"" + 
"}(this));");

		} catch (Exception e) {
			logger.error("Error al generar html. {}", e);
		}

	}

	/**
	 * Crear css.
	 */
	private void createCSS() {
		try (FileWriter ficheroCSS = new FileWriter("./chart.css"); PrintWriter pw = new PrintWriter(ficheroCSS)) {
			pw.println("@import url('https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css');"
					+ "@import url('https://fonts.googleapis.com/css?family=Lato:100,300,400,700,900');"
					+ "body, html {color: #333538;font-family: 'Lato', sans-serif;line-height: 1.6;padding: 0;margin: 0;}a {color: #f27173;text-decoration: none;}a:hover {color: #e25f5f;text-decoration: underline;"
					+ "}.content {	max-width: 800px;	margin: auto;	padding: 16px 32px;}.header {text-align: center;	padding: 32px 0;}.wrapper {	min-height: 400px;"
					+ "padding: 16px 0;position: relative;}.wrapper.col-2 {	display: inline-block;	min-height: 256px;	width: 49%;}@media (max-width: 400px) {	.wrapper.col-2 {width: 100%	}}"
					+ ".wrapper canvas {	-moz-user-select: none;	-webkit-user-select: none;	-ms-user-select: none;}"
					+ ".toolbar {display: flex;}.toolbar > * {	margin: 0 8px 0 0;}.btn {	background-color: #aaa;	border-radius: 4px;	color: white;	padding: 0.25rem 0.75rem;}"
					+ ".btn .fa {	font-size: 1rem;}.btn:hover {	background-color: #888;	color: white;	text-decoration: none;}"
					+

					".btn-chartjs { background-color: #f27173; }.btn-chartjs:hover { background-color: #e25f5f; }.btn-docs:hover { background-color: #2793db; }.btn-docs { background-color: #36A2EB; }.btn-docs:hover { background-color: #2793db; }.btn-gh { background-color: #444; }.btn-gh:hover { background-color: #333; }"
					+

					".btn-on {	border-style: inset;}.chartjs-title {	font-size: 2rem;	font-weight: 600;	white-space: nowrap;}.chartjs-title::before {	background-image: url(logo.svg); "
					+ "	background-position: left center;background-repeat: no-repeat;background-size: 40px;content: 'Chart.js | ';color: #f27173;font-weight: 600;"
					+ "	padding-left: 48px;}.chartjs-caption {	font-size: 1.2rem;}.chartjs-links {	display: flex;	justify-content: center;	padding: 8px 0;}"
					+

					".chartjs-links a {align-items: center;display: flex;	font-size: 0.9rem;	margin: 0.2rem;}.chartjs-links .fa:before {	margin-right: 0.5em;}"
					+

					".samples-category {	display: inline-block;	margin-bottom: 32px;	vertical-align: top;	width: 25%;}.samples-category > .title {	color: #aaa;"
					+ "font-weight: 300;	font-size: 1.5rem;}.samples-category:hover > .title {	color: black;}.samples-category > .items {	padding: 8px 0; "
					+ "}.samples-entry {padding: 0 0 4px 0;}.samples-entry > .title {	font-weight: 700;}@media (max-width: 640px) {	.samples-category { width: 33%; }}"
					+

					"@media (max-width: 512px) {	.samples-category { width: 50%; }}@media (max-width: 420px) {	.chartjs-caption { font-size: 1.05rem; }	.chartjs-title::before { content: ''; }"
					+ "	.chartjs-links a { flex-direction: column; }	.chartjs-links .fa { margin: 0 }	.samples-category { width: 100%; }}.analyser table {"
					+ "	color: #333;	font-size: 0.9rem;	margin: 8px 0;	width: 100%}.analyser th {	background-color: #f0f0f0;	padding: 2px;}"
					+ ".analyser td {	padding: 2px;	text-align: center;}");

		} catch (Exception e) {
			logger.error("Error al generar html. {}", e);
		}

	}

	/**
	 * Metodo que crea el html de chart.
	 */
	private void createHTML() {
		try (FileWriter ficheroJS = new FileWriter("./chart.html"); PrintWriter pw = new PrintWriter(ficheroJS)) {
			pw.println("<!doctype html><html><head>" + "<meta charset=\"utf-8\">"
					+ "<title>Gráfico de interacción en UbuVirtual</title>"
					+ "<link rel=\"stylesheet\" href=\"./chart.css\">"
					+ "<script type=\"text/javascript\" src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.bundle.js\"></script>"
					+ "<script type=\"text/javascript\" src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.js\"></script>"
					+ "<script type=\"text/javascript\" src=\"./Chart.js\"></script> "
					+ "<script type=\"text/javascript\" src=\"./utils.js\"> </script>"
					+ "<style>canvas {-moz-user-select: none;-webkit-user-select: none;-ms-user-select: none;}</style>"
					+ "</head><body>" + "<div id=\"container\" style=\"width: 100%;\">"
					+ " <canvas id=\"canvas\"></canvas>" + "</div></body></html>");

		} catch (Exception e) {
			logger.error("Error al generar html. {}", e);
		}
	}

	/**
	 * Método que devuelve lista de meses del log.
	 * 
	 * @return date.
	 */
	public List<String> getDate() {
		return dates;
	}

	/**
	 * Método que añade un mes a la lista si no existe ya en la lista
	 * 
	 * @param month,
	 *            mes del log.
	 * @param year,
	 *            año.
	 */
	private void setDate(int month, int year) {
		if (!dates.contains(MONTH[month] + " " + year)) {
			dates.add(MONTH[month] + " " + year);
		}
	}

	/**
	 * Devuelve HasMap de clave combinación selecionada, valor lista de cuantas
	 * veces aparece por cada mes.
	 * 
	 * @return label
	 */
	public Map<String, ArrayList<Integer>> getLabel() {
		return label;
	}

	/**
	 * Método para asignamos los mese para la gráfica.
	 * 
	 * @param logs,
	 *            logs cargados.
	 */
	private void asignedUserMonth(List<Log> logs) {
		getDate().clear();
		for (int i = 0; i < logs.size(); ++i) {
			setDate(logs.get(i).getDate().get(Calendar.MONTH), logs.get(i).getDate().get(Calendar.YEAR));
		}
	}

	/**
	 * Metodo para meter los diferentes eventos con participantes y el numero de
	 * interacciones con ellos.
	 * 
	 * @param selectedParticipants,
	 *            participantes seleccionados.
	 * @param selectedEvents,
	 *            eventos seleccionados.
	 * @param filterLogs,
	 *            logs.
	 */
	public void setLabel(ObservableList<EnrolledUser> selectedParticipants, ObservableList<Event> selectedEvents,
			ArrayList<Log> filterLogs) {
		int cont = 0;
		String fechaLog = null;
		asignedUserMonth(filterLogs);

		if (selectedEvents.isEmpty()) {
			for (EnrolledUser participant : selectedParticipants) {
				ArrayList<Integer> cantidad = new ArrayList<>();
				for (int i = 0; i < dates.size(); i++) {
					for (Log log : filterLogs) {
						fechaLog = MONTH[log.getDate().get(Calendar.MONTH)] + " " + log.getDate().get(Calendar.YEAR);
						if (log.getUser().getFullName().equals(participant.toString())
								&& fechaLog.equals(dates.get(i))) {
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
							fechaLog = MONTH[log.getDate().get(Calendar.MONTH)] + " "
									+ log.getDate().get(Calendar.YEAR);
							if (log.getEvent().equals(event.toString()) && fechaLog.equals(dates.get(i))) {
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
								fechaLog = MONTH[log.getDate().get(Calendar.MONTH)] + " "
										+ log.getDate().get(Calendar.YEAR);
								if (log.getEvent().equals(event.toString())
										&& log.getUser().getFullName().equals(participant.toString())
										&& fechaLog.equals(dates.get(i))) {
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

		try (FileWriter ficheroJS = new FileWriter("./Chart.js"); PrintWriter pw = new PrintWriter(ficheroJS)) {
			pw.println(
					"var MONTHS = [\"January\", \"February\", \"March\", \"April\", \"May\", \"June\", \"July\", \"August\", \"September\", \"October\", \"November\", \"December\"];");
			pw.println("var colorNames = [\"red\", \"orange\", \"yellow\", \"green\", \"blue\", \"purple\", \"grey\"];");
			pw.println("var color = Chart.helpers.color;");
			pw.println("var barChartData = {");

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
		}
	}

	/**
	 * Método para añadir los datos al javaScript.
	 * 
	 * @param pw,
	 *            para escribir en el fichero.
	 */
	private void setDataSetJavaScript(PrintWriter pw) {
		int tam = label.size();
		int cont = 0;
		for (String dataset : label.keySet()) {
			pw.println("{");
			pw.print("\t\t\tlabel:");
			pw.println("'" + dataset + "',");

			pw.println("\t\t\tbackgroundColor: colorDataSet(" + cont
					+ "),");
			pw.println("\t\t\tborderColor: colorDataSet(" + cont
					+ "),");
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
	 * 
	 * @return typeChart
	 */
	public String getTypeChart() {
		return typeChart;
	}

	/**
	 * Método que asigna el tipo de gráfico.
	 * 
	 * @param typeChart,
	 *            tipo de gráfico.
	 */
	public void setTypeChart(String typeChart) {
		this.typeChart = typeChart;
	}

}
