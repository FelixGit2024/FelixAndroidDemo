package com.firstproject.androiddemofx.month1.week3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.firstproject.androiddemofx.R;

import java.util.Timer;
import java.util.TimerTask;

public class ServiceForBindDay132 extends Service {
    private static final String TAG = "MyBindService";
    private NotificationManager notificationManager;
    private Timer timer;
    public ServiceForBindDay132.MyBinder myBinder=new ServiceForBindDay132.MyBinder();
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("msg","B执行了onCreat()");
        sendBroadcast("onCreate");
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("msg","B执行了onStartCommand()");
        sendBroadcast("onStartCommand");
        startForeground(1, getNotification());
        startTimer();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        sendBroadcast("onDestroy");
        Log.d("msg","B执行了onDestory()");
        stopTimer();
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("msg","B执行了onUnbind()");
        sendBroadcast("onUnbind");
        stopTimer();
        stopForeground(true);
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("msg","B执行了onBind()");
        sendBroadcast("onBind");
        startForeground(1, getNotification());
        startTimer();
        return myBinder;
    }
    //新建一个子类继承自Binder类
    class MyBinder extends Binder {
        public void service_connect_Activity() {
            Log.d("msg", "B执行了service_connect_Activity");
            sendBroadcast("service_connect_Activity");
        }
    }

    private void sendBroadcast(String status) {
        Intent intent = new Intent("BIND_SERVICE_LIFECYCLE_BROADCAST_TEST");
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
                    .setSmallIcon(R.drawable.pink_b)
                    .build();
        } else {
            return new Notification.Builder(this)
                    .setContentTitle("My Service")
                    .setContentText("Service is running.")
                    .build();
        }
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "Sending notification...");
                notificationManager.notify(1, getNotification());
            }
        }, 0, 10000);
    }

    private void stopTimer() {
        if (timer!= null) {
            timer.cancel();
        }
    }
}
