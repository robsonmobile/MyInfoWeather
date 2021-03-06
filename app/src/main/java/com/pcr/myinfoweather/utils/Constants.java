package com.pcr.myinfoweather.utils;

import java.util.ArrayList;

/**
 * Created by Paula on 08/11/2014.
 */
public class Constants {

    /*** Webservices Url´s***/
    public static final String SERVER_URL = "http://api.openweathermap.org/data/2.5/weather?";
    //public static final String SERVER_URL = "http://api.openweathermap.org/data/2.5/find?";

    /*** Paths ***/
    public static final String LOCATION_PATH = "";
    public static final String LOCAL_PATH = "q=";
    public static final String COMMA_CHARACTER = ",";
    public static final String GEOLOCATION_PATH = "lat=-23&lon=-46";
    public static final String LATITUDE_PATH = "lat=";
    public static final String LONGITUDE_PATH = "&lon=";
    public static final String AND = "&";
    public static final String UNITS_PATH = "&units=";

    /***Convert Temperature Constants***/
    public static final String UNIT_TYPE_CELSIUS = "C";
    public static final String UNIT_TYPE_FAHRENHEIT = "F";

    /***Temperature Constants***/
    public static final String CELSIUS_TEMP = "metric";
    public static final String FAHRENHEIT_TEMP = "imperial";

    /***Paths to make request***/
    public static final int PATH_FOR_GEOLOCATION = 0;
    public static final int PATH_FOR_CITY = 1;

    /***DIALOG***/

    /***Dialog Constants Tag***/
    public static final String DIALOG_INTERNET_CONN_FAILURE = "internetConnectionFailure";
    public static final String DIALOG_SERVER_CONN_FAILURE = "serverConnectionFailure";
    public static final String DIALOG_SERVER_ERROR = "dialogServerError";

    /***Dialog Type***/
    public static final int DIALOG_TYPE_ALERT = 0;
    public static final int DIALOG_TYPE_OPTIONS = 1;
    public static final int DIALOG_TYPE_CUSTOM = 2;
    public static final int DIALOG_TYPE_SINGLECHOICE = 3;

    /***Units***/
    public static final String UNIT_TEMP_CELSIUS = " °C";
    public static final String UNIT_TEMP_FAHRENHEIT = " °F";
    public static final String UNIT_WIND_METRIC = " m/s";

    /***Day/Night indicator***/
    public static final int TIME_DAY = 0;
    public static final int TIME_NIGHT = 1;

    /***Preferences***/
    public static final int PREF_TYPE_TEMPERATURE = 0;
    public static final int PREF_TYPE_UPDATE_INTERVAL = 1;
    //DEFAULT 30 MINUTOS
    public static final long PREF_DEFAULT_UPDATE_INTERVAL = 1800000;

    /***Number of listed occurrences***/
    public static final int NUMBER_OF_OCCURRENCES = 20;

    /***Geolocation***/
    public static final int LATITUDE = 0;
    public static final int LONGITUDE = 1;

    /***Address***/
    public static final int CITY = 0;
    public static final int STATE = 1;
    public static final int COUNTRY = 2;

    public static final String ADDRESS_NOT_FOUND = "Address not found";

    /***Connections feedback***/
    public static final String GPS_CONNECTION_FAILURE = "Unable to find GPS, plese verify";
    public static final String INTERNET_CONNECTION_FAILURE = "Unable to connect to the server";

    /***Connections feedback***/
    public static final String BAD_CONNECTION = "bad_connection";
    public static final String GPS_OFF = "gps_off";

    public static final String TURN_ON_INTERNET = "Please verify your internet connection";
    public static final String TURN_ON_GPS = "Please verify if your gps is off";

}
