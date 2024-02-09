package com.example.thetrek.screens.mobileUsage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class ScreenOnReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // Schedule an alarm after 1 hour
            AlarmHelper.scheduleNotificationAlarm(context);
        }
    }
}

