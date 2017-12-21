package fr.istic.vv.common;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;

public class StringUtilsTest {

    private static final Logger logger = LoggerFactory.getLogger(StringUtilsTest.class);

    @Test
    public void numberMaximalOfCharacter(){
        double val  = 0.611610;

        String res = StringUtils.percentage(val);
        String[] parts = res.split("\\.");
        System.out.println(res);

        assertTrue(parts[0].length() == 2);
        assertTrue(parts[1].length() == 2);
    }

    @Test
    public void numberMaximal(){
        double val  = 1;

        String res = StringUtils.percentage(val);
        String[] parts = res.split("\\.");
        System.out.println(res);

        assertTrue(parts[0].equalsIgnoreCase("100"));
    }

    @Test
    public void numberMinimal(){
        double val  = 0;

        String res = StringUtils.percentage(val);
        String[] parts = res.split("\\.");
        System.out.println(res);

        assertTrue(parts[0].equalsIgnoreCase(" 0"));
        assertTrue(parts[1].equalsIgnoreCase("0 "));
    }

    @Test
    public void numberOutOfBounds(){
        double val  = -1.5;

        String res = StringUtils.percentage(val);
        res.equalsIgnoreCase("-150");
    }

    @Test
    public void numberOutOfBoundsPositive(){
        double val  = 1.2;

        String res = StringUtils.percentage(val);
        res.equalsIgnoreCase("120");
    }

}