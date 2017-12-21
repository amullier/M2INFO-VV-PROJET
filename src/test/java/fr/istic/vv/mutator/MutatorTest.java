package fr.istic.vv.mutator;

import fr.istic.vv.common.MutantContainer;
import fr.istic.vv.mutator.exception.MutatorException;
import fr.istic.vv.testrunner.exception.TestRunnerException;
import fr.istic.vv.testrunner.runner.TestRunner;
import fr.istic.vv.testrunner.runner.TestRunnerImpl;
import javassist.CtMethod;
import javassist.bytecode.MethodInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

public class MutatorTest {

    Mutator m;

    @Before
    public void setUp() throws Exception {
        m = new Mutator(null,null,null);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = MutatorException.class)
    public void mutatorWithNullClasses() throws Exception {
        m = new Mutator(null,null,null);
        m.mutate();
    }

    @Test(expected = MutatorException.class)
    public void mutatorWithNullTestRunner() throws Exception {
        m = new Mutator(new ArrayList<>(),null,null);
        m.mutate();
    }

    @Test(expected = MutatorException.class)
    public void mutatorWithNullClassPath() throws Exception {
        m = new Mutator(new ArrayList<>(),new TestRunnerImpl(),null);
        m.mutate();
    }

    @Test(expected = MutatorException.class)
    public void mutationNotFoundClass() throws Exception {
        List<Class> classes = new ArrayList<>();
        classes.add(String.class);

        m = new Mutator(classes ,new TestRunnerImpl(),"");
        m.mutate();
    }

    @Test
    public void createMutantContainer(){
        MutantContainer container = m.createMutantContainer("Class","method", MutantContainer.MutantType.ADDITION);
        assertNotNull(container);
    }

    @Test
    public void testMutationClass() throws MutatorException, TestRunnerException {
        List<Class> classList = new ArrayList<>();
        classList.add(TargetClassForTest.class);

        TestRunner testRunner = mock(TestRunner.class);
        m = new Mutator(classList,testRunner,System.getProperty("user.dir")+"/target/test-classes");

        m.mutate();
    }
}