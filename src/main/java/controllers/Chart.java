package controllers;

public class Chart {

	//TODO para generar los graficos, construiremos los html correspondientes
	
	public Chart() {
		// TODO Auto-generated constructor stub
	}
@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "<html>\n" +
                "<head>\n" +
                "<title>My first chart using FusionCharts Suite XT</title>\n" +
                "<script type=\"text/javascript\" src=\"http://static.fusioncharts.com/code/latest/fusioncharts.js?cacheBust=8232\"></script>\n" +
                "<script type=\"text/javascript\" src=\"fusioncharts/js/themes/fusioncharts.theme.fint.js\"></script>\n" +
                "<script type=\"text/javascript\">\n" +
                "FusionCharts.ready(function () {\n" +
                "    var revenueChart = new FusionCharts({\n" +
                "        type: 'doughnut2d',\n" +
                "        renderAt: 'chart-container',\n" +
                "        width: '450',\n" +
                "        height: '450',\n" +
                "        dataFormat: 'json',\n" +
                "        dataSource: {\n" +
                "            \"chart\": {\n" +
                "                \"caption\": \"Split of Revenue by Product Categories\",\n" +
                "                \"subCaption\": \"Last year\",\n" +
                "                \"numberPrefix\": \"$\",\n" +
                "                \"paletteColors\": \"#0075c2,#1aaf5d,#f2c500,#f45b00,#8e0000\",\n" +
                "                \"bgColor\": \"#ffffff\",\n" +
                "                \"showBorder\": \"0\",\n" +
                "                \"use3DLighting\": \"0\",\n" +
                "                \"showShadow\": \"0\",\n" +
                "                \"enableSmartLabels\": \"0\",\n" +
                "                \"startingAngle\": \"310\",\n" +
                "                \"showLabels\": \"0\",\n" +
                "                \"showPercentValues\": \"1\",\n" +
                "                \"showLegend\": \"1\",\n" +
                "                \"legendShadow\": \"0\",\n" +
                "                \"legendBorderAlpha\": \"0\",\n" +
                "                \"defaultCenterLabel\": \"Total revenue: $64.08K\",\n" +
                "                \"centerLabel\": \"Revenue from $label: $value\",\n" +
                "                \"centerLabelBold\": \"1\",\n" +
                "                \"showTooltip\": \"0\",\n" +
                "                \"decimals\": \"0\",\n" +
                "                \"captionFontSize\": \"14\",\n" +
                "                \"subcaptionFontSize\": \"14\",\n" +
                "                \"subcaptionFontBold\": \"0\"\n" +
                "            },\n" +
                "            \"data\": [\n" +
                "                {\n" +
                "                    \"label\": \"Food\",\n" +
                "                    \"value\": \"28504\"\n" +
                "                }, \n" +
                "                {\n" +
                "                    \"label\": \"Apparels\",\n" +
                "                    \"value\": \"14633\"\n" +
                "                }, \n" +
                "                {\n" +
                "                    \"label\": \"Electronics\",\n" +
                "                    \"value\": \"10507\"\n" +
                "                }, \n" +
                "                {\n" +
                "                    \"label\": \"Household\",\n" +
                "                    \"value\": \"4910\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    }).render();\n" +
                "});\n" +
                "</script>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div id=\"chart-container\">FusionCharts XT will load here!</div>\n" +
                "<div> Loaded from a string </div>\n" +
                "</body>\n" +
                "</html>";
	}{
		
	}
}
