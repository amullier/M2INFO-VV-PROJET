package fr.istic.vv.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.istic.vv.testrunner.runner.TestRunner;
import fr.istic.vv.testrunner.runner.TestRunnerImpl;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Throwable {

		/**
		 * TODO: recuperation du classPath + init
		 */

		TestRunner testRunner = new TestRunnerImpl();
		testRunner.setClasses(null);
		testRunner.setTestClasses(null);

	}
}
