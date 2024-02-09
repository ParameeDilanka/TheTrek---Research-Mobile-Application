package com.example.thetrek.screens.mobileUsage;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.thetrek.MainActivityLogin;
import com.example.thetrek.R;

public class AlarmHelper {
    private static final int NOTIFICATION_ID = 1;
    private static final int HOUR_IN_MILLIS = 60 * 1000; // 1 hour in milliseconds
    private static final String CHANNEL_ID = "screen_monitoring_channel";

    public static void scheduleNotificationAlarm(Context context) {
        // Create a pending intent to start the notification service
        Intent serviceIntent = new Intent(context, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get the alarm manager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Calculate the alarm time
        long triggerTime = SystemClock.elapsedRealtime() + HOUR_IN_MILLIS;

        // Schedule the alarm
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
        }
    }

    public static void cancelNotificationAlarm(Context context) {
        // Create a pending intent to cancel the notification service
        Intent serviceIntent = new Intent(context, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get the alarm manager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Cancel the alarm
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    public static void showNotification(Context context) {
        // Create a pending intent to open the MainActivity when the notification is clicked
        Intent intent = new Intent(context, MainActivityLogin.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Screen Awake Notification")
                .setContentText("Your screen has been awake for over 1 hour.")
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setColor(ContextCompat.getColor(context, R.color.black))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Screen Monitoring", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}

