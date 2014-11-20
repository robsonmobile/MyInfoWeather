package com.pcr.myinfoweather.models;

/**
 * Created by Paula on 08/11/2014.
 */
public class LocationData {

    private float latitude;
    private float longitude;
    private String city;
    private String state;
    private String country;
    private String tempMedium;
    private String tempMax;
    private String tempMin;
    private String title;

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
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

    public String getTempMedium() {
        return tempMedium;
    }

    public void setTempMedium(String tempMedium) {
        this.tempMedium = tempMedium;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
