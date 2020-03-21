package com.vladimir.rpp_lab_4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.vladimir.rpp_lab_4.Widget.UPDATE_ALL_WIDGETS;

public class UpdateWidgetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED) ||
                intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) ||
                intent.getAction().equals(Intent.ACTION_TIME_CHANGED)) {

            Intent updateIntent = new Intent(context, Widget.class);
            updateIntent.setAction(UPDATE_ALL_WIDGETS);
            context.sendBroadcast(updateIntent);
        }
    }

}
