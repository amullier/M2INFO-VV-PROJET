package fr.istic.vv.main;

import fr.istic.vv.common.ClassParser;
import fr.istic.vv.mutator.Mutator;
import fr.istic.vv.report.ReportService;
import fr.istic.vv.report.ReportServiceImpl;
import fr.istic.vv.testrunner.runner.TestRunner;
import fr.istic.vv.testrunner.runner.TestRunnerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	//Default values for classes and test classes directories
	private static String rootPath = "./TargetProject/";
	private static String classesPath = rootPath+"target/classes";
	private static String testClassesPath = rootPath +"target/classes";

	public static void main(String[] args){
		definePaths(args);

		logger.info("==================== V&V PROJECT : Antoine & Romain ====================");
		logger.info("Mutation testing for project :");
		logger.info("  | Classes root directory : {}", classesPath);
		logger.info("  | Test classes root directory : {}", testClassesPath);
		logger.info("========================================================================");


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

		logger.info("");
		logger.info("Reporting Generation");
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
