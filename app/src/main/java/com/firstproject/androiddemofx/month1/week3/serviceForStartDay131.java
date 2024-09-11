package com.firstproject.androiddemofx.month1.week3;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class serviceForStartDay131 extends Service {
    private static final String TAG = "MyStartService1";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"S执行了onCreat()");
        sendBroadcast("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"S执行了onStartCommand()");
        sendBroadcast("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"S执行了onDestory()");
        sendBroadcast("onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        sendBroadcast("onUnbind");
        Log.d(TAG,"S执行了onUnbind()");
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"S执行了onBind()");
        sendBroadcast("onBind");
        return null;
    }

    private void sendBroadcast(String status) {
        Intent intent = new Intent("START_SERVICE_LIFECYCLE_BROADCAST");
        intent.putExtra("status", status+"\n");
        sendBroadcast(intent);
    }
}
