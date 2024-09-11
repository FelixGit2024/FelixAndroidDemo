package com.firstproject.androiddemofx.util;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class TimerManager {
    private static TimerManager instance;
    private Timer timer;

    private TimerManager() {
    }

    public static TimerManager getInstance() {
        if (instance == null) {
            instance = new TimerManager();
        }
        return instance;
    }

    public void startTimer() {
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Log.d("TimerManager", "Sending notification...");
                    // 发送通知的逻辑
                }
            }, 0, 3000);
        }
    }

    public void cancelTimer() {
        if (timer!= null) {
            timer.cancel();
            timer = null;
        }
    }
}