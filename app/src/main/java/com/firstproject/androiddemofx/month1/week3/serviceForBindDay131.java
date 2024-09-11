package com.firstproject.androiddemofx.month1.week3;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;


public class serviceForBindDay131 extends Service {
    private static final String TAG = "MyBindService1";
    public MyBinder myBinder=new serviceForBindDay131.MyBinder();
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("msg","B执行了onCreat()");
        sendBroadcast("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("msg","B执行了onStartCommand()");
        sendBroadcast("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        sendBroadcast("onDestroy");
        Log.d("msg","B执行了onDestory()");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("msg","B执行了onUnbind()");
        sendBroadcast("onUnbind");
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("msg","B执行了onBind()");
        sendBroadcast("onBind");
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
        Intent intent = new Intent("BIND_SERVICE_LIFECYCLE_BROADCAST");
        intent.putExtra("status", status+"\n");
        sendBroadcast(intent);
    }
}

