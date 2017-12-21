package fr.istic.vv.common;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ClassParserTest {

    ClassParser cp;

    @Before
    public void setUp() throws Exception {
        cp = new ClassParser();
    }

    @After
    public void tearDown() throws Exception {
        cp = null;
    }

    @Test
    public void getClassesFromNotExistingDirectory(){
        List<Class> classes = cp.getClassesFromDirectory("AA#B C&");
        assertEquals(classes.size(),0);
    }

    @Test
    public void getClassesFromCurrentDirectory(){
        String directory = System.getProperty("user.dir") + "/target/classes/";
        List<Class> classes = cp.getClassesFromDirectory(directory);
        assertTrue(classes.size()>0);
    }

    @Test
    public void getClassesFromCurrentDirectoryDirectPath(){
        String directory = System.getProperty("user.dir") + "/target/classes/fr/istic/vv/common/";
        List<Class> classes = cp.getClassesFromDirectory(directory);
        System.out.println(classes.size());
        assertEquals(classes.size(),0); //Because the recursion is not able to set fr.istic.vv.common package
    }

    @Test
    public void getClassesFromCurrentDirectoryDirectPathButNoClasses(){
        String directory = System.getProperty("user.dir") + "/target/sonar";
        List<Class> classes = cp.getClassesFromDirectory(directory);
        System.out.println(classes.size());
        assertEquals(classes.size(),0);
    }
}