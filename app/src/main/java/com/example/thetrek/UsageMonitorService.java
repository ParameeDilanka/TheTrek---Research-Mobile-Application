package com.example.thetrek;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class UsageMonitorService extends Service {

    private static final String TAG = "UsageMonitorService";
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "UsageMonitorChannel";
    private static final long USAGE_THRESHOLD = 2 * 60 * 1000; // 2 minutes in milliseconds

    private boolean isScreenOn = true;
    private long usageStartTime = 0;
    private Handler handler;
    private Runnable checkUsageRunnable;

    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler();
        checkUsageRunnable = new Runnable() {
            @Override
            public void run() {
                if (isScreenOn) {
                    if (usageStartTime == 0) {
                        usageStartTime = System.currentTimeMillis();
                    } else {
                        long currentTime = System.currentTimeMillis();
                        long usageTime = currentTime - usageStartTime;
                        if (usageTime > USAGE_THRESHOLD) {
                            sendNotification();
                        }
                    }
                } else {
                    usageStartTime = 0;
                }
                handler.postDelayed(this, 1000); // Check every second
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, createNotification());

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver, filter);

        handler.post(checkUsageRunnable);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenReceiver);
        handler.removeCallbacks(checkUsageRunnable);
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Usage Monitor Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private Notification createNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Mobile Usage Monitor")
                .setContentText("Monitoring your mobile usage...")
                .setSmallIcon(R.mipmap.ic_launcher2)
                .setContentIntent(pendingIntent)
                .build();
    }

    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher1)
                .setContentTitle("Mobile Usage Alert")
                .setContentText("📱🕒 You've been glued to your phone for over 2 minutes! 🚶‍♂️ It's time to stretch those legs and get moving! 🏃‍♀️💨")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private final BroadcastReceiver screenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                    isScreenOn = true;
                } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    isScreenOn = false;
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
