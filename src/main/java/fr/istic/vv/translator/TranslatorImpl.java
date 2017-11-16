package fr.istic.vv.translator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.istic.vv.main.Main;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.Translator;
import javassist.bytecode.Opcode;

public class TranslatorImpl implements Translator {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public void start(ClassPool classPool) throws NotFoundException, CannotCompileException {
		logger.info("Starting");
	}

	public void onLoad(ClassPool classPool, String className) throws NotFoundException, CannotCompileException {

		logger.debug("CLASSNAME : " + className);
		if (className.contains("Addition")) {
			CtMethod operate = classPool.get(className).getDeclaredMethod("operate");
			// operate.setBody("return FirstTerm - SecondTerm;");
		}

	}
}