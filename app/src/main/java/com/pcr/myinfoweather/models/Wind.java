package com.pcr.myinfoweather.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paula on 10/11/2014.
 */
public class Wind {

    private float speed;
    private int deg;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The speed
     */
    public float getSpeed() {
        return speed;
    }

    /**
     *
     * @param speed
     * The speed
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     *
     * @return
     * The deg
     */
    public int getDeg() {
        return deg;
    }

    /**
     *
     * @param deg
     * The deg
     */
    public void setDeg(int deg) {
        this.deg = deg;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

