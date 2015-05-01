package com.pcr.myinfoweather.models.currentweather;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paula on 10/11/2014.
 */
public class Sys {

    private float message;
    private int id;
    private int sunrise;
    private int type;
    private int sunset;
    private String country;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The message
     */
    public float getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(float message) {
        this.message = message;
    }

    /**
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The sunrise
     */
    public int getSunrise() {
        return sunrise;
    }

    /**
     * @param sunrise The sunrise
     */
    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    /**
     * @return The type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return The sunset
     */
    public int getSunset() {
        return sunset;
    }

    /**
     * @param sunset The sunset
     */
    public void setSunset(int sunset) {
        this.sunset = sunset;
    }

    /**
     * @return The country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}