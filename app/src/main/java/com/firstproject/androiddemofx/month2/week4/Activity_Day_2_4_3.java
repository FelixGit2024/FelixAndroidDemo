package com.firstproject.androiddemofx.month2.week4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class Activity_Day_2_4_3 extends AppCompatActivity {
    private TextView batteryStatus;
    private BroadcastReceiver batteryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day243);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        batteryStatus=findViewById(R.id.batteryStatus);
        batteryReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                    int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
                    int percentage = (level * 100) / scale;
                    boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
                    String chargingMethod = "";
                    int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                    if (plugged == BatteryManager.BATTERY_PLUGGED_USB) {
                        chargingMethod = "USB";
                    } else if (plugged == BatteryManager.BATTERY_PLUGGED_AC) {
                        chargingMethod = "AC";
                    }
                    int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
                    batteryStatus.setText("电池电量：" + percentage + "%\n" +
                            "充电状态：" + (isCharging? "正在充电（" + chargingMethod + "）" : "未充电")+"\n" +
                            "电池温度："+temperature/10+"°C");
                }
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryReceiver);
    }
}