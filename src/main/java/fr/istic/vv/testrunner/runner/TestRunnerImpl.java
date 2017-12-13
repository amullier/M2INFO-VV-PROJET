package fr.istic.vv.testrunner.runner;

import fr.istic.vv.common.MutantContainer;
import fr.istic.vv.report.ReportService;
import fr.istic.vv.testrunner.exception.TestRunnerException;
import fr.istic.vv.testrunner.listener.LoggingListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestRunnerImpl implements TestRunner {

	private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);

	private List<Class> classes;

	private List<Class<?>> testClasses;

	private MutantContainer mutantContainer;

	private ReportService reportService;

	/**
	 * Constructor instanciates list classes
	 */
	public TestRunnerImpl() {
		classes = new ArrayList<>();
		testClasses = new ArrayList<>();
	}

	/**
	 * @return the classes
	 */
	public List<Class> getClasses() {
		return classes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.istic.vv.testrunner.runner.TestRunner#setClasses(java.util.List)
	 */
	@Override
	public void setClasses(List<Class> classes) {
		for (Class classString : classes) {
			addClass(classString);
		}
		if(classes.size() == 0){
			logger.warn("No classes loaded during this setter call");
		}
		else{
			logger.debug("{} classes are loaded in TestRunner",classes.size());
		}
	}

	private void addClass(Class clazz) {
		if (classes == null) {
			classes = new ArrayList<>();
		}
		logger.debug("Adding {} to TestRunner classes collection",clazz);
		classes.add(clazz);
	}

	/**
	 * @return the testClasses
	 */
	public List<Class<?>> getTestClasses() {
		return testClasses;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.istic.vv.testrunner.runner.TestRunner#setTestClasses(java.util.List)
	 */
	@Override
	public void setTestClasses(List<Class> testClasses) {
		for (Class testClass : testClasses) {
			addTestClass(testClass);
		}
		if(testClasses.size() == 0){
			logger.warn("No test classes loaded during this setter call");
		}
		else{
			logger.debug("{} test classes are loaded in TestRunner",classes.size());
		}
	}

	private void addTestClass(Class testClass) {
		if (testClasses == null) {
			testClasses = new ArrayList<>();
		}
		testClasses.add(testClass);
	}

	/**
	 * @return the mutantContainer
	 */
	public MutantContainer getMutantContainer() {
		return mutantContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.istic.vv.testrunner.runner.TestRunner#setMutantContainer(fr.istic.vv.
	 * common.MutantContainer)
	 */
	@Override
	public void setMutantContainer(MutantContainer mutantContainer) {
		this.mutantContainer = mutantContainer;
	}

	/**
	 * @return the reportService
	 */
	public ReportService getReportService() {
		return reportService;
	}

	/**
	 * @param reportService
	 *            the reportService to set
	 */
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.istic.vv.testrunner.runner.TestRunner#runTests()
	 */
	@Override
	public void execute() throws TestRunnerException {


		verifyTestRunnerForExecution();

//		logger.info("Recherche de la classe de test à effectuer");
//		Class testClass = getTestClassForMutant(mutantContainer);
//		if (testClass == null) {
//			logger.warn("La classe de test associée à la classe " + mutantContainer.getMutatedClass()
//					+ " n'a pas été trouvée");
//		}
		//FIXME : rechercher la bonne classe à tester
		runATestClass(testClasses.get(1));
	}

	private void verifyTestRunnerForExecution() throws TestRunnerException {
		logger.trace("TestRunner checking");
		if (classes == null || classes.isEmpty()) {
			throw new TestRunnerException("Project classes are not in TestRunner");
		}
		if (testClasses == null || testClasses.isEmpty()) {
			throw new TestRunnerException("Project test classes are not in TestRunner");
		}
		if (mutantContainer == null) {
			//throw new TestRunnerException("Mutated class is not in TestRunner");
		}

		//logger.debug("MUTANT sur la classe : {}", mutantContainer.getMutatedClass());//FIXME
		logger.debug("Classes : {}", classes);
		logger.debug("Test classes : {}", testClasses);
	}

	/**
	 * Search into test classes the test class corresponding to the mutant
	 * 
	 * @param mutantContainer
	 *            : the mutant
	 * @return the test class name or null if not find
	 */
	private Class getTestClassForMutant(MutantContainer mutantContainer) {
		Class originalClass = mutantContainer.getMutatedClass();
		logger.info("Recherche du test de {}", originalClass);

		String searchTestClassName = originalClass + "Test";
		logger.info("Nom de la classe de test à rechercher : {}", searchTestClassName);

		for (Class testClass : testClasses) {
			//A revoir
//			if (testClass.equals(searchTestClassName)) {
//				logger.debug("Classe de test trouvée dans les classes de test du projet");
//				return testClass;
//			}
		}
		logger.debug("La classe de test n'a pas été trouvée");
		return null;
	}

	/**
	 * Run a test class with the mutated class
	 * 
	 * @param testClass
	 */
	private void runATestClass(Class testClass) {
		try {
			Process p = Runtime.getRuntime().exec("mvn test -f TargetProject/pom.xml");
			if(!p.waitFor(1, TimeUnit.MINUTES)) {
				//timeout - kill the process.
				p.destroy(); // consider using destroyForcibly instead
			}
			int i = p.exitValue();
			logger.info("i = {}",i);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
