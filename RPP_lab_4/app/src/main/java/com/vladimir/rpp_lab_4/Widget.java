package com.vladimir.rpp_lab_4;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

public class Widget extends AppWidgetProvider {

    static final String TAG = "Widget";
    static final String UPDATE_ALL_WIDGETS = "update_all_widgets";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        setAlarmManager(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, id);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        SharedPreferences.Editor editor = context.getSharedPreferences(
                ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE).edit();
        for (int widgetID : appWidgetIds) {
            editor.remove(ConfigActivity.WIDGET_DATE + widgetID);
        }
        editor.apply();
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        removeAlarmManager(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equalsIgnoreCase(UPDATE_ALL_WIDGETS)) {
            ComponentName thisAppWidget = new ComponentName(
                    context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for (int appWidgetID : ids) {
                updateWidget(context, appWidgetManager, appWidgetID);
            }
        }
    }

    static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetID) {
        SharedPreferences sp = context.getSharedPreferences(
                ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);

        long widgetDate = sp.getLong(ConfigActivity.WIDGET_DATE + widgetID, 0);
        if (widgetDate == 0) return;

        long daysLeft = (widgetDate - getNextTriggerTime()) / TimeConstants.ONE_DAY;

        RemoteViews widgetView = new RemoteViews(context.getPackageName(),
                R.layout.widget);
        widgetView.setTextViewText(R.id.widget_text, getWidgetText(daysLeft));

        Intent configIntent = new Intent(context, ConfigActivity.class);
        configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        PendingIntent pIntent = PendingIntent.getActivity(context, widgetID,
                configIntent, 0);
        widgetView.setOnClickPendingIntent(R.id.widget_text, pIntent);

        if (daysLeft == 0 && System.currentTimeMillis() >= widgetDate) {
            widgetView.setTextViewText(R.id.widget_text, "-");

            SharedPreferences.Editor editor = context.getSharedPreferences(
                    ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE).edit();


            editor.remove(ConfigActivity.WIDGET_DATE + widgetID);

            editor.apply();
            showNotification(context);
        }

        appWidgetManager.updateAppWidget(widgetID, widgetView);
        setAlarmManager(context);
    }

    static void setAlarmManager(Context context) {
        Intent intent = new Intent(context, Widget.class);
        intent.setAction(UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(
                context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, getNextTriggerTime(), pIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, getNextTriggerTime(), pIntent);
        }

        Log.wtf(TAG, "NEXT TRIGGER: " + getNextTriggerTime());
    }

    static void removeAlarmManager(Context context) {
        Intent intent = new Intent(context, Widget.class);
        intent.setAction(UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
    }

    static void showNotification(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "1",
                    "my channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("my channel description");
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(context, "1")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Свершилось чудо")
                .setContentText("Этот день настал!")
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(), 0))
                .setAutoCancel(true)
                .build();

        notificationManager.notify(1, notification);
    }

    static long getNextTriggerTime() {
        long nowTime = System.currentTimeMillis();
        long fromStartOfDay = nowTime % TimeConstants.ONE_DAY;
        long nextTrigger = nowTime - fromStartOfDay + TimeConstants.NINE_HOURS;

        if (fromStartOfDay > TimeConstants.NINE_HOURS) {
            nextTrigger += TimeConstants.ONE_DAY;
        }

        return nextTrigger - TimeConstants.UTC_DIFF;
    }

    static private String getWidgetText(long daysLeft) {
        return "Осталось дней: " + daysLeft;
    }

}
