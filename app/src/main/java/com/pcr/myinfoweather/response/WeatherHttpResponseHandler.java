package com.pcr.myinfoweather.response;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Paula on 08/11/2014.
 */
public class WeatherHttpResponseHandler extends JsonHttpResponseHandler{

    public interface ResourceParserHandler {
        public void onSuccess(Object resource);
        public void onFailure(Throwable e);
        public void onFailure(Throwable e, String errorMessage);
    }

    public interface BitmapParserHandler {
        public void onSuccess(byte[] resource);
        public void onFailure(Throwable e);
    }

    private ResourceParserHandler handler;
    private static WeatherHttpResponseHandler instance;

    private WeatherHttpResponseHandler(ResourceParserHandler handler) {
        super();
        this.handler = handler;
    }

    public static WeatherHttpResponseHandler getInstance(ResourceParserHandler handler) {
        instance = new WeatherHttpResponseHandler(handler);
        instance.setHandler(handler);
        return instance;
    }

    private void setHandler(ResourceParserHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        handler.onSuccess(response);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        handler.onSuccess(response);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        handler.onSuccess(responseString);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        handler.onFailure(throwable, responseString);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        handler.onFailure(throwable);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        handler.onFailure(throwable);
    }
}
