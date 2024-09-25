package com.firstproject.androiddemofx.month2.week3;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class Activity_Day_2_3_6 extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor magneticSensor;
    private TextView textViewMagneticValues,textViewDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day236);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewMagneticValues = findViewById(R.id.textViewMagneticValues);
        textViewDirection = findViewById(R.id.textViewDirection);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            textViewMagneticValues.setText("地磁传感器数据：" +
                    "\nx=" + String.format("%.2f",x) +
                    "\ny=" + String.format("%.2f",y) +
                    "\nz=" + String.format("%.2f",z));

            // 计算方向
            double azimuth = Math.atan2(y, x);
            double degrees = Math.toDegrees(azimuth);
            if (degrees < 0) {
                degrees += 360;
            }

            String direction = getDirection(degrees);
            textViewDirection.setText("方向：" + direction);
        }
    }

    private String getDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5) {
            return "北";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            return "东北";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            return "东";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            return "东南";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            return "南";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            return "西南";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            return "西";
        } else {
            return "西北";
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (magneticSensor!= null) {
            sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}