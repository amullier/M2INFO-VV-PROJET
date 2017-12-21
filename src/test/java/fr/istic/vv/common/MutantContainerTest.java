package fr.istic.vv.common;

import org.junit.Test;

import static org.junit.Assert.*;

public class MutantContainerTest {

    @Test
    public void enumText(){
        for(MutantContainer.MutantType m : MutantContainer.MutantType.values()){
            assertTrue(m.toString().length()>0);
        }
    }

}