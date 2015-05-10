package com.pcr.myinfoweather.exceptions;

/**
 * Created by Paula on 10/05/2015.
 */
public class UserLocationException extends Exception {

    public UserLocationException(String message) {
        super(message);
    }

    public UserLocationException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public UserLocationException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub

    }

}
