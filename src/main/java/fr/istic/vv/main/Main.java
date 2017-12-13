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

	// Add parameter in main to make project generic for all projects
	private static String classesPath = "./TargetProject/target/classes";
	private static String testClassesPath = "./TargetProject/target/classes";

	public static void main(String[] args){
		//definePaths(args);

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
			mutator.mutate();
		} catch (Exception e) {
			logger.error("Error start mutation", e);
		}


		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("./reports/report"+ System.currentTimeMillis()+".csv"));
			writer.write(reportService.toCSV());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void definePaths(String[] args) {
		if(args!=null && args[0]!=null){
			classesPath = args[0];

		}
		if(args!=null && args[1]!=null) {
			testClassesPath = args[1];
		}
	}
}
