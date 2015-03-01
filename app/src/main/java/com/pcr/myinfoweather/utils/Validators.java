package com.pcr.myinfoweather.utils;

import android.content.Context;
import android.widget.Toast;

import com.pcr.myinfoweather.R;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Created by Paula on 01/03/2015.
 */
public class Validators {

    public static String validateTypedCity(String city, Context ctx) {
        URI uri = null;
        try {
            String nfdNormalizedString = Normalizer.normalize(city, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            String str = pattern.matcher(nfdNormalizedString).replaceAll("");

            uri = new URI(str.replaceAll(" ", "%20"));
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("log uri: " + uri);

        if(uri != null) {
            return Constants.LOCAL_PATH + uri.toString(); //+ Constants.UNITS_PATH + unity;
        } else {
            Toast.makeText(ctx, R.string.toast_invalid_city_name, Toast.LENGTH_LONG).show();
            return "";
        }
    }
}
