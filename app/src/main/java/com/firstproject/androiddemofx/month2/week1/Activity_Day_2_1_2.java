package com.firstproject.androiddemofx.month2.week1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class Activity_Day_2_1_2 extends AppCompatActivity {
    private int[] brightnessLevels = {255, 204, 153, 102, 51};
    private int level = 0;
    TextView tv;
    private boolean isPaused = false;
    private Handler tHandle;
    private Runnable runnable;
    private Thread mThread;
    private int initSB=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day212);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        Intent intentFromWeek = getIntent();
        if (intentFromWeek!=null) initSB=intentFromWeek.getIntExtra("CurrentScreenBrightness",0);

        tv = findViewById(R.id.tv);
        tHandle = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                setScreenBrightness(brightnessLevels[level % 5]);
                level++;
                tv.setText("" + getCurrentScreenBrightness());
            }
        };
        runnable = new Runnable() {
            @Override
            public void run() {
                while (!isPaused) {
                    try {
                        Thread.sleep(1000);
                        tHandle.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
                        Thread.currentThread().interrupt();
//                        return;
                    }
                }
            }
        };
        mThread = new Thread(runnable);
        mThread.start();
    }

    @Override
    protected void onDestroy() {
        if (mThread != null && mThread.isAlive())
            mThread.interrupt();

        setScreenBrightness(initSB);
        super.onDestroy();
    }

    private void setScreenBrightness(int brightness) {
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
    }

    private int getCurrentScreenBrightness() {
        try {
            int anInt = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            return anInt;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}