package fr.istic.vv.testrunner.exception;

/**
 * Exception throwed when an error occured test run with a mutant
 */
public class TestRunnerException extends Exception {

	/**
	 * Build an exception with a message
	 * 
	 * @param message
	 */
	public TestRunnerException(String message) {
		super(message);
	}
}
