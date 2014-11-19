package com.pcr.myinfoweather.utils;

/**
 * Created by Paula on 08/11/2014.
 */
public class Constants {

    /*** Webservices Url´s***/
    public static final String SERVER_URL = "http://api.openweathermap.org/data/2.5/weather?";
    //public static final String SERVER_URL = "http://api.openweathermap.org/data/2.5/find?";

    /*** Paths ***/
    public static final String LOCATION_PATH = "";
    public static final String LOCAL_PATH = "q=sao%20paulo,sp";
    public static final String GEOLOCATION_PATH = "lat=-23&lon=-46";
    public static final String LATITUDE_PATH = "lat=";
    public static final String LONGITUDE_PATH = "&lon=";

    /***Convert Temperature Constants***/
    public static final int TEMP_CELSIUS = 0;
    public static final int TEMP_FAHRENHEIT = 1;

    /***Shared Preferences Type***/
    public static final int PREF_TYPE_TEMPERATURE = 0;

    /***Paths to make request***/
    public static final int PATH_FOR_GEOLOCATION = 0;
    public static final int PATH_FOR_CITY = 1;



}
