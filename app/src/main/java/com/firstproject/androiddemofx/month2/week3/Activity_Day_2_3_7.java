package com.firstproject.androiddemofx.month2.week3;

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

public class Activity_Day_2_3_7 extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor gravitySensor;
    private TextView textViewGravityValues;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day237);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewGravityValues = findViewById(R.id.textViewGravityValues);
        imageView = findViewById(R.id.imageView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            textViewGravityValues.setText("重力传感器数据：\nx=" + x + "\ny=" + y + "\nz=" + z);

            // 根据重力传感器数据调整图片显示
            if (Math.abs(x) > Math.abs(y) && x > 0) {
                // 向右倾斜
                imageView.setImageResource(R.drawable.west);
            } else if (Math.abs(x) > Math.abs(y) && x < 0) {
                // 向左倾斜
                imageView.setImageResource(R.drawable.east);
            } else if (Math.abs(y) > Math.abs(x) && y > 0) {
                // 向后倾斜
                imageView.setImageResource(R.drawable.south);
            } else if (Math.abs(y) > Math.abs(x) && y < 0) {
                // 向前倾斜
                imageView.setImageResource(R.drawable.north);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gravitySensor!= null) {
            sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}