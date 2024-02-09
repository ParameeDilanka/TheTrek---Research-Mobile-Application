package com.example.thetrek.screens.mobileUsage;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.thetrek.R;
import com.example.thetrek.screens.findPlaces.FindPlacesActivity;

public class ScreenMonitoringService extends Service {
    private static final String CHANNEL_ID = "screen_monitoring_channel";
    private static final int NOTIFICATION_ID = 1;
    private static final int HOUR_IN_MILLIS = 60 * 60 * 1000; // 1 hour in milliseconds

    private ScreenReceiver screenReceiver;
    private Handler handler;
    private Runnable notificationRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        notificationRunnable = new Runnable() {
            @Override
            public void run() {
                sendNotification();
            }
        };

        screenReceiver = new ScreenReceiver();
        registerReceiver(screenReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(screenReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager.isInteractive()) {
            // If the phone is already awake, start the timer immediately
            startNotificationTimer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenReceiver);
        stopNotificationTimer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startNotificationTimer() {
        handler.postDelayed(notificationRunnable, HOUR_IN_MILLIS);
    }

    private void stopNotificationTimer() {
        handler.removeCallbacks(notificationRunnable);
    }

    private void sendNotification() {
        // Create a pending intent to open the MainActivity when the notification is clicked
        Intent intent = new Intent(this, FindPlacesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Too long on the phone")
                .setContentText("Hey there! You've been on your phone for an hour, and while it's great to stay connected, how about exploring some nearby places that can boost your well-being?")
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setColor(ContextCompat.getColor(this, R.color.black))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Screen Monitoring", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private class ScreenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                startNotificationTimer();
            } else if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                stopNotificationTimer();
            }
        }
    }
}
