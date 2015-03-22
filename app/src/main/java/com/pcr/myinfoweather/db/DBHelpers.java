package com.pcr.myinfoweather.db;

import android.content.Context;

import com.pcr.myinfoweather.models.UserLocation;

import se.emilsjolander.sprinkles.Migration;
import se.emilsjolander.sprinkles.Sprinkles;

/**
 * Created by Paula on 07/03/2015.
 */
public class DBHelpers {

    private static final String TAG = DBHelpers.class.getSimpleName();

    public static void configureDatabaseIfNeeded(Context context) {
        setupDB(context);
    }

    private static void setupDB(Context context) {
        Sprinkles sprinkles = Sprinkles.init(context);
        Migration initialMigration = new Migration();
        addTables(initialMigration);
        sprinkles.addMigration(initialMigration);
    }

    private static void addTables(Migration m) {
        m.createTable(UserLocation.class);
    }


}
