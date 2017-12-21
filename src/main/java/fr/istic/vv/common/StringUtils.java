package fr.istic.vv.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtils {

    private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);

    private StringUtils() {
    }

    /**
     * Transform a double between 0 and 1 in
     * a String between 0.00% to 100%
     * @param number
     * @return
     */
    public static String percentage(Double number){

        //Check value
        if(number<0||number>1){
            logger.warn("The value {} cannot be converted to a percentage.",number);
            return Double.toString(number*100);
        }
        else{
            //Maximal value
            if(number==1D){
                return "100.0";
            }

            int integerPart = (int)(number*100);
            int decimalPart = (int)(number*10000) - (int)(number*100)*100;

            return String.format("%2d.%-2d", integerPart,decimalPart);
        }
    }
}
