package fr.istic.vv.testrunner.runner;

import java.util.List;

import fr.istic.vv.common.MutantContainer;
import fr.istic.vv.report.ReportService;
import fr.istic.vv.testrunner.exception.TestRunnerException;

/**
 * TestRunner interface
 */
public interface TestRunner {

	/**
	 * Sets project classes to be tested
	 * 
	 * @param classes
	 */
	public void setClasses(List<Class> classes);

	/**
	 * Sets project test classes
	 * 
	 * @param testClasses
	 */
	public void setTestClasses(List<Class> testClasses);

	/**
	 * Sets the mutantContainer for the test execution
	 * 
	 * @param mutantContainer
	 */
	public void setMutantContainer(MutantContainer mutantContainer);

	/**
	 * Sets the reportService
	 * 
	 * @param reportService
	 */
	public void setReportService(ReportService reportService);

	/**
	 * Run tests in project test classes
	 * 
	 * @pre Classes, TestClasses and MutantContainer must not be empty
	 */
	public void execute() throws TestRunnerException;

}
