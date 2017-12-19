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

/**
 * Mutator class take in charge bytecode modification and test launch with the TestRunner
 */
public class Mutator {

    /**
     * List of all possible mutations for mutation testing
     */
    public static final List<Mutation> mutations = MutationDefinition.getMutations();
    private static final Logger logger = LoggerFactory.getLogger(Mutator.class);
    private static final String PATH_DELIMITER = "/";

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
     * @throws Exception
     */
    public void mutate() throws Exception {
        //Classes crawl
        for (Class cl : classes) {
            //Get the class path file
            String classPath = classesPath + PATH_DELIMITER + cl.getName().replaceAll("\\.", "/") + ".class";
            logger.debug("Loading class for mutation : {}", classPath);

            CtClass ctClass;
            ClassPool cp = ClassPool.getDefault();
            ctClass = cp.makeClass(new FileInputStream(classPath));
            ctClass.stopPruning(true);

            //Running algorithm only for classes
            if (!ctClass.isInterface()) {
                ClassFile cf = ctClass.getClassFile();
                double progression  = ((double)indexMutationClass/(double)classes.size())*100;
                logger.info("[{}.{}%] Mutation testing on {}",(int)progression,(int) (progression - (int)(progression))*100, cf.getName());

                MutateClass mc = new MutateClass(ctClass.getName());

                //Gets all class methods to run mutation testing algorithm on them
                CtMethod[] methods = ctClass.getDeclaredMethods();
                mutationTestingForMethods(mc, methods, cf, ctClass);

                //Rewrite the file to undo file modifications
                ctClass.writeFile(classesPath);
                ctClass.defrost();
            }
            indexMutationClass++;
        }
    }


    /**
     * Method take the method list and run the mutation testing algorithm for each
     * @param mc
     * @param methods
     * @param cf
     * @param ctClass
     * @throws BadBytecode
     * @throws TestRunnerException
     * @throws CannotCompileException
     */
    private void mutationTestingForMethods(MutateClass mc, CtMethod[] methods, ClassFile cf, CtClass ctClass) throws CannotCompileException, TestRunnerException, BadBytecode {
        logger.info("{} methods might be mutated in the class : {}", methods.length, cf.getName());
        for (CtMethod method : methods) {
            // construction des methods a muter > je ne vois pas trop l'utilité pour l'instant, possible pour la description
            MutateMethod mm = new MutateMethod(method.getName());

            CodeAttribute ca = method.getMethodInfo().getCodeAttribute();

            mutationTestingForAMethod(mc, mm, ca, cf, ctClass, method);
        }
    }


    /**
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
    private void mutationTestingForAMethod(MutateClass mc, MutateMethod mm, CodeAttribute ca, ClassFile cf, CtClass ctClass, CtMethod method) throws BadBytecode, TestRunnerException, CannotCompileException {
        if (ca != null) {
            CodeIterator ci = ca.iterator();
            while (ci.hasNext()) {
                int index = ci.next();
                int op = ci.byteAt(index);

                mutateOp(cf, ci, index, op, ctClass, method);

                mm.addBytecode(op);

            }

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
                logger.info("#{} {}", indexMutation, mutation);
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
     * @param ctClass
     * @param baseCode
     * @param index
     * @param ci
     * @param cf
     * @param method
     * @throws CannotCompileException
     * @throws TestRunnerException
     */
    public void runTestsAndUndoMutation(CtClass ctClass, int baseCode, int index, CodeIterator ci, ClassFile cf, CtMethod method, MutantType m) throws CannotCompileException, TestRunnerException {

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
        MutantContainer mutantContainer = createMutantContainer(classMutant, method, m);
        this.testRunner.setMutantContainer(mutantContainer);
        logger.debug("Start TestRunner execute after mutation on {}", classMutant);
        this.testRunner.execute();
        logger.debug("TestRunner execution is finished", classMutant);
    }

    /**
     * Builder method for mutant container
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