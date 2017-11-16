package fr.istic.vv.testrunner.runner;

import java.util.List;

import fr.istic.vv.common.MutantContainer;
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
	public void setClasses(List<String> classes);

	/**
	 * Sets project test classes
	 * 
	 * @param testClasses
	 */
	public void setTestClasses(List<String> testClasses);

	/**
	 * Sets the mutantContainer for the test execution
	 * 
	 * @param mutantContainer
	 */
	public void setMutantContainer(MutantContainer mutantContainer);

	/**
	 * Run tests in project test classes
	 * 
	 * @pre Classes, TestClasses and MutantContainer must not be empty
	 */
	public void runTests() throws TestRunnerException;
}
