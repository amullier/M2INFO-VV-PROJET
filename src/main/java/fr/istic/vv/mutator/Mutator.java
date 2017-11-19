package fr.istic.vv.mutator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.istic.vv.common.MutantContainer;
import fr.istic.vv.common.MutantContainerImpl;
import fr.istic.vv.mutator.projet.MutateClass;
import fr.istic.vv.mutator.projet.MutateMethod;
import fr.istic.vv.testrunner.exception.TestRunnerException;
import fr.istic.vv.testrunner.runner.TestRunner;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.DuplicateMemberException;
import javassist.bytecode.Mnemonic;

public class Mutator {

	private static Logger logger = LoggerFactory.getLogger(Mutator.class);
	private List<Class> classes;
	private TestRunner testRunner;
	
	public Mutator(List<Class> classes, TestRunner testRunner) {
		this.classes = classes;
		this.testRunner = testRunner;
	}
	
	public void mutate() throws Exception {
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
				
				if(!ctClass.isInterface()) {
					// faudrait utilisé le cf ??
					ClassFile cf = ctClass.getClassFile();
					
					Bytecode code = new Bytecode(cf.getConstPool());
					
					// construction de la class a muter ??????????????? pas très sur de son utilité ........
					MutateClass mc = new MutateClass(ctClass.getName());
					
					// get all methods that should be mutate
					CtMethod[] methods = ctClass.getDeclaredMethods();
					codeGeneration(code, mc, methods, cf);
					/**
					 * TODO: temporaire, pour l'instant je lance les tests une fois que tout les mutants ont été généré
					 */
					generateTestFromMutant(ctClass);
				}
			}
		}
		jar.close();
	}
	

	/**
	 * 
	 * @param code
	 * @param mc
	 * @param methods
	 * @throws BadBytecode
	 * @throws DuplicateMemberException 
	 */
	private void codeGeneration(Bytecode code, MutateClass mc, CtMethod[] methods, ClassFile cf) throws BadBytecode, DuplicateMemberException {
		for (CtMethod method : methods) {
			// construction des methods a muter
			MutateMethod mm = new MutateMethod(method.getName());
			
			CodeAttribute ca = method.getMethodInfo().getCodeAttribute();
			
			codeIterator(code, mc, mm, ca, cf);
			
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
	 * @throws BadBytecode
	 * @throws DuplicateMemberException 
	 */
	private void codeIterator(Bytecode code, MutateClass mc, MutateMethod mm, CodeAttribute ca, ClassFile cf) throws BadBytecode, DuplicateMemberException {
		if (ca != null) {
			CodeIterator ci = ca.iterator();
			while (ci.hasNext()) {
				int index = ci.next();
				int op = ci.byteAt(index);
				
				// au debut crée le meme code attribute
				mutateOp(cf, ci, index, op);				
				//
				mm.addBytecode(op);
				
			}
			
			// add the mutated method to the class ??????????????? pas très sur de son utilité ........
			mc.addMethods(mm);
			/**
			 * TODO: une fois la modif faite, crée la class puis lancé les tests
			 */
		}
	}
	
	/**
	 * 
	 * @param cf
	 * @param ci
	 * @param index
	 * @param op
	 */
	private void mutateOp(ClassFile cf, CodeIterator ci, int index, int op) {
		if (Mnemonic.OPCODE[op].toUpperCase().equals("DADD")) {
			Bytecode test = new Bytecode(cf.getConstPool());
			test.add(103);
			ci.write(test.get(), index);
		}
		else if (Mnemonic.OPCODE[op].toUpperCase().equals("DSUB")) {
			Bytecode test = new Bytecode(cf.getConstPool());
			test.add(99);
			ci.write(test.get(), index);
		} else if (Mnemonic.OPCODE[op].toUpperCase().equals("DMUL")) {
			Bytecode test = new Bytecode(cf.getConstPool());
			test.add(111);
			ci.write(test.get(), index);
		}
	}
	/**
	 * 
	 * @param ctClass
	 * @throws TestRunnerException
	 * @throws CannotCompileException
	 */
	private void generateTestFromMutant(CtClass ctClass) throws TestRunnerException, CannotCompileException {
		MutantContainer mutantContainer = createMutantContainer(ctClass);
		this.testRunner.setMutantContainer(mutantContainer);
		this.testRunner.execute();
	}
	/**
	 * 
	 * @param ctClass
	 * @return
	 * @throws CannotCompileException
	 */
	private MutantContainer createMutantContainer(CtClass ctClass) throws CannotCompileException {
		MutantContainer m = new MutantContainerImpl();
		m.setMutatedClass(ctClass.toClass());
		
		return m;
	}
	
}
//

/*
 * Pour le moment, on a une mutation total, bien sur il faut regler ce problem:
 * 1er solution: utilisé mutateClass et mutateMethod comme memento pour revenir en arriere a chaque fois qu'un mutant est généré
 * 
 * 2eme solution: on stock l'index où la mutation a été appliqué, on génère le mutant, on dégèle la class, puis on remet 
 * à l'index le code d'avant, puis on continue la génération du mutant
 * 
 * A faire : commencer par la seconde solution, plus simple a priori et voir si ça répond à ce qui est attendu
 */


//