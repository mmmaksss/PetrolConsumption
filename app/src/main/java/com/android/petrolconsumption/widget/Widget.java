package com.android.petrolconsumption.widget;

import com.android.petrolconsumption.R;
import com.android.petrolconsumption.utils.VoiceRecognizerService;

import android.content.ComponentName;
import android.content.Context;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Intent;
import android.widget.RemoteViews;


public class Widget extends AppWidgetProvider {

    public static String ACTION_WIDGET_START_COMMAND_LISTENING = "com.android.voicerecognizer.widget.START_COMMAND_LISTENING";
    public static String ACTION_WIDGET_STOP_COMMAND_LISTENING = "com.android.voicerecognizer.widget.STOP_COMMAND_LISTENING";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        Intent intent = new Intent(context, Widget.class);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);
        intent.setAction(ACTION_WIDGET_START_COMMAND_LISTENING);
        remoteViews.setInt(R.id.widget_button, "setBackgroundResource",
                R.drawable.widget_icon_off);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_button, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Receive event
        final String action = intent.getAction();

        if (ACTION_WIDGET_START_COMMAND_LISTENING.equals(action)) {
            // create intent for stop service after click on widget.
            Intent stopIntent = new Intent(context, Widget.class);
            stopIntent.setAction(ACTION_WIDGET_STOP_COMMAND_LISTENING);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, stopIntent, 0);

            // update remote view
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
            remoteViews.setInt(R.id.widget_button, "setBackgroundResource",
                    R.drawable.widget_icon_on);
            remoteViews.setOnClickPendingIntent(R.id.widget_button,
                    pendingIntent);

            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context.getApplicationContext());
            ComponentName thisWidget = new ComponentName(context,
                    this.getClass());
            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetManager.updateAppWidget(allWidgetIds, remoteViews);

            context.startService(new Intent(context,
                    VoiceRecognizerService.class));

        } else if (ACTION_WIDGET_STOP_COMMAND_LISTENING.equals(action)) {
            // create intent for start service after click on widget.
            Intent startIntent = new Intent(context, Widget.class);
            startIntent.setAction(ACTION_WIDGET_START_COMMAND_LISTENING);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, startIntent, 0);

            // update remote view
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
            remoteViews.setInt(R.id.widget_button, "setBackgroundResource",
                    R.drawable.widget_icon_off);
            remoteViews.setOnClickPendingIntent(R.id.widget_button,
                    pendingIntent);

            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context.getApplicationContext());
            ComponentName thisWidget = new ComponentName(context,
                    this.getClass());
            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetManager.updateAppWidget(allWidgetIds, remoteViews);

            context.stopService(new Intent(context,
                    VoiceRecognizerService.class));
        }

        super.onReceive(context, intent);
    }
}
