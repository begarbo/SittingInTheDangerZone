package edu.neu.madcourse.michellelee.dangerzone.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBootReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            // Enable notification
            NotificationHelper.scheduleRepeatingElapsedNotification(context);
        }
    }
}
