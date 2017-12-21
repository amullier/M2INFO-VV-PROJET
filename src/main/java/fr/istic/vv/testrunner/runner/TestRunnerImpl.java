package fr.istic.vv.testrunner.runner;

import fr.istic.vv.common.MutantContainer;
import fr.istic.vv.mutator.Mutator;
import fr.istic.vv.report.Report;
import fr.istic.vv.report.ReportService;
import fr.istic.vv.testrunner.exception.TestRunnerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * TestRunner is in charge to run tests on the target project
 * defined in the Main
 * <p>
 * The execute() call is sent by the Mutator
 */
public class TestRunnerImpl implements TestRunner {

    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);

    private List<Class> classes;

    private List<Class<?>> testClasses;

    private MutantContainer mutantContainer;

    private ReportService reportService;

    private String rootProjectPath;

    /**
     * Constructor instantiates list classes
     */
    public TestRunnerImpl() {
        classes = new ArrayList<>();
        testClasses = new ArrayList<>();
    }

    public List<Class> getClasses() {
        return classes;
    }

    @Override
    public void setClasses(List<Class> classes) {
        for (Class classString : classes) {
            addClass(classString);
        }
        if (classes.isEmpty()) {
            logger.warn("No classes loaded during this setter call");
        } else {
            logger.debug("{} classes are loaded in TestRunner", classes.size());
        }
    }

    private void addClass(Class clazz) {
        if (classes == null) {
            classes = new ArrayList<>();
        }
        logger.trace("Adding {} to TestRunner classes collection", clazz);
        classes.add(clazz);
    }

    public List<Class<?>> getTestClasses() {
        return testClasses;
    }

    @Override
    public void setTestClasses(List<Class> testClasses) {
        for (Class testClass : testClasses) {
            addTestClass(testClass);
        }
        if (testClasses.size() == 0) {
            logger.warn("No test classes loaded during this setter call");
        } else {
            logger.debug("{} test classes are loaded in TestRunner", classes.size());
        }
    }

    private void addTestClass(Class testClass) {
        if (testClasses == null) {
            testClasses = new ArrayList<>();
        }
        testClasses.add(testClass);
    }

    public MutantContainer getMutantContainer() {
        return mutantContainer;
    }

    @Override
    public void setMutantContainer(MutantContainer mutantContainer) {
        this.mutantContainer = mutantContainer;
    }

    @Override
    public void setReportService(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public void setRootProjectPath(String rootProjectPath) {
        this.rootProjectPath = rootProjectPath;
    }

    @Override
    public void execute() throws TestRunnerException {
        verifyTestRunnerForExecution();

        runTest();
    }

    private void verifyTestRunnerForExecution() throws TestRunnerException {
        logger.trace("TestRunner checking");
        if (classes == null || classes.isEmpty()) {
            throw new TestRunnerException("Project classes are not in TestRunner");
        }
        if (testClasses == null || testClasses.isEmpty()) {
            throw new TestRunnerException("Project test classes are not in TestRunner");
        }
        if (mutantContainer == null) {
            throw new TestRunnerException("Mutated class is not in TestRunner");
        }

        logger.debug("Verification OK for running tests on project.");
    }

    /**
     * Run a test class with the mutated class
     */
    private void runTest() throws TestRunnerException {
        logger.debug("Starting testing with MAVEN on {}", rootProjectPath);
        try {
            ProcessBuilder ps = new ProcessBuilder("mvn", "surefire:test");
            ps.redirectErrorStream(true);
            if (rootProjectPath.substring(0, 1).equalsIgnoreCase(".")) {
                ps.directory(new File(System.getProperty("user.dir") + Mutator.PATH_DELIMITER + rootProjectPath));
            } else {
                ps.directory(new File(rootProjectPath));
            }

            Process process = ps.start();

            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                logger.trace(line);
            }

            process.waitFor();
            in.close();

            int returnValue = process.exitValue(); //This block the execution but it is expected
            reportService.addReport(new Report(returnValue == 0, mutantContainer));

        } catch (IOException | InterruptedException e) {
            logger.warn("An error occurred during testing", e);
            throw new TestRunnerException("An error occurred during testing");
        }
    }

}
