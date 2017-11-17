package fr.istic.vv.report;

import java.util.ArrayList;
import java.util.List;

public class ReportServiceImpl implements ReportService {

	List<Report> reports;

	/**
	 * Constructor creates a empty reports list
	 */
	public ReportServiceImpl() {
		reports = new ArrayList<>();
	}

	@Override
	public void addReport(Report report) {
		reports.add(report);

	}

	@Override
	public String toMarkdown() {

		return "";
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
