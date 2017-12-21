package fr.istic.vv.testrunner.runner;

import fr.istic.vv.common.MutantContainer;
import fr.istic.vv.common.MutantContainerImpl;
import fr.istic.vv.report.ReportServiceImpl;
import fr.istic.vv.testrunner.exception.TestRunnerException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class TestRunnerImplTest {

    TestRunnerImpl testRunner;

    @Before
    public void setUp(){
        testRunner = new TestRunnerImpl();
    }

    @After
    public void tearDown(){
        testRunner = null;
    }

    @Test(expected = TestRunnerException.class)
    public void executionWithOutClassesAndMutantFailed() throws TestRunnerException {
        testRunner.execute();
    }

    @Test(expected = TestRunnerException.class)
    public void executionWithOutClasses() throws TestRunnerException {
        MutantContainer mutantContainer = Mockito.mock(MutantContainer.class);
        testRunner.setMutantContainer(mutantContainer);
        assertNotNull(testRunner.getMutantContainer());
        testRunner.execute();
    }

    @Test(expected = TestRunnerException.class)
    public void executionWithOutMutant() throws TestRunnerException {
        Class class1 = Mockito.any(Class.class);
        Class class2 = Mockito.any(Class.class);
        Class class3 = Mockito.any(Class.class);

        List<Class> classList = new ArrayList<>();
        classList.add(class1);
        classList.add(class2);
        testRunner.setClasses(classList);

        List<Class> testClassList = new ArrayList<>();
        testClassList.add(class3);
        testRunner.setTestClasses(testClassList);

        assertNotNull(testRunner.getClasses());
        assertNotNull(testRunner.getTestClasses());
        testRunner.execute();
    }

    @Test(expected = TestRunnerException.class)
    public void executionWithoutPath() throws TestRunnerException{
        Class class1 = String.class;
        Class class2 = List.class;
        Class class3 = Class.class;

        List<Class> classList = new ArrayList<>();
        classList.add(class1);
        classList.add(class2);
        testRunner.setClasses(classList);

        List<Class> testClassList = new ArrayList<>();
        testClassList.add(class3);
        testRunner.setTestClasses(testClassList);

        MutantContainer container = new MutantContainerImpl();
        container.setMutatedClass("class");
        container.setMutationMethod("method");
        container.setMutationType(MutantContainer.MutantType.ADDITION);
        testRunner.setMutantContainer(container);

        assertNotNull(testRunner.getClasses());
        assertNotNull(testRunner.getTestClasses());
        testRunner.execute();
    }

    @Test
    public void validExecution() throws TestRunnerException{
        Class class1 = String.class;
        Class class2 = List.class;
        Class class3 = Class.class;

        List<Class> classList = new ArrayList<>();
        classList.add(class1);
        classList.add(class2);
        testRunner.setClasses(classList);

        List<Class> testClassList = new ArrayList<>();
        testClassList.add(class3);
        testRunner.setTestClasses(testClassList);

        MutantContainer container = new MutantContainerImpl();
        container.setMutatedClass("class");
        container.setMutationMethod("method");
        container.setMutationType(MutantContainer.MutantType.ADDITION);
        testRunner.setMutantContainer(container);

        testRunner.setRootProjectPath("/tmp");
        testRunner.setReportService(new ReportServiceImpl());

        assertNotNull(testRunner.getClasses());
        assertNotNull(testRunner.getTestClasses());
        testRunner.execute();
    }
}