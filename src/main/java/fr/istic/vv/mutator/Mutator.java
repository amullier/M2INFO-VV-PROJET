package fr.istic.vv.mutator;

import fr.istic.vv.common.MutantContainer;
import fr.istic.vv.common.MutantContainer.MutantType;
import fr.istic.vv.common.MutantContainerImpl;
import fr.istic.vv.mutator.exception.MutatorException;
import fr.istic.vv.testrunner.exception.TestRunnerException;
import fr.istic.vv.testrunner.runner.TestRunner;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Mutator class take in charge bytecode modification and test launch with the TestRunner
 */
public class Mutator {

    /**
     * List of all possible mutations for mutation testing
     */
    private static final List<Mutation> mutations = MutationDefinition.getMutations();
    private static final Logger logger = LoggerFactory.getLogger(Mutator.class);
    public static final String PATH_DELIMITER = "/";

    private List<Class> classes;
    private TestRunner testRunner;
    private String classesPath;
    private int indexMutation;
    private int indexMutationClass;

    public Mutator(List<Class> classes, TestRunner testRunner, String classesPath) {
        this.classes = classes;
        this.testRunner = testRunner;
        this.classesPath = classesPath;
    }

    /**
     * Start the mutation testing on the project defined by Mutator attributes
     *
     * @throws MutatorException
     */
    public void mutate() throws MutatorException, TestRunnerException {
        if(classes==null){
            logger.error("Classes are not set correctly in Mutator");
            throw new MutatorException("Classes are not set correctly in Mutator");
        }

        if(testRunner==null){
            logger.error("Test runner is not defined in Mutator");
            throw new MutatorException("Test runner is not defined in Mutator");
        }

        if(classesPath==null){
            logger.error("Classes path is not defined in Mutator");
            throw new MutatorException("Classes path is not defined in Mutator");
        }

        //Classes crawl
        for (Class cl : classes) {
            //Get the class path file
            String classPath = classesPath + PATH_DELIMITER + cl.getName().replaceAll("\\.", "/") + ".class";
            logger.debug("Loading class for mutation : {}", classPath);

            CtClass ctClass;
            ClassPool cp = ClassPool.getDefault();
            try {
                ctClass = cp.makeClass(new FileInputStream(classPath));
                ctClass.stopPruning(true);

                if (!ctClass.isInterface()) {
                    ClassFile cf = ctClass.getClassFile();
                    double progression = ((double) indexMutationClass / (double) classes.size()) * 100;
                    logger.info("[{}.{}%] Mutation testing on {}", (int) progression, (int) (progression - (int) (progression)) * 100, cf.getName());

                    //Gets all class methods to run mutation testing algorithm on them
                    CtMethod[] methods = ctClass.getDeclaredMethods();
                    mutationTestingForMethods(methods, cf, ctClass);

                    //Rewrite the file to undo file modifications
                    ctClass.writeFile(classesPath);
                    ctClass.defrost();
                }
                indexMutationClass++;

            } catch (IOException e) {
                logger.error("An error occurred during file reading", e);
                throw new MutatorException("File reading error");
            } catch (BadBytecode | CannotCompileException e) {
                logger.error("An error occurred during bytecode modification", e);
                throw new MutatorException("An error occurred during bytecode modification");
            } catch (TestRunnerException e) {
                logger.error("An error occurred during project test running with a mutant", e);
                throw e;
            }
        }
    }


    /**
     * Method take the method list and run the mutation testing algorithm for each
     *
     * @param methods
     * @param cf
     * @param ctClass
     * @throws BadBytecode
     * @throws TestRunnerException
     * @throws CannotCompileException
     */
    private void mutationTestingForMethods(CtMethod[] methods, ClassFile cf, CtClass ctClass) throws CannotCompileException, TestRunnerException, BadBytecode {
        logger.info("\t {} methods might be mutated in the class : {}", methods.length, cf.getName());
        for (CtMethod method : methods) {
            CodeAttribute ca = method.getMethodInfo().getCodeAttribute();

            mutationTestingForAMethod(ca, cf, ctClass, method);
        }
    }


    /**
     * @param ca
     * @param cf
     * @param ctClass
     * @param method
     * @throws BadBytecode
     * @throws TestRunnerException
     * @throws CannotCompileException
     */
    private void mutationTestingForAMethod(CodeAttribute ca, ClassFile cf, CtClass ctClass, CtMethod method) throws BadBytecode, TestRunnerException, CannotCompileException {
        if (ca != null) {
            CodeIterator ci = ca.iterator();
            while (ci.hasNext()) {
                int index = ci.next();
                int op = ci.byteAt(index);

                mutateOp(cf, ci, index, op, ctClass, method);

            }
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
                logger.info("\t\t#{} {}", indexMutation, mutation);
                indexMutation++;
                Bytecode bytecode = new Bytecode(cf.getConstPool());
                bytecode.add(mutation.getMutationOperationCode());
                ci.write(bytecode.get(), index);

                //Writing file into the physical file
                try {
                    logger.debug("Writing class file : {}", ctClass.getClassFile().getName());
                    ctClass.writeFile(classesPath);
                    ctClass.defrost();
                } catch (IOException e) {
                    logger.warn("An error occurred during mutated class writing", e);
                }

                runTestsAndUndoMutation(ctClass, mutation.getTargetOperationCode(), index, ci, cf, method, mutation.getMutationType());
            }
        }
    }

    /**
     * Method run test with Test Runner execution call and it undo the mutation transformation
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
    private void runTestsAndUndoMutation(CtClass ctClass, int baseCode, int index, CodeIterator ci, ClassFile cf, CtMethod method, MutantType m) throws TestRunnerException {

        runTest(ctClass.getName(), method, m);
        ctClass.defrost();

        //Perform the undo
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
    private void runTest(String classMutant, CtMethod method, MutantType m) throws TestRunnerException {
        MutantContainer mutantContainer = createMutantContainer(classMutant, method.getName(), m);
        this.testRunner.setMutantContainer(mutantContainer);
        logger.debug("Start TestRunner execute after mutation on {}", classMutant);
        this.testRunner.execute();
        logger.debug("TestRunner execution is finished", classMutant);
    }

    /**
     * Builder method for mutant container
     *
     * @param classMutant
     * @return
     * @throws CannotCompileException
     */
    public MutantContainer createMutantContainer(String classMutant, String method, MutantType mt) {
        logger.trace("Creating a mutant container");
        MutantContainer m = new MutantContainerImpl();
        m.setMutatedClass(classMutant);
        m.setMutationMethod(method);
        m.setMutationType(mt);
        return m;
    }

}