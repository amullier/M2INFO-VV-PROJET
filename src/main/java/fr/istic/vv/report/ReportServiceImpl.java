package fr.istic.vv.report;

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

	private List<Report> reports;

	private String projectName;

	private long startTesting;

	private long stopTesting;

	/**
	 * Constructor creates a empty reports list
	 */
	public ReportServiceImpl() {
		reports = new ArrayList<>();
	}

	@Override
	public void addReport(Report report) {
		logger.debug("======================================");
		logger.debug("Mutation information :");
		logger.debug("Mutated class {}", report.getMutantContainer().getMutatedClass());
		logger.debug("Mutation method {}", report.getMutantContainer().getMutationMethod());
		logger.debug("Mutation type {}", report.getMutantContainer().getMutationType());
		logger.debug("Mutation testing result : {}",report.isMutantAlive());
		logger.debug("======================================");

		logger.debug("Adding report, current size = {}",reports.size());
		reports.add(report);

	}

	@Override
	public void generateCSV() {
		try {
			DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
			Date date = new Date(startTesting);
			BufferedWriter writer = new BufferedWriter(new FileWriter("./reports/report-"+ df.format(date) +".csv"));
			writer.write(toCSV());
			writer.close();
		} catch (IOException e) {
			logger.error("Reporting error during file writing",e);
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

		for(Report report : reports){
			if(report.getMutantContainer()!=null){
				stringBuilder.append(report.getMutantContainer().getMutatedClass());
				stringBuilder.append(separator);
				stringBuilder.append(report.getMutantContainer().getMutationType());
				stringBuilder.append(separator);
				stringBuilder.append(report.getMutantContainer().getMutationMethod());
				stringBuilder.append(separator);
				if(report.isMutantAlive()){
					stringBuilder.append("TRUE");
				}
				else{
					stringBuilder.append("FALSE");
				}
			}
			stringBuilder.append(separatorLine);
		}

		return stringBuilder.toString();
	}

	@Override
	public void generateHTML() {
		try {
			DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
			Date date = new Date(startTesting);

			BufferedWriter writer = new BufferedWriter(new FileWriter("./reports/report-"+ df.format(date) +".html"));
			writer.write(toHTML());
			writer.close();
		} catch (IOException e) {
			logger.error("Reporting error during file writing",e);
		}
	}

	public String toHTML(){
		StringBuilder html = new StringBuilder();
		html.append(getHTMLHeader());

		int index = 1;
		for(Report r : reports) {
			if(r.getMutantContainer()!=null){
				html.append("<tr>");
				html.append("<td>");
				html.append(index);
				html.append("</td>");
				html.append("<td>");
				html.append(r.getMutantContainer().getMutatedClass());
				html.append("</td>");
				html.append("<td>");
				html.append(r.getMutantContainer().getMutationType());
				html.append("</td>");
				html.append("<td>");
				html.append(r.getMutantContainer().getMutationMethod());
				html.append("</td>");
				html.append(r.isMutantAlive() ? "<td class=\"ko\">" : "<td class=\"ok\">");
				html.append(r.isMutantAlive() ? "TRUE" : "FALSE");
				html.append("</td>");
				index++;
			}
		}

		html.append(getHTMLFooter());
		return html.toString();
	}

	private String getHTMLHeader(){
		String head="";
		head+="<html>";
		head+="<head>" +
				"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css\" integrity=\"sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb\" crossorigin=\"anonymous\">" +
				"<style>" +
				".ok{background: #599759;color: white;}" +
				".ko{background: #aa0000;color: white;}" +
				"</style>" +
				"</head>";
		head+="<body>" +
				"<div class='container'>" +
				"<div class='row' style='min-height: 100px'>" +
				"	<div class='col-md-3' style='padding-top: 14px;'>"+
				"		<img src='https://istic.univ-rennes1.fr/sites/istic.univ-rennes1.fr/files/logoisticfr_0.png' style='width: 216px;'>" +
				"	</div>"+
				"	<div class='col-md-6' style='padding-top: 30px;text-align: center;'>"+
				"		<h2>Mutation testing report</h2>" +
				"	</div>" +
				"	<div class='col-md-3' style='text-align: right;'>" +
				"		<img src='https://fondation.univ-rennes1.fr/sites/default/files/imce/blocs/partenaires/323734_UR1-RVB.png' style='width: 216px;'>"+
				"	</div>" +
				"</div>";
		head+="<table class='table'>" +
				"	<tr>" +
				"		<td><b>Project :</b></td>" +
				"		<td>"+projectName+"</td>" +
				"	</tr>" +
				"	<tr>" +
				"   	<td><b>Duration :</b></td>" +
				"   	<td>"+getInterval()+"</td>" +
				"	</tr>" +
				"</table>";
		head+="<table class='table'>" +
				"<th>Index</th>" +
				"<th>Mutated class</th>" +
				"<th>Mutation type</th>" +
				"<th>Mutated method</th>" +
				"<th>Is mutant still alive ?</th>";
		return head;
	}

	private String getHTMLFooter(){
		String footer="";
		footer+="</table>";
		footer+="<table class='table' style='background-color: #ddd;color:#444;'>" +
				"<tr><td>Mullier Antoine & Romain Sadok - M2INFO V&V Project</td></tr>" +
				"</table>";
		footer+="</div>" +
				"</body>";
		footer+="</html>";
		return footer;
	}

	@Override
	public void startMutationTesting() {
		startTesting = System.currentTimeMillis();
		logger.debug("Set startTesting to {}",startTesting);
	}

	@Override
	public void stopMutationTesting() {
		stopTesting = System.currentTimeMillis();
		logger.debug("Set stopTesting to {}",stopTesting);

	}

	private String getInterval(){
		DateFormat df = new SimpleDateFormat("[dd/MM/yyyy] HH:mm:ss");

		Date startDate = new Date(startTesting);
		Date endDate = new Date(stopTesting);

		String suffix = " ("+df.format(startDate)+" to "+df.format(endDate)+")";
		String label = "<b>";
		if(stopTesting>=startTesting){
			//Second conversion
			long start = startTesting/1000;
			long stop = stopTesting/1000;

			int hours = (int)(stop - start)/3600;
			int minutes = (int)(stop - start)/60 - hours*60;
			int seconds = (int)(stop - start) - minutes*60 - hours*3600;

			if(hours>0){
				label+= hours+"h";
			}
			if(minutes>0){
				label+= minutes+"m";
			}
			if(seconds>0){
				label+= seconds+"s";
			}
			return label+"</b>"+suffix;
		}
		else{
			return "ERROR";
		}
	}

	@Override
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
