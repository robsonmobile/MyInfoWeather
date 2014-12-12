package com.pcr.myinfoweather.utils;

import android.content.Context;

/**
 * Created by Paula on 12/12/2014.
 */
public class DigitSeparator {

    private static DigitSeparator instance;
    private Context mContext;


    public static DigitSeparator getInstance(Context context) {
        if(instance == null) {
            instance = new DigitSeparator(context);
        }
        return instance;
    }

    private DigitSeparator(Context ctx) {
        this.mContext = ctx;
    }

    public int getDigit(int weatherConditionId, int numberDigits) {
        /*** 1 para primeiro digito;
         *   2 para os dois primeiros digitos;
         *   3 para ultimo digito;
         */
        String numberString = String.valueOf(weatherConditionId);
        int result = 0;
        switch (numberDigits) {
            case 1:
                char firstLetterchar = numberString.charAt(0);
                result = Integer.parseInt(String.valueOf(firstLetterchar));
                break;
            case 2:
                char secondLetterchar = numberString.charAt(1);
                result = Integer.parseInt(String.valueOf(secondLetterchar));
                break;
            case 3:
                char lastLetterchar = numberString.charAt(2);
                result = Integer.parseInt(String.valueOf(lastLetterchar));
                break;
        }

        return result;
    }

}
