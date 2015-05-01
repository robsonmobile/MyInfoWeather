package com.pcr.myinfoweather.providers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.activities.Main;

/**
 * Created by Paula on 01/05/2015.
 */
public class WidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager manager, int[] id) {
        final int N = id.length;

        for(int i=0; i < N; i++) {
            int appWidgetId = id[i];

            //creating intent to start the activity
            Intent intent = new Intent(context, Main.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            //click listener - when button is clicked the activity with the app is opened
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget_layout);
            remoteViews.setOnClickPendingIntent(R.id.button, pendingIntent);

            manager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}
