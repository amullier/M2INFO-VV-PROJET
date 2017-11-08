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
		try {
			ClassPool pool = ClassPool.getDefault();

			Loader loader = new Loader(pool);
			Translator translator = new TranslatorImpl();
			File classDir = new File("../TargetProject/target/classes");
			File testDir = new File("../TargetProject/target/test-classes");
			loader.addTranslator(pool, translator);
			pool.appendClassPath(classDir.getPath());
			pool.appendClassPath(testDir.getPath());
			loader.run("fr.istic.vv.TargetApp", args);
			JUnitCore jUnitCore = new JUnitCore();
			String[] classes = { "fr.istic.vv.AdditionTest", "fr.istic.vv.MultiplicationTest",
					"fr.istic.vv.DivisionTest", "fr.istic.vv.SubtractionTest" };
			for (CtClass ctClass : pool.get(classes)) {
				Request request = Request.aClass(ctClass.toClass());
				Result r = jUnitCore.run(request);
				logger.info("Tests ran : " + r.getRunCount() + ", failed : " + r.getFailureCount());
				logger.info("FAILURE : " + r.getFailures());
				logger.info("SUCCESS : " + r.wasSuccessful());
			}

		}

		catch (Throwable e) {
			logger.info("Oh, no! Something went wrong.", e);
		}

	}
}
