package fr.istic.vv.mutator;

import fr.istic.vv.common.MutantContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MutationTest {

    Mutation m;

    @Before
    public void setUp() throws Exception {
        m = new Mutation("a",12,"aa",13, MutantContainer.MutantType.ADDITION);
    }

    @After
    public void tearDown() throws Exception {
        m=null;
    }

    @Test
    public void gettersAndSetters(){
        assertNotNull(m.getTargetOperation());
        assertNotNull(m.getTargetOperationCode());
        assertNotNull(m.getMutationOperationCode());
        assertNotNull(m.getMutationType());
    }


    @Test
    public void toStringTest() {
        Mutation m = new Mutation("a",12,"aa",13, MutantContainer.MutantType.ADDITION);
        assertTrue(m.toString().length()>0);
    }

}