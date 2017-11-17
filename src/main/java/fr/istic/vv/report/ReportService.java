package fr.istic.vv.report;

/**
 * Report Service aggregates all reports generate during mutation testing
 */
public interface ReportService {

	/**
	 * Adds a report to the report service
	 * 
	 * @param report
	 */
	public void addReport(Report report);

	/**
	 * Returns all report informations in a markdown format
	 * 
	 * @return
	 */
	public String toMarkdown();

}
