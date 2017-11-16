package fr.istic.vv.testrunner.listener;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logging listener for JUnit test execution
 */
public class LoggingListener extends RunListener {

	Logger logger = LoggerFactory.getLogger(LoggingListener.class);

	@Override
	public void testRunStarted(Description description) throws Exception {
		logger.info("RUN STARTED");
	}

	@Override
	public void testStarted(Description description) throws Exception {
		logger.info("TEST STARTED: " + description.getDisplayName());
	}

	@Override
	public void testFailure(Failure failure) throws Exception {
		logger.info("FAILURE: " + failure.getMessage());
	}

	@Override
	public void testIgnored(Description description) throws Exception {
		logger.info("TEST IGNORED: " + description.getDisplayName());
	}

	@Override
	public void testFinished(Description description) throws Exception {
		logger.info("TEST FINISHED: " + description.getDisplayName());
	}

	@Override
	public void testRunFinished(Result result) throws Exception {
		logger.info("RUN FINISHED");
		logger.info(String.format("| IGNORED: %d", result.getIgnoreCount()));
		logger.info(String.format("| FAILURES: %d", result.getFailureCount()));
		logger.info(String.format("| RUN: %d", result.getRunCount()));
	}
}
