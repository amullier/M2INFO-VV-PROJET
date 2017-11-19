package fr.istic.vv.mutator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.istic.vv.mutator.projet.MutateClass;
import fr.istic.vv.mutator.projet.MutateMethod;
import fr.istic.vv.testrunner.runner.TestRunner;
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
				mutateOp(code, op);
				
				
				//
				mm.addBytecode(op);
				
				//System.out.println("op : " + op + " - " + Mnemonic.OPCODE[op]);
				if (Mnemonic.OPCODE[op].equals("dadd")) { // TODO: géré quand c'est un iadd
					//System.out.println("c'est une addition");
				}
			}
			
			//System.out.println("le code copié :" + code.toString());
			
			// add the mutated method to the class ??????????????? pas très sur de son utilité ........
			mc.addMethods(mm);
			/**
			 * TODO: une fois la modif faite, crée la class puis lancé les tests
			 */
		}
	}
	
	/**
	 * If the op must be mutate, then b will be added to it with the mutated op
	 * @param b
	 * @param op
	 */
	private void mutateOp(Bytecode b, int op) {
		// ajout des tests pour savoir dans qu'elle operation on est
		b.add(op);
	}
	
}
