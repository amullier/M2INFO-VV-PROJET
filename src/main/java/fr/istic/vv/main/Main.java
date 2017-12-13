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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	//Default values for classes and test classes directories
	private static String rootPath = "./TargetProject/";
	private static String classesPath = rootPath+"target/classes";
	private static String testClassesPath = rootPath +"target/classes";

	public static void main(String[] args){
		definePaths(args);

		logger.info("==== V&V PROJECT : Antoine & Romain ====");
		logger.debug("Mutation testing for project :");
		logger.debug("Classes root directory : {}", classesPath);
		logger.debug("Test classes root directory : {}", testClassesPath);

		// Project parsing to find classes list
		ClassParser classParser = new ClassParser();
		List<Class> classList = classParser.getClassesFromDirectory(classesPath);
		List<Class> testClassList = classParser.getClassesFromDirectory(testClassesPath);

		// Report service initialization
		ReportService reportService = new ReportServiceImpl();
		reportService.setProjectName(rootPath);

		// Test Runner initialization
		TestRunner testRunner = new TestRunnerImpl();
		testRunner.setRootProjectPath(rootPath);
		testRunner.setClasses(classList);
		testRunner.setTestClasses(testClassList);
		testRunner.setReportService(reportService);

		//Mutator initialization
		Mutator mutator = new Mutator(classList, testRunner, classesPath);
		try {
			reportService.startMutationTesting();
			mutator.mutate();
		} catch (Exception e) {
			logger.error("Error start mutation", e);
		}
		reportService.stopMutationTesting();


		reportService.generateCSV();
		reportService.generateHTML();
	}

	/**
	 * Static method defines project directories (classes directory and test classes directory
	 * This method presumes that project is built with a maven architecture
	 *
	 * @param args : Main arguments
	 */
	private static void definePaths(String[] args) {
		if(args!=null && args.length>=1 && args[0]!=null) {
			rootPath = args[0];
			classesPath = rootPath + "/target/classes";
			testClassesPath = rootPath + "/target/test-classes";
		}
		else{
			logger.warn("Main parameters does not define project to test. Default is set.");
		}
	}
}
