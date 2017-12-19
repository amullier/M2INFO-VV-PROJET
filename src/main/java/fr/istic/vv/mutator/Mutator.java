package fr.istic.vv.mutator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.istic.vv.common.MutantContainer;
import fr.istic.vv.common.MutantContainer.MutantType;
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
import javassist.bytecode.Mnemonic;

public class Mutator {

    private static Logger logger = LoggerFactory.getLogger(Mutator.class);
    private List<Class> classes;
    private TestRunner testRunner;
    private String classesPath;

    private static List<Mutation> mutations = MutationDefinition.getMutations();

    public Mutator(List<Class> classes, TestRunner testRunner, String classesPath) {
        this.classes = classes;
        this.testRunner = testRunner;
        this.classesPath = classesPath;
    }

    /**
     * @throws Exception
     */
    public void mutate() throws Exception {

        for (Class cl : classes) {
            CtClass ctClass;
            ClassPool cp = ClassPool.getDefault();
            String classPath = classesPath + "/" + cl.getName().replaceAll("\\.", "/") + ".class";
            logger.debug("Loading class for mutation : {}", classPath);
            ctClass = cp.makeClass(new FileInputStream(classPath));
            ctClass.stopPruning(true);
            if (!ctClass.isInterface()) {
                ClassFile cf = ctClass.getClassFile();
                logger.debug("ClassFile might be mutated : {}", cf.getName());

                Bytecode code = new Bytecode(cf.getConstPool());

                // construction de la class a muter > je ne vois pas trop l'utilité pour l'instant, possible pour la description
                MutateClass mc = new MutateClass(ctClass.getName());

                // get all methods that should be mutate
                CtMethod[] methods = ctClass.getDeclaredMethods();
                codeGeneration(code, mc, methods, cf, ctClass);

                // remettre les modifs
                ctClass.writeFile(classesPath);
                ctClass.defrost();
            }
        }
    }


    /**
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
        logger.debug("{} methods might be mutated in the class : {}", methods.length, cf.getName());
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
        for (Mutation mutation : mutations) {
            if (Mnemonic.OPCODE[op].equalsIgnoreCase(mutation.getTargetOperation())) {
                logger.debug("{}", mutation);
                Bytecode bytecode = new Bytecode(cf.getConstPool());
                bytecode.add(mutation.getMutationOperationCode());
                ci.write(bytecode.get(), index);

                //Writing file into the physical file
                try {
                    logger.info("Writing class file : {}", ctClass.getClassFile().getName());
                    ctClass.writeFile(classesPath);
                    ctClass.defrost();
                } catch (IOException e) {
                    logger.warn("An error occurred during mutated class writing", e);
                }

                generateMutantClassTestItAndUndo(ctClass, mutation.getTargetOperationCode(), index, ci, cf, method, mutation.getMutationType());
            }
        }
    }

    /**
     * @param ctClass
     * @param baseCode
     * @param index
     * @param ci
     * @param cf
     * @param method
     * @throws CannotCompileException
     * @throws TestRunnerException
     */
    public void generateMutantClassTestItAndUndo(CtClass ctClass, int baseCode, int index, CodeIterator ci, ClassFile cf, CtMethod method, MutantType m) throws CannotCompileException, TestRunnerException {
        // on génère le mutant et on lance les tests
        //Class classMutant = ctClass.toClass();
        //generateTestForMutant(classMutant, method, m);
        //ctClass.writeFile(classesPath);
        generateTestForMutant(ctClass.getName(), method, m);
        ctClass.defrost();
        // on revient en arrière
        Bytecode baseMutant = new Bytecode(cf.getConstPool());
        baseMutant.add(baseCode);
        ci.write(baseMutant.get(), index);
    }

    /**
     * Creates a mutant container (to transfer information to TestRunner) and launch test with TestRunner
     *
     * @param classMutant
     * @param method
     * @throws TestRunnerException
     * @throws CannotCompileException
     */
    private void generateTestForMutant(String classMutant, CtMethod method, MutantType m) throws TestRunnerException {
        MutantContainer mutantContainer = createMutantContainer(classMutant, method, m);
        this.testRunner.setMutantContainer(mutantContainer);
        logger.debug("Start TestRunner execute after mutation on {}", classMutant);
        this.testRunner.execute();
        logger.debug("TestRunner execution is finished", classMutant);
    }

    /**
     * @param classMutant
     * @return
     * @throws CannotCompileException
     */
    private MutantContainer createMutantContainer(String classMutant, CtMethod method, MutantType mt) {
        logger.trace("Creating a mutant container");
        MutantContainer m = new MutantContainerImpl();
        m.setMutatedClass(classMutant);
        m.setMutationMethod(method.getName());
        m.setMutationType(mt);
        return m;
    }

}