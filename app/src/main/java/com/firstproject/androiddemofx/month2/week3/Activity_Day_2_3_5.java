package com.firstproject.androiddemofx.month2.week3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class Activity_Day_2_3_5 extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private TextView tvGyroscope;
    private ImageView imageViewBall;
    private float lx = 0;
    private float ly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day235);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvGyroscope = findViewById(R.id.tvGyroscope);
        imageViewBall = findViewById(R.id.imageViewBall);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 根据陀螺仪进行相应处理
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            tvGyroscope.setText("陀螺仪数据：\nx:" + x + "\ny:" + y + "\nz:" + z);

            float nx = lx + x * 10;
            float ny = ly + y * 10;

            // 获取屏幕尺寸
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int screenHeight = getResources().getDisplayMetrics().heightPixels;


            // 限制小球的移动范围
            if (nx > screenHeight / 2) {
                nx = screenHeight / 2;
            } else if (nx < -(screenHeight / 2)) {
                nx = -(screenHeight / 2);
            }
            if (ny > screenWidth / 2) {
                ny = screenWidth / 2;
            } else if (ny < -(screenWidth / 2)) {
                ny = -(screenWidth / 2);
            }

            // 设置小球的位置
            imageViewBall.setTranslationX(ny);
            imageViewBall.setTranslationY(nx);

            lx = nx;
            ly = ny;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gyroscopeSensor != null) {
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}