package com.firstproject.androiddemofx.month2.week4;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class Activity_Day_2_4_6 extends AppCompatActivity implements LocationListener {
    private LocationManager locationManager;
    private Button openGPS,stopGPS;
    private TextView locationTextView,locationText;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day246);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        openGPS = findViewById(R.id.openGPS);
        stopGPS = findViewById(R.id.stopGPS);
        locationTextView = findViewById(R.id.locationTextView);
        locationText = findViewById(R.id.locationText);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        openGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("GPS", "开启定位");
                updateTextView("开启定位");
                startLocationUpdates();
            }
        });

        stopGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("GPS", "关闭定位");
                stopLocationUpdates();
                updateTextView("关闭定位");
            }
        });
    }

    private void startLocationUpdates() {
        checkPermission();
        Log.d("GPS", "请求位置更新…… ");
        updateTextView("请求位置更新…… ");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, (float) 0, this);
    }

    private void stopLocationUpdates() {
        Log.d("GPS", "停止位置更新…… ");
        updateTextView("停止位置更新…… ");
        locationManager.removeUpdates(this);
    }

    public void checkPermission(){
        updateTextView("检查权限");
        // 检查并请求位置权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                return;
            }else {
                updateTextView("有GPS权限");
                Log.d("GPS", "有GPS权限");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                // 权限被拒绝，可以提示用户需要权限才能使用该功能
                Log.d("GPS", "位置被拒绝没有权限");
                updateTextView("位置被拒绝没有权限");
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("GPS", "onLocationChanged");
        updateTextView("onLocationChanged");
        String locationInfo = "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude();
        if (location.hasAltitude()) {
            locationInfo += ", Altitude: " + location.getAltitude();
        }
        if (location.hasAccuracy()) {
            locationInfo += ", Accuracy: " + location.getAccuracy();
        }
        locationText.setText(locationInfo);
    }

    public void updateTextView(String text){
        String tempText = locationTextView.getText().toString();
        locationTextView.setText(tempText+"\n"+text);
    }
}