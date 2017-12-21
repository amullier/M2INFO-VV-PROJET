package fr.istic.vv.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MutantContainerImplTest {

    @Test
    public void getAndSet(){
        MutantContainer mc = new MutantContainerImpl();
        mc.setMutationType(MutantContainer.MutantType.ADDITION);
        mc.setMutationMethod("");
        mc.setMutatedClass("");
        assertEquals(mc.getMutatedClass(),"");
        assertEquals(mc.getMutationMethod(),"");
        assertNotNull(mc.getMutationType());
    }
}