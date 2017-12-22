package fr.istic.vv.report;

import fr.istic.vv.common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportServiceImpl implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    private List<Report> reportsAlive;

    private List<Report> reportsDead;

    private List<Report> reports;

    private String projectName;

    private long startTesting;

    private long stopTesting;

    private String htmlFilename;

    private static final String HTML_STARTLINE = "<td>";
    private static final String HTML_ENDLINE = "</td>";

    private int aliveNumber;
    private int totalNumber;

    /**
     * Constructor creates a empty reports list
     */
    public ReportServiceImpl() {
        reportsAlive = new ArrayList<>();
        reportsDead = new ArrayList<>();
        reports = new ArrayList<>();
    }

    @Override
    public void addReport(Report report) {
        //If the report is null warn add return
        if (report == null || report.getMutantContainer() == null) {
            logger.warn("Adding a NULL report could be an error.");
            return;
        }

        //Debug logging
        logger.debug("======================================");
        logger.debug("Mutation information :");
        logger.debug("Mutated class {}", report.getMutantContainer().getMutatedClass());
        logger.debug("Mutation method {}", report.getMutantContainer().getMutationMethod());
        logger.debug("Mutation type {}", report.getMutantContainer().getMutationType());
        logger.debug("Mutation testing result : {}", report.isMutantAlive());
        logger.debug("======================================");

        totalNumber++;
        if (report.isMutantAlive()) {
            aliveNumber++;
            String percentageDisplayed = StringUtils.percentage(1 - (double) aliveNumber / totalNumber);
            logger.info("\t\tMutant is still alive. (Total : {}%)", percentageDisplayed);
            reportsAlive.add(report);
        } else {
            String percentageDisplayed = StringUtils.percentage(1 - (double) aliveNumber / totalNumber);
            logger.info("\t\tMutant killed.(Total : {}%)", percentageDisplayed);
            reportsDead.add(report);
        }
        reports.add(report);

        logger.debug("Adding report, total size = {}", totalNumber);

        generateHTML();
    }

    @Override
    public void generateCSV() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date(startTesting);


        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./reports/report-" + df.format(date) + ".csv"))) {
            writer.write(toCSV());
        } catch (IOException e) {
            logger.error("Reporting error during file writing", e);
        }
    }

    public String toCSV() {
        String separator = ";";
        String separatorLine = "\n";

        StringBuilder stringBuilder = new StringBuilder();

        //Adding CSV Headers
        stringBuilder.append("Mutated class");
        stringBuilder.append(separator);
        stringBuilder.append("Mutation type");
        stringBuilder.append(separator);
        stringBuilder.append("Mutated method");
        stringBuilder.append(separator);
        stringBuilder.append("Is mutant still alive ?");
        stringBuilder.append(separatorLine);

        for (Report report : reports) {
            if (report.getMutantContainer() != null) {
                stringBuilder.append(report.getMutantContainer().getMutatedClass());
                stringBuilder.append(separator);
                stringBuilder.append(report.getMutantContainer().getMutationType());
                stringBuilder.append(separator);
                stringBuilder.append(report.getMutantContainer().getMutationMethod());
                stringBuilder.append(separator);
                if (report.isMutantAlive()) {
                    stringBuilder.append("TRUE");
                } else {
                    stringBuilder.append("FALSE");
                }
            }
            stringBuilder.append(separatorLine);
        }

        return stringBuilder.toString();
    }

    @Override
    public void generateHTML() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date(startTesting);

        htmlFilename = "./reports/report-" + df.format(date) + ".html";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(htmlFilename))) {
            writer.write(toHTML());
            writer.close();
            logger.debug("HTML file generated.");
        } catch (IOException e) {
            logger.error("Reporting error during file writing", e);
        }
    }

    /**
     * Gets HTML code in order to display alive mutant first
     *
     * @return
     */
    public String toHTML() {
        StringBuilder html = new StringBuilder();
        html.append(getHTMLHeader());

        html.append("<h2><span class='glyphicon glyphicon-warning-sign'></span>Alive Mutants</h2>");
        html.append(getHTMLTableHeader());

        int index = 1;
        for (Report r : reportsAlive) {
            if (r.getMutantContainer() != null) {
                index = getHtmlForReport(html, index, r);
            }
        }

        html.append("</table><h2>Killed Mutants</h2>");
        html.append(getHTMLTableHeader());

        index = 1;
        for (Report r : reportsDead) {
            if (r.getMutantContainer() != null) {
                index = getHtmlForReport(html, index, r);
            }
        }

        html.append(getHTMLFooter());
        return html.toString();
    }

    private int getHtmlForReport(StringBuilder html, int index, Report r) {
        html.append("<tr>");
        html.append(HTML_STARTLINE);
        html.append(index);
        html.append(HTML_ENDLINE);
        html.append("<td class='code'>");
        html.append(r.getMutantContainer().getMutatedClass());
        html.append(HTML_ENDLINE);
        html.append(HTML_STARTLINE);
        html.append(r.getMutantContainer().getMutationType());
        html.append(HTML_ENDLINE);
        html.append("<td class='code'>");
        html.append(r.getMutantContainer().getMutationMethod());
        html.append(HTML_ENDLINE);
        html.append(r.isMutantAlive() ? "<td class=\"ko\">" : "<td class=\"ok\">");
        html.append(r.isMutantAlive() ? "TRUE" : "FALSE");
        html.append(HTML_ENDLINE);
        index++;
        return index;
    }

    private String getHTMLHeader() {
        String head = "";
        head += "<html>";
        head += "<head>" +
                "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css\" integrity=\"sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb\" crossorigin=\"anonymous\">" +
                "<style>" +
                ".ok{background: #599759;color: white;text-align: center;}" +
                ".ko{background: #aa0000;color: white;text-align: center;}" +
                ".code{font-family:monospace;}" +
                "</style>" +
                "</head>";
        head += "<body>" +
                "<div class='container'>" +
                "<div class='row' style='min-height: 100px'>" +
                "	<div class='col-md-3' style='padding-top: 14px;'>" +
                "		<img src='https://istic.univ-rennes1.fr/sites/istic.univ-rennes1.fr/files/logoisticfr_0.png' style='width: 216px;'>" +
                "	</div>" +
                "	<div class='col-md-6' style='padding-top: 30px;text-align: center;'>" +
                "		<h2>Mutation testing report</h2>" +
                "	</div>" +
                "	<div class='col-md-3' style='text-align: right;'>" +
                "		<img src='https://fondation.univ-rennes1.fr/sites/default/files/imce/blocs/partenaires/323734_UR1-RVB.png' style='width: 216px;'>" +
                "	</div>" +
                "</div>";
        double percentage = 1 - (double) aliveNumber / totalNumber;
        head += "<table class='table'>" +
                "	<tr>" +
                "		<td><b>Target project :</b></td>" +
                "		<td>" + projectName + HTML_ENDLINE +
                "	</tr>" +
                "	<tr>" +
                "		<td><b>Execution time :</b></td>" +
                "		<td>" + getInterval() + HTML_ENDLINE +
                "	</tr>" +
                "	<tr>" +
                "		<td><b>Number of mutant :</b></td>" +
                "		<td><b>" + reports.size() + " mutants </b>" + "(" + StringUtils.percentage(percentage) + "% killed)" +
                "           <div class='progress' style='width:200px;float:right;'>" +
                "              <div class='progress-bar bg-success' style='width:" + (percentage * 100) + "%'></div>" +
                "              <div class='progress-bar bg-danger' style='width:" + ((1 - percentage) * 100) + "%'></div>" +
                "           </div>" +
                "       </td>" +
                "	</tr>" +
                "</table>";
        head += "<div style=\"" +
                "    height: 5px;" +
                "    background: #e0e0e0;" +
                "    border-top: solid 1px #aaaaaa;" +
                "    margin: 25px -25px;" +
                "\"></div>";
        return head;
    }

    private String getHTMLTableHeader() {
        return "<table class='table'>" +
                "<th>Index</th>" +
                "<th>Mutated class</th>" +
                "<th>Mutation type</th>" +
                "<th>Mutated method</th>" +
                "<th>Is mutant still alive ?</th>";
    }

    private String getHTMLFooter() {
        String footer = "";
        footer += "</table>";
        footer += "<table class='table' style='background-color: #ddd;color:#444;'>" +
                "<tr><td>Mullier Antoine & Romain Sadok - M2INFO V&V Project</td></tr>" +
                "</table>";
        footer += "</div>" +
                "</body>";
        footer += "</html>";
        return footer;
    }

    @Override
    public void startMutationTesting() {
        startTesting = System.currentTimeMillis();
        logger.debug("Set startTesting to {}", startTesting);
    }

    @Override
    public void stopMutationTesting() {
        stopTesting = System.currentTimeMillis();
        logger.debug("Set stopTesting to {}", stopTesting);

    }

    private String getInterval() {
        DateFormat df = new SimpleDateFormat("[dd/MM/yyyy] HH:mm:ss");

        Date startDate = new Date(startTesting);
        Date endDate = new Date(stopTesting);

        String suffix = " (" + df.format(startDate) + " to " + df.format(endDate) + ")";
        String label = "<b>";
        if (stopTesting >= startTesting) {
            //Second conversion
            long start = startTesting / 1000;
            long stop = stopTesting / 1000;

            int hours = (int) (stop - start) / 3600;
            int minutes = (int) (stop - start) / 60 - hours * 60;
            int seconds = (int) (stop - start) - minutes * 60 - hours * 3600;

            if (hours > 0) {
                label += hours + "h";
            }
            if (minutes > 0) {
                label += minutes + "m";
            }
            if (seconds > 0) {
                label += seconds + "s";
            }
            return label + "</b>" + suffix;
        } else {
            return "ERROR";
        }
    }

    @Override
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<Report> getReports() {
        return reports;
    }

    public int getAliveNumber() {
        return aliveNumber;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setStartTesting(long startTesting) {
        this.startTesting = startTesting;
    }

    public void setStopTesting(long stopTesting) {
        this.stopTesting = stopTesting;
    }
}
