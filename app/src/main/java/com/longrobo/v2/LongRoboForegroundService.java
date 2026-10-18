package com.longrobo.v2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

public class LongRoboForegroundService extends Service {
    private static final String CHANNEL_ID = "LongroboV2Channel";
    @Override public void onCreate() { super.onCreate(); createChannel(); }
    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notif = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Longrobo-V2 dang chay")
                .setContentText("Tu dong tra loi khi tat man hinh")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setOngoing(true).build();
        startForeground(1, notif);
        return START_STICKY;
    }
    @Override public IBinder onBind(Intent intent) { return null; }
    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(CHANNEL_ID, "Longrobo V2", NotificationManager.IMPORTANCE_LOW);
            NotificationManager nm = getSystemService(NotificationManager.class);
            if (nm != null) nm.createNotificationChannel(ch);
        }
    }
}
