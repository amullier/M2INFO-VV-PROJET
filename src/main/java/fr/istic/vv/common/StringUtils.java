package fr.istic.vv.common;

public class StringUtils {

    private StringUtils() {
    }

    /**
     * Transform a double between 0 and 1 in
     * a String between 0.00% to 100%
     * @param number
     * @return
     */
    public static String pourcentage(double number){
        int numberOfDecimal = 3;
        int integerPart = (int)(number*100);
        int decimalPart = (int)(number*10000) - (int)(number*100)*100;

        for (int i=1;i<=numberOfDecimal;i++){
            if(decimalPart/(10*numberOfDecimal)<0){
                decimalPart*=10*numberOfDecimal;
                break;
            }
        }

        return String.format("%d.%d", integerPart,decimalPart);
    }
}
