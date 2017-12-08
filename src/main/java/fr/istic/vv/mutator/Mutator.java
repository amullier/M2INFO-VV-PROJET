package fr.istic.vv.mutator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.plaf.synth.SynthSeparatorUI;

import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.istic.vv.common.MutantContainer;
import fr.istic.vv.common.MutantContainer.MutantType;
import fr.istic.vv.common.MutantContainerImpl;
import fr.istic.vv.mutator.projet.MutateClass;
import fr.istic.vv.mutator.projet.MutateMethod;
import fr.istic.vv.testrunner.exception.TestRunnerException;
import fr.istic.vv.testrunner.runner.TestRunner;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.Mnemonic;

public class Mutator {

	private static Logger logger = LoggerFactory.getLogger(Mutator.class);
	private List<Class> classes;
	private TestRunner testRunner;
	private String classesPath;
	
	public Mutator(List<Class> classes, TestRunner testRunner, String classesPath) {
		this.classes = classes;
		this.testRunner = testRunner;
		this.classesPath = classesPath;
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void mutate() throws Exception {
//		String jarFile = "../M2INFO-VV-DUMMY-PROJET/target/VV-DUMMY-PROJET-1.0-SNAPSHOT.jar";
//		JarFile jar = new JarFile(jarFile);
//		
//		Enumeration<JarEntry> jarEntries = jar.entries();

//		while (jarEntries.hasMoreElements()) {
//			final JarEntry jarEntry = jarEntries.nextElement();
//
//			if (jarEntry.getName().endsWith(".class")) {
//				InputStream is = null;
//				CtClass ctClass = null;
//				try {
//					is = jar.getInputStream(jarEntry);
//					ClassPool cp = ClassPool.getDefault();
//					ctClass = cp.makeClass(is);
//					ctClass.stopPruning(true);
//				} catch (IOException ioex1) {
//					throw new Exception("Could not load class from JAR entry [" + jarEntry.getName() + "].");
//				} finally {
//					try {
//						if (is != null)
//							is.close();
//					} catch (IOException ignored) {
//					}
//				}
		for(Class cl : classes) {
			CtClass ctClass;
			ClassPool cp = ClassPool.getDefault();
			String classPath = classesPath + "/" + cl.getName().replaceAll("\\.","/") + ".class";
			logger.debug("Loading class for mutation : {}",classPath);
			ctClass = cp.makeClass(new FileInputStream(classPath));
			ctClass.stopPruning(true);
				if(!ctClass.isInterface()) {
					ClassFile cf = ctClass.getClassFile();
					logger.debug("[Mutator]ClassFile might be mutated : {}", cf);
					
					Bytecode code = new Bytecode(cf.getConstPool());
					
					// construction de la class a muter > je ne vois pas trop l'utilité pour l'instant, possible pour la description
					MutateClass mc = new MutateClass(ctClass.getName());
					
					// get all methods that should be mutate
					CtMethod[] methods = ctClass.getDeclaredMethods();
					codeGeneration(code, mc, methods, cf, ctClass);
				}
			}
		}
		// jar.close();
	//}
	


	/**
	 * 
	 * @param code
	 * @param mc
	 * @param methods
	 * @param cf
	 * @param ctClass
	 * @throws BadBytecode
	 * @throws TestRunnerException
	 * @throws CannotCompileException
	 */
	private void codeGeneration(Bytecode code, MutateClass mc, CtMethod[] methods, ClassFile cf, CtClass ctClass) throws BadBytecode, TestRunnerException, CannotCompileException {
		logger.debug("[Mutator]methods might be mutated : {}", methods);
		for (CtMethod method : methods) {
			// construction des methods a muter > je ne vois pas trop l'utilité pour l'instant, possible pour la description
			MutateMethod mm = new MutateMethod(method.getName());
			
			CodeAttribute ca = method.getMethodInfo().getCodeAttribute();
			
			codeIterator(code, mc, mm, ca, cf, ctClass, method);
			
			// on remet le code a zero
			code = new Bytecode(cf.getConstPool());
		}
	}


	/**
	 * 
	 * @param code
	 * @param mc
	 * @param mm
	 * @param ca
	 * @param cf
	 * @param ctClass
	 * @param method
	 * @throws BadBytecode
	 * @throws TestRunnerException
	 * @throws CannotCompileException
	 */
	private void codeIterator(Bytecode code, MutateClass mc, MutateMethod mm, CodeAttribute ca, ClassFile cf, CtClass ctClass, CtMethod method) throws BadBytecode, TestRunnerException, CannotCompileException {
		if (ca != null) {
			CodeIterator ci = ca.iterator();
			while (ci.hasNext()) {
				int index = ci.next();
				int op = ci.byteAt(index);
				
				mutateOp(cf, ci, index, op, ctClass, method);				

				mm.addBytecode(op);
				
			}
			
			// add the mutated method to the class > je ne vois pas trop l'utilité pour l'instant, possible pour la description
			mc.addMethods(mm);
		}
	}
	
	/**
	 * 
	 * @param cf
	 * @param ci
	 * @param index
	 * @param op
	 * @param ctClass
	 * @param method
	 * @throws TestRunnerException
	 * @throws CannotCompileException
	 */
	private void mutateOp(ClassFile cf, CodeIterator ci, int index, int op, CtClass ctClass, CtMethod method) throws TestRunnerException, CannotCompileException {
		if (Mnemonic.OPCODE[op].toUpperCase().equals("DADD")) {
			Bytecode mutantCode = new Bytecode(cf.getConstPool());
			mutantCode.add(103);
			ci.write(mutantCode.get(), index);
			
			generateMutantClassTestItAndUndo(ctClass, 99, index, ci, cf, method, MutantType.ADDITION);
		}
		else if (Mnemonic.OPCODE[op].toUpperCase().equals("DSUB")) {
			Bytecode test = new Bytecode(cf.getConstPool());
			test.add(99);
			ci.write(test.get(), index);
			
			generateMutantClassTestItAndUndo(ctClass, 103, index, ci, cf, method, MutantType.SUBTRACTION);
			
		} 
		else if (Mnemonic.OPCODE[op].toUpperCase().equals("DMUL")) {
			Bytecode test = new Bytecode(cf.getConstPool());
			test.add(111);
			ci.write(test.get(), index);
			
			generateMutantClassTestItAndUndo(ctClass, 107, index, ci, cf, method, MutantType.MULTIPLICATION);
		} 
		else if (Mnemonic.OPCODE[op].toUpperCase().equals("DDIV")) {
			Bytecode test = new Bytecode(cf.getConstPool());
			test.add(107);
			ci.write(test.get(), index);
			
			generateMutantClassTestItAndUndo(ctClass, 111, index, ci, cf, method, MutantType.DIVISION);
		}
	}

	/**
	 * 
	 * @param ctClass
	 * @param baseCode
	 * @param index
	 * @param ci
	 * @param cf
	 * @param method
	 * @throws CannotCompileException
	 * @throws TestRunnerException
	 */
	private void generateMutantClassTestItAndUndo(CtClass ctClass, int baseCode, int index, CodeIterator ci, ClassFile cf, CtMethod method, MutantType m) throws CannotCompileException, TestRunnerException {
		// on génère le mutant et on lance les tests
		try {
			ctClass.writeFile("/tmp/test2");
			logger.debug("Writing class {}",ctClass);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//	Class<?> classMutant = ctClass.toClass();
	//	generateTestFromMutant(classMutant, method, m);
		ctClass.defrost();
		
		// on revient en arrière
		Bytecode baseMutant = new Bytecode(cf.getConstPool());
		baseMutant.add(baseCode);
		ci.write(baseMutant.get(), index);
	}
	
	/**
	 * ici les parametres servent a construire des informations utils lors des tests, comme quelle class a été muter ou quelle methode
	 * @param classMutant
	 * @param method
	 * @throws TestRunnerException
	 * @throws CannotCompileException
	 */
	private void generateTestFromMutant(Class<?> classMutant, CtMethod method, MutantType m) throws TestRunnerException, CannotCompileException {
		MutantContainer mutantContainer = createMutantContainer(classMutant, method, m);
		this.testRunner.setMutantContainer(mutantContainer);
		logger.debug("[Mutator]Start TestRunner on : {}", classMutant);
		this.testRunner.execute();
		logger.debug("[Mutator]End TestRunner on : {}", classMutant);
	}
	/**
	 * 
	 * @param classMutant
	 * @return
	 * @throws CannotCompileException
	 */
	private MutantContainer createMutantContainer(Class classMutant, CtMethod method, MutantType mt) throws CannotCompileException {
		MutantContainer m = new MutantContainerImpl();
		m.setMutatedClass(classMutant);
		m.setMutationMethod(method.getName());
		m.setMutationType(mt);
		return m;
	}
	
}
//

/*
 * TODO:
 * Pour le moment, on a une mutation total, bien sur il faut regler ce problem:
 * 1er solution: utilisé mutateClass et mutateMethod comme memento pour revenir en arriere a chaque fois qu'un mutant est généré
 * 
 * 2eme solution: on stock l'index où la mutation a été appliqué, on génère le mutant, on /!\dégèle/!\ la class, puis on remet 
 * à l'index le code d'avant, puis on continue la génération du mutant
 * 
 * A faire : commencer par la seconde solution, plus simple a priori et voir si ça répond à ce qui est attendu
 * 
 * C'est fait
 * /!\/!\
 */


//