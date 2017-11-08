package fr.istic.main;

import java.io.File;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.istic.translator.TranslatorImpl;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.Loader;
import javassist.Translator;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {

		// Path definitions
		File classDir = new File("../VV-DUMMY-PROJET/target/classes");
		File testDir = new File("../VV-DUMMY-PROJET/target/test-classes");

		// Test classes definitions
		String testPackage = "fr.istic.vv.operations.";
		String[] testClasses = { testPackage + "PlusTest", testPackage + "MinusTest", testPackage + "TimesTest",
				testPackage + "DivTest" };

		try {
			ClassPool pool = ClassPool.getDefault();

			Loader loader = new Loader(pool);
			Translator translator = new TranslatorImpl();

			loader.addTranslator(pool, translator);
			pool.appendClassPath(classDir.getPath());
			pool.appendClassPath(testDir.getPath());

			// App loading
			loader.run("fr.istic.vv.Main", args);

			JUnitCore jUnitCore = new JUnitCore();

			for (CtClass ctClass : pool.get(testClasses)) {
				Request request = Request.aClass(ctClass.toClass());
				Result r = jUnitCore.run(request);
				logger.info("Tests ran : " + r.getRunCount() + ", failed : " + r.getFailureCount());
				logger.info("FAILURE : " + r.getFailures());
				logger.info("SUCCESS : " + r.wasSuccessful());
			}
		} catch (Throwable e) {
			logger.error("Oh, no! Something went wrong.", e);
		}

	}
}
