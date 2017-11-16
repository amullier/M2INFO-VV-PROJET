package fr.istic.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.Mnemonic;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Throwable {

		String jarFile = "../M2INFO-VV-DUMMY-PROJET/target/VV-DUMMY-PROJET-1.0-SNAPSHOT.jar";
		JarFile jar = new JarFile(jarFile);

		Enumeration<JarEntry> jarEntries = jar.entries();

		while (jarEntries.hasMoreElements()) {
			final JarEntry jarEntry = jarEntries.nextElement();

			if (jarEntry.getName().endsWith(".class")) {
				InputStream is = null;
				CtClass ctClass = null;
				try {
					is = jar.getInputStream(jarEntry);
					ClassPool cp = ClassPool.getDefault();
					ctClass = cp.makeClass(is);
				} catch (IOException ioex1) {
					throw new Exception("Could not load class from JAR entry [" + jarEntry.getName() + "].");
				} finally {
					try {
						if (is != null)
							is.close();
					} catch (IOException ignored) {
					}
				}
				System.out.println(ctClass.getName());
				ClassFile cf = new ClassFile(false, ctClass.getName(), null);
				CtMethod[] methods = ctClass.getDeclaredMethods();
				for (CtMethod method : methods) {
					System.out.println(method.getName());
					CodeAttribute ca = method.getMethodInfo().getCodeAttribute();
					if (ca != null) {
						CodeIterator ci = ca.iterator();
						while (ci.hasNext()) {
							int index = ci.next();
							int op = ci.byteAt(index);
							System.out.println("op : " + op + " - " + Mnemonic.OPCODE[op]);
							if (Mnemonic.OPCODE[op].equals("dadd")) { // TODO: géré quand c'est un iadd
								System.out.println("c'est une addition");
							}
						}
					}
				}
			}
		}

		// // Path definitions
		// File classDir = new File("../VV-DUMMY-PROJET/target/classes");
		// File testDir = new File("../VV-DUMMY-PROJET/target/test-classes");
		//
		// // Test classes definitions
		// String testPackage = "fr.istic.vv.operations.";
		// String[] testClasses = { testPackage + "PlusTest", testPackage + "MinusTest",
		// testPackage + "TimesTest",
		// testPackage + "DivTest" };
		//
		// try {
		// ClassPool pool = ClassPool.getDefault();
		//
		// Loader loader = new Loader(pool);
		// Translator translator = new TranslatorImpl();
		//
		// loader.addTranslator(pool, translator);
		//
		// // FIXME : classpath a fixer
		// pool.appendClassPath(classDir.getPath());
		// pool.appendClassPath(testDir.getPath());
		//
		// // App loading
		// loader.run("fr.istic.vv.Main", args);
		//
		// JUnitCore jUnitCore = new JUnitCore();
		//
		// for (CtClass ctClass : pool.get(testClasses)) {
		// Request request = Request.aClass(ctClass.toClass());
		// Result r = jUnitCore.run(request);
		// logger.info("Tests ran : {}, failed : {}", r.getRunCount(),
		// r.getFailureCount());
		// logger.info("FAILURE : " + r.getFailures());
		// logger.info("SUCCESS : " + r.wasSuccessful());
		// }
		// } catch (Throwable e) {
		// logger.error("Oh, no! Something went wrong.", e);
		// }

	}
}
