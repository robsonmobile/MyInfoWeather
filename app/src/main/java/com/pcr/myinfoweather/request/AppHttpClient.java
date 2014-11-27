package com.pcr.myinfoweather.request;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pcr.myinfoweather.response.WeatherHttpResponseHandler;
import com.pcr.myinfoweather.utils.Constants;

import org.apache.http.Header;

/**
 * Created by Paula on 08/11/2014.
 */
public class AppHttpClient {

    private static AsyncHttpClient mClient = new AsyncHttpClient();

    public static void cancelAllRequests(Context mContext) {
        mClient.cancelAllRequests(true);
    }

    public static void get(String url, RequestParams params, WeatherHttpResponseHandler.ResourceParserHandler responseHandler) {
        setClientPresets();
        mClient.get(getAbsoluteUrl(url), params, WeatherHttpResponseHandler.getInstance(responseHandler));
        ;
    }



    public static void getResource(String url, BinaryHttpResponseHandler responseHandler) {
        setClientPresets();
        mClient.get(url, responseHandler);
    }

    public static void getImageBitmap(String url, final WeatherHttpResponseHandler.BitmapParserHandler handler) {
        String[] allowedContentTypes = new String[] {"image/png", "image/jpeg"};
        AppHttpClient.getResource(url, new BinaryHttpResponseHandler(allowedContentTypes) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                handler.onSuccess(binaryData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
                handler.onFailure(error);
            }
        });
    }

    public static String getAbsoluteUrl(String relativeUrl) {
        return Constants.SERVER_URL + relativeUrl;
    }


    private static void setClientPresets() {
        mClient.setTimeout(60000);
        mClient.setMaxConnections(1);
    }
}
