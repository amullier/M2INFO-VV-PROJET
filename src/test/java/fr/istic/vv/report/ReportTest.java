package fr.istic.vv.report;

import fr.istic.vv.common.MutantContainerImpl;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReportTest {
    @Test
    public void gettersAndSetters() throws Exception {
        Report r = new Report(true,null);
        r.setMutantContainer(new MutantContainerImpl());
        r.setMutantAlive(false);
        assertEquals(r.isMutantAlive(),false);
        assertNotNull(r.getMutantContainer());
    }
}