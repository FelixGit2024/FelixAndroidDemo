package com.firstproject.androiddemofx.month2.week1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class Activity_Day_2_1_8 extends AppCompatActivity {
    private Button btnLightSwitch;
    private Boolean isTorchOn;
    private LinearLayout layout;
    private TextView lightStatus;
    private CameraManager mCameraManager;
    private String mCameraId;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day218);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLight), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnLightSwitch = findViewById(R.id.btn_light_switch);
        layout=findViewById(R.id.mainLight);
        lightStatus=findViewById(R.id.lightStatus);

        isTorchOn = false;

        //判断设备是否支持闪光灯
        Boolean isFlashAvailable = getApplicationContext()
                .getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!isFlashAvailable) {
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle("Error !!");
            alert.setMessage("Your device doesn't support flash light!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {                    // closing the application
                            finish();
                            System.exit(0);
                        }
                    });
            alert.show();
        } else {
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle("Right !!");
            alert.setMessage("Your device does support flash light!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {                    // closing the application
//                            finish();
//                            System.exit(0);
                        }
                    });
            alert.show();
        }

        //获取CameraManager实例
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            //打开指定摄像头，通常后置摄像头id为0
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (Exception e) {
            e.printStackTrace();
        }

        //设置按钮开关
        btnLightSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isTorchOn) {
                        turnOffFlashLight();
                        isTorchOn = false;
                    } else {
                        turnOnFlashLight();
                        isTorchOn = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //打开闪光灯
    public void turnOnFlashLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //选择摄像头
                mCameraManager.setTorchMode(mCameraId, true);
                layout.setBackgroundColor(Color.WHITE);
                lightStatus.setText("手电筒状态：开");
                playOnOffSound();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //关闭闪光灯
    public void turnOffFlashLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, false);
                layout.setBackgroundColor(Color.BLACK);
                lightStatus.setText("手电筒状态：关");
                playOnOffSound();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //播放音频文件
    private void playOnOffSound() {
        mp = MediaPlayer.create(this, R.raw.skypemp3);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {                // TODO Auto-generated method stub
                mp.release();
            }
        });
        mp.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isTorchOn) {
            turnOffFlashLight();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isTorchOn) {
            turnOffFlashLight();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTorchOn) {
            turnOnFlashLight();
        }
    }
}

