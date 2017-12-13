package fr.istic.vv.main;

import fr.istic.vv.common.ClassParser;
import fr.istic.vv.mutator.Mutator;
import fr.istic.vv.report.ReportService;
import fr.istic.vv.report.ReportServiceImpl;
import fr.istic.vv.testrunner.exception.TestRunnerException;
import fr.istic.vv.testrunner.runner.TestRunner;
import fr.istic.vv.testrunner.runner.TestRunnerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	// Add parameter in main to make project generic for all projects
	private static String classesPath = "./TargetProject/target/classes";
	private static String testClassesPath = "./TargetProject/target/classes";

	public static void main(String[] args){
		definePaths(args);

		logger.info("==== V&V PROJECT : Antoine & Romain ====");
		logger.debug("Mutation testing for project :");
		logger.debug("Classes root directory : {}", classesPath);
		logger.debug("Test classes root directory : {}", testClassesPath);

		// Récupération et chargement des classes
		ClassParser classParser = new ClassParser();
		List<Class> classList = classParser.getClassesFromDirectory(classesPath);
		List<Class> testClassList = classParser.getClassesFromDirectory(testClassesPath);

		// Report service initialisation
		ReportService reportService = new ReportServiceImpl();

		// Test Runner initialisation
		TestRunner testRunner = new TestRunnerImpl();
		testRunner.setClasses(classList);
		testRunner.setTestClasses(testClassList);
		testRunner.setReportService(reportService);
		
		// init du mutator
		Mutator mutator = new Mutator(classList, testRunner, classesPath);
		try {
			//mutator.mutate();
		} catch (Exception e) {
			logger.error("Error start mutation", e);
		}
		try {
			testRunner.execute();
		} catch (TestRunnerException e) {
			e.printStackTrace();
		}
	}

	private static void definePaths(String[] args) {
		classesPath = args[0];
		testClassesPath = args[1];
	}
}
