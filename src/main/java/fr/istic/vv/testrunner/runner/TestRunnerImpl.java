package fr.istic.vv.testrunner.runner;

import fr.istic.vv.common.MutantContainer;
import fr.istic.vv.report.ReportService;
import fr.istic.vv.testrunner.exception.TestRunnerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TestRunnerImpl implements TestRunner {

	private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);

	private List<Class> classes;

	private List<Class> testClasses;

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
		for (Class<?> classString : classes) {
			addClass(classString);
		}

	}

	private void addClass(Class classString) {
		if (classes == null) {
			classes = new ArrayList<>();
		}
		classes.add(classString);
	}

	/**
	 * @return the testClasses
	 */
	public List<Class> getTestClasses() {
		return testClasses;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.istic.vv.testrunner.runner.TestRunner#setTestClasses(java.util.List)
	 */
	@Override
	public void setTestClasses(List<Class> testClasses) {
		for (Class<?> testClass : testClasses) {
			addTestClass(testClass);
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
		logger.debug("Vérification des informations du TestRunner");

		verifyTestRunnerForExecution();

		logger.info("Recherche de la classe de test à effectuer");
		Class testClass = getTestClassForMutant(mutantContainer);
		if (testClass == null) {
			logger.warn("La classe de test associée à la classe " + mutantContainer.getMutatedClass()
					+ " n'a pas été trouvée");
		}
	}

	private void verifyTestRunnerForExecution() throws TestRunnerException {
		if (classes == null || classes.isEmpty()) {
			throw new TestRunnerException("Les classes du projet ne sont pas renseignées dans le TestRunner");
		}
		if (testClasses == null || testClasses.isEmpty()) {
			throw new TestRunnerException("Les classes  de tests du projet ne sont pas renseignées dans le TestRunner");
		}
		if (mutantContainer == null) {
			throw new TestRunnerException("Le mutant n'est pas renseigné dans le TestRunner");
		}

		logger.debug("Vérification terminée.");
		logger.debug("MUTANT sur la classe : {}", mutantContainer.getMutatedClass());
		logger.debug("Ensemble des classes : {}", classes);
		logger.debug("Ensemble des classes de tests : {}", testClasses);
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
	private void runATestClass(Class<?> testClass) {
		//JUnitCore core = new JUnitCore();

		//logger.info("Ajout du Listener {} pour le test", LoggingListener.class);
		//core.addListener(new LoggingListener()); // Replace LoggingListener by a useful listener

		logger.info("Ajout des classes sauf celle du mutant");

		logger.info("Ajout du mutant");

		logger.info("Début de l'éxécution de la classe de test : {}", testClass);
		// FIXME : La classe ne peut pas être un string
		// Result result = core.run(testClass);
		//Result result;

		logger.info("FINISHED");
		/*
		 * logger.info("IGNORED: {}", result.getIgnoreCount());
		 * logger.info("FAILURES: {}", result.getFailureCount());
		 * logger.info("IGNORED: {}", result.getRunCount());
		 */
	}

}
