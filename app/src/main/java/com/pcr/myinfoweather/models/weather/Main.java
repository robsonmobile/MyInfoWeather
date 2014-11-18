package com.pcr.myinfoweather.models.weather;

import com.pcr.myinfoweather.utils.Constants;
import com.pcr.myinfoweather.utils.SharedPreferencesData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paula on 10/11/2014.
 */
public class Main {

    private float temp_min;
    private float temp;
    private int humidity;
    private int pressure;
    private float temp_max;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The tempMin
     */
    public float getTempMin() {
        return temp_min;
    }

    /**
     *
     * @param tempMin
     * The temp_min
     */
    public void setTempMin(float tempMin) {
        this.temp_min = tempMin;
    }

    /**
     *
     * @return
     * The temp
     */
    public float getTemp() {
        return temp;
    }

    /**
     *
     * @param temp
     * The temp
     */
    public void setTemp(float temp) {
        this.temp = temp;
    }

    /**
     *
     * @return
     * The humidity
     */
    public int getHumidity() {
        return humidity;
    }

    /**
     *
     * @param humidity
     * The humidity
     */
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    /**
     *
     * @return
     * The pressure
     */
    public int getPressure() {
        return pressure;
    }

    /**
     *
     * @param pressure
     * The pressure
     */
    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    /**
     *
     * @return
     * The tempMax
     */
    public float getTempMax() {
        return temp_max;
    }

    /**
     *
     * @param tempMax
     * The temp_max
     */
    public void setTempMax(float tempMax) {
        this.temp_max = tempMax;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


}