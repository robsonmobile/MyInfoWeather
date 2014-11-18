package com.pcr.myinfoweather.models.weather;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paula on 10/11/2014.
 */
public class Coord {

    private float lat;
    private float lon;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The lat
     */
    public float getLat() {
        return lat;
    }

    /**
     *
     * @param lat
     * The lat
     */
    public void setLat(float lat) {
        this.lat = lat;
    }

    /**
     *
     * @return
     * The lon
     */
    public float getLon() {
        return lon;
    }

    /**
     *
     * @param lon
     * The lon
     */
    public void setLon(float lon) {
        this.lon = lon;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}