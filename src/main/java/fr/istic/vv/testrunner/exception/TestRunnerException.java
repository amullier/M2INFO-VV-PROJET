package fr.istic.vv.testrunner.exception;

/**
 * Exception for errors occurred during testing
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
