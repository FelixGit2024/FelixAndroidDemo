package com.firstproject.androiddemofx.month2.week1;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class Activity_Day_2_1_9 extends AppCompatActivity {

    private Button btnLight;
    private CameraManager mCameraManager;
    private String mCameraId;
    private boolean isLight=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day219);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnLight=findViewById(R.id.btnLight);
        mCameraManager= (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String[] cameraIds = mCameraManager.getCameraIdList();
            // 假设我们选择第一个摄像头（通常0代表后置摄像头，但具体情况可能因设备而异）
            mCameraId = cameraIds[0];
        } catch (Exception e) {
            // 处理异常，例如权限不足或设备错误
            e.printStackTrace();
        }

        btnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLight){
                    //关
//                    Toast.makeText(Activity_Day_2_1_9.this, "关闭闪光灯", Toast.LENGTH_SHORT).show();
                    turnOffFlashLight();
                    isLight=false;
                }else {
                    //开
//                    Toast.makeText(Activity_Day_2_1_9.this, "开启闪光灯", Toast.LENGTH_SHORT).show();
                    turnOnFlashLight();
                    isLight=true;
                }
            }
        });
    }
    private void turnOffFlashLight() {
        try {
            if (mCameraManager!= null && mCameraId!= null) {
                mCameraManager.setTorchMode(mCameraId, false);
            }
        }catch (Exception e) {// 处理设置闪光灯模式失败的情况
            e.printStackTrace();
        }
    }
    private void turnOnFlashLight() {
        try {
            if (mCameraManager!= null && mCameraId!= null) {
                mCameraManager.setTorchMode(mCameraId, true);
            }
        }catch (Exception e) {// 处理设置闪光灯模式失败的情况
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isLight) {
            turnOffFlashLight();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isLight) {
            turnOffFlashLight();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLight) {
            turnOnFlashLight();
        }
    }
}