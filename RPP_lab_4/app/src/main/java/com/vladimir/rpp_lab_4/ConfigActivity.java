package com.vladimir.rpp_lab_4;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

import static com.vladimir.rpp_lab_4.TimeConstants.*;

public class ConfigActivity extends Activity {

    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;

    final String LOG_TAG = "myLogs";
    final Calendar dateAndTime=Calendar.getInstance();

    public final static String WIDGET_PREF = "widget_pref";
    public final static String WIDGET_DATE = "widget_date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate config");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        setResult(RESULT_CANCELED, resultValue);

        setContentView(R.layout.activity_config);

        setDate();
    }

    public void setDate() {
        long minDate = System.currentTimeMillis();
        if ((minDate + UTC_DIFF) % ONE_DAY  > NINE_HOURS ) {
            minDate += ONE_DAY;
        }

        DatePickerDialog dialog = new DatePickerDialog(
                this, dateListener,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(minDate);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });

        dialog.show();
    }

    DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dateAndTime.set(Calendar.HOUR, 9);
            dateAndTime.set(Calendar.MINUTE, 0);
            dateAndTime.set(Calendar.SECOND, 0);
            dateAndTime.set(Calendar.MILLISECOND, 0);

            finishWithResult(dateAndTime.getTimeInMillis());
        }
    };

    private void finishWithResult(long date) {
        SharedPreferences sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putLong(WIDGET_DATE + widgetID, date);
        editor.apply();

        AppWidgetManager appWidgetManager =
                AppWidgetManager.getInstance(getApplicationContext());
        Widget.updateWidget(getApplicationContext(), appWidgetManager, widgetID);

        setResult(RESULT_OK, resultValue);

        Log.d(LOG_TAG, "finish config " + widgetID);

        finish();
    }

}
