package fr.istic.domain.operations;

import fr.istic.domain.Operation;
import fr.istic.domain.OperationException;
import fr.istic.domain.operations.Div;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;

public class DivTest {

    Operation div;

    private static Logger logger = Logger.getGlobal();

    @Before
    public void setUp(){
        div = new Div();
    }

    @After
    public void tearDown(){
        div = null;
    }

    @Test
    public void testDivisionWithPositiveInteger(){
        try {
            logger.info("execute : 1/2");
            double res = div.execute(1,2);

            if(res<0){
                fail("the division returns a negative integer");
            }
        } catch (OperationException e) {
            fail("the division failed.");
        }

        try {
            logger.info("execute : 15646/2211");
            double res = div.execute(15646,2211);

            if(res<0){
                fail("the division returns a negative integer");
            }

        } catch (OperationException e) {
            fail("the division failed.");
        }
    }

    @Test
    public void testDivisionWithOneNegativeInteger(){
        try {
            logger.info("execute : -11/2");
            double res = div.execute(-11,2);

            if(res>0){
                fail("the division returns a positive integer");
            }
        } catch (OperationException e) {
            fail("the division failed.");
        }

        try {
            logger.info("execute : 84/-5484");
            double res = div.execute(84,-5484);

            if(res>0){
                fail("the division returns a positive integer");
            }

        } catch (OperationException e) {
            fail("the division failed.");
        }
    }

    @Test
    public void testDivisionWithTwoNegativeInteger(){
        try {
            logger.info("execute : -84/-5484");
            double res = div.execute(-84,-5484);

            if(res<0){
                fail("the division returns a negative integer");
            }

        } catch (OperationException e) {
            fail("the division failed.");
        }
    }

    @Test
    public void testDivisionWithOperand2ToZero(){
        try {
            logger.info("execute : -84/0");
            double res = div.execute(-84,0);

            fail("OperationException expected");

        } catch (OperationException e) {
            assertTrue(true);
        }

        try {
            logger.info("execute : 0/0");
            double res = div.execute(0,0);

            fail("OperationException expected");

        } catch (OperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDivisionElementNeutre(){
        try {
            logger.info("execute : 0/11");
            double res = div.execute(0,11);
            if(res!=0){
                fail("the result must be equals to 0");
            }
        } catch (OperationException e) {
            assertTrue(true);
        }

        try {
            logger.info("execute : 0/-11");
            double res = div.execute(0,-11);
            if(res!=0){
                fail("the result must be equals to 0");
            }
        } catch (OperationException e) {
            assertTrue(true);
        }
    }


}