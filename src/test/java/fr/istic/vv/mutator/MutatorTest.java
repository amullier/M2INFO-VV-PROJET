package fr.istic.vv.mutator;

import fr.istic.vv.mutator.exception.MutatorException;
import fr.istic.vv.testrunner.runner.TestRunnerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MutatorTest {

    Mutator m;

    @Before
    public void setUp() throws Exception {
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

}