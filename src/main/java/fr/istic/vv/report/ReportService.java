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

    void generateHTML();

	void generateCSV();

	void startMutationTesting();

	void stopMutationTesting();

	void setProjectName(String projectName);
}
