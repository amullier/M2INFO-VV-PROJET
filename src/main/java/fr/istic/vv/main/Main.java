package fr.istic.vv.main;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import fr.istic.vv.common.ClassParser;
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

	public static void main(String[] args){
		logger.info("==== V&V PROJECT : Antoine & Romain ====");
		logger.debug("Mutation testing for project :");
		logger.debug("Classes root directory : {}", classesPath);
		logger.debug("Test classes root directory : {}", testClassesPath);

		// Récupération des classes
		ClassParser classParser = new ClassParser();
		List<Class> classList = classParser.getClassesFromDirectory(classesPath);
		List<Class> testClassList = classParser.getClassesFromDirectory(testClassesPath);

		// Report service initialisation
		ReportService reportService = new ReportServiceImpl();

		// Test Runner initialisation
		TestRunner testRunner = new TestRunnerImpl();
		/*
		 * testRunner.setClasses(null); testRunner.setTestClasses(null);
		 * testRunner.setReportService(reportService);
		 */
	}

	private static Class getClassFromFile(String fullClassName) throws Exception {
		URLClassLoader loader = new URLClassLoader(new URL[] { new URL("file://" + fullClassName) });
		return loader.loadClass(fullClassName);
	}
}
