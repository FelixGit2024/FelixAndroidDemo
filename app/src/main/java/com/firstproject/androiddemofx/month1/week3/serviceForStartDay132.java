package com.firstproject.androiddemofx.month1.week3;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.firstproject.androiddemofx.R;
import com.firstproject.androiddemofx.util.TimerManager;

import java.util.Timer;
public class serviceForStartDay132 extends Service {
    private static final String TAG = "MyStartService2";
    private NotificationManager notificationManager;
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"S执行了onCreat()");
        sendBroadcast("onCreate");
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"S执行了onStartCommand()");
        sendBroadcast("onStartCommand");
        startForeground(1,getNotification());
        TimerManager.getInstance().startTimer();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"S执行了onDestory()");
        sendBroadcast("onDestroy");
        TimerManager.getInstance().cancelTimer();
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"S执行了onUnbind()");
        sendBroadcast("onUnbind");
        stopForeground(true);
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"S执行了onBind()");
        sendBroadcast("onBind");
        startForeground(1, getNotification());
        return null;
    }

    private void sendBroadcast(String status) {
        Intent intent = new Intent("START_SERVICE_LIFECYCLE_BROADCAST_TEST");
        intent.putExtra("status", status+"\n");
        sendBroadcast(intent);
    }

    private void createNotificationChannel() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("my_channel_id", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification getNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(this, "my_channel_id")
                    .setContentTitle("My Service")
                    .setContentText("Service is running.")
                    .setSmallIcon(R.drawable.pink_s)
                    .build();
        } else {
            return new Notification.Builder(this)
                    .setContentTitle("My Service")
                    .setContentText("Service is running.")
                    .build();
        }
    }
}

