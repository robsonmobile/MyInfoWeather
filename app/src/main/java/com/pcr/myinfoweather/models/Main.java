package com.pcr.myinfoweather.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paula on 10/11/2014.
 */
public class Main {

    private float temp_min;
    private float temp;
    private float humidity;
    private float pressure;
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
    public float getHumidity() {
        return humidity;
    }

    /**
     *
     * @param humidity
     * The humidity
     */
    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    /**
     *
     * @return
     * The pressure
     */
    public float getPressure() {
        return pressure;
    }

    /**
     *
     * @param pressure
     * The pressure
     */
    public void setPressure(float pressure) {
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