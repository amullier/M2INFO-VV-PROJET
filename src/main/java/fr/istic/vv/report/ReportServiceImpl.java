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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReportServiceImpl [reports=" + reports + "]";
	}

}
