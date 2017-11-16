package fr.istic.vv.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.ClassPool;
import javassist.NotFoundException;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {

		// Path definitions
		String targetPath = "../VV-DUMMY-PROJET/target/";
		String jarFile = targetPath + "VV-DUMMY-PROJET-1.0-SNAPSHOT.jar";

		logger.info("Loading JAR file...");

		ClassPool pool = ClassPool.getDefault();
		try {
			// ClassPath definition
			pool.insertClassPath(jarFile);

		} catch (NotFoundException e) {
			logger.error("JAR file not found in : " + jarFile, e);
			return;
		}

		logger.info("Done.");

	}
}
