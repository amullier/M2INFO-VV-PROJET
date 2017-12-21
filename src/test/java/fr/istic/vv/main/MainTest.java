package fr.istic.vv.main;

import org.junit.Test;

public class MainTest {

    @Test
    public void testMainWithOutArguments(){
        Main.main(null);
    }

    @Test
    public void testMainWithArguments(){
        String[] args = new String[1];
        args[0] = "/tmp/a";
        Main.main(args);
    }

}