package com.pcr.myinfoweather.models.currentweather;

/**
 * Created by Paula on 20/11/2014.
 */
public class LocationData {

    private float lat;
    private float lon;
    private String city;
    private String state;
    private String country;

    private static LocationData locationDataInstance;

    private LocationData() {
        this.lat = 0;
        this.lon = 0;
        this.city = "";
        this.state = "";
        this.country = "";
    }
    public static LocationData getInstance() {
        if(locationDataInstance == null) {
            locationDataInstance = new LocationData();
        }
        return locationDataInstance;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
