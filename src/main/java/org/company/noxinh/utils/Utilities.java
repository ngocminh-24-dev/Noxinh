package org.company.noxinh.utils;

import java.math.BigDecimal;

public class Utilities {
    public static String processString (String string, String compareString){
        if (string == null)
            return compareString;
        return string.trim();
    }

    public static BigDecimal processBigDecimal (BigDecimal bigDecimal, BigDecimal compareBigDecimal){
        if (bigDecimal == null){
            return compareBigDecimal;
        }
        return bigDecimal;
    }
}
