package fr.istic.vv.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.istic.vv.report.ReportService;
import fr.istic.vv.report.ReportServiceImpl;
import fr.istic.vv.testrunner.runner.TestRunner;
import fr.istic.vv.testrunner.runner.TestRunnerImpl;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	// Add parameter in main to make project generic for all projects
	private static String classesPath = "../VV-DUMMY-PROJET/target/classes";
	private static String testClassesPath = "../VV-DUMMY-PROJET/target/test-classes";

	public static void main(String[] args) throws Throwable {
		logger.info("==== V&V PROJECT : Antoine & Romain ====");
		logger.debug("Mutation testing for project :");
		logger.debug("Classes : {}", classesPath);
		logger.debug("Test classes : {}", classesPath);

		// Report service initialisation
		ReportService reportService = new ReportServiceImpl();

		// Test Runner initialisation
		TestRunner testRunner = new TestRunnerImpl();
		testRunner.setClasses(null);
		testRunner.setTestClasses(null);
		testRunner.setReportService(reportService);

	}
}
