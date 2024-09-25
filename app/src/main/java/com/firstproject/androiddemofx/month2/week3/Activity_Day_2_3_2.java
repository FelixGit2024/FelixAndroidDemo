package com.firstproject.androiddemofx.month2.week3;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class Activity_Day_2_3_2 extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private TextView tvLight;
    private Sensor lightSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day232);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        tvLight=findViewById(R.id.tvLight);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(this,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);

        if (!isCanWrite()) {
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS), 1101);
        }
        else
            Toast.makeText(this, "权限申请成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //根据光强度进行响应处理
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightIntensity = event.values[0];
            tvLight.setText("光强度：" + lightIntensity);

            // 设定区间值，假设小于 10 为低亮度区间，大于 500 为高亮度区间
            if (lightIntensity < 50) {
                setScreenBrightness(20); // 设置低亮度值
            } else if (lightIntensity > 100) {
                setScreenBrightness(255); // 设置高亮度值
            }
        }
    }

    private void setScreenBrightness(int brightness) {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //传感器精度发生变化
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lightSensor!= null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isCanWrite() {
        return Settings.System.canWrite(this);
    }
}