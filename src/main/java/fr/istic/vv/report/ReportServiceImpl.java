package fr.istic.vv.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ReportServiceImpl implements ReportService {

	private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

	List<Report> reports;

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
	public String toMarkdown() {
		return "";
	}

	@Override
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
	public String toHTML(){
		StringBuilder html = new StringBuilder();
		html.append(getHTMLHeader());

		for(Report r : reports) {
			if(r.getMutantContainer()!=null){
				html.append("<tr>");
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
		head+="<body>";
		head+="<table class=\"table\">" +
				"<th>Mutated class</th>" +
				"<th>Mutation type</th>" +
				"<th>Mutated method</th>" +
				"<th>Is mutant still alive ?</th>";
		return head;
	}

	private String getHTMLFooter(){
		String footer="";
		footer+="</table>";
		footer+="</body>";
		footer+="</html>";
		return footer;
	}
}
