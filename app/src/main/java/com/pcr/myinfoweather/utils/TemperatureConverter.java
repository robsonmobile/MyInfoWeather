package com.pcr.myinfoweather.utils;

import java.util.Locale;

/**
 * Created by Paula on 12/12/2014.
 */
public class TemperatureConverter {

    private static TemperatureConverter instance;

    public static TemperatureConverter getInstance() {
        if(instance == null) {
            instance = new TemperatureConverter();
        }
        return instance;
    }

    private TemperatureConverter() {

    }

    public float convertTemperature(int unity, float temperature) {
        float calculatedTemp = calcTemp(unity, temperature);
        calculatedTemp = Float.valueOf(formatFloatNumber(calculatedTemp));
        return calculatedTemp;
    }

    private float calcTemp (int type, float temp) {
        float finalTemp = 0;
        switch (type) {
            case Constants.TEMP_CELSIUS:
                finalTemp = (temp - 273.15f);
                break;
            case Constants.TEMP_FAHRENHEIT:
                finalTemp = ((1.8f * (temp - 273.15f)) + 32);
                break;
        }
        return finalTemp;
    }

    private String formatFloatNumber(float number) {
        return String.format(Locale.US, "%.1f", number);
    }
}
