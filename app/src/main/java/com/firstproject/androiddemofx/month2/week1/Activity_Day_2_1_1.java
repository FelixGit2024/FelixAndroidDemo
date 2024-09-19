package com.firstproject.androiddemofx.month2.week1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class Activity_Day_2_1_1 extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button btnPermission, btnCheck, btnClose, btnOpen, btnSet;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day211);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnPermission = findViewById(R.id.btn_permission);
        btnCheck = findViewById(R.id.btn_check);
        btnClose = findViewById(R.id.btn_close);
        btnOpen = findViewById(R.id.btn_open);
        btnSet = findViewById(R.id.btn_set);

        editText = findViewById(R.id.edit_number);

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentScreenBrightness = getCurrentScreenBrightness();
                Log.i(TAG, "onClick: currentScreenBrightness" + currentScreenBrightness);
                Toast.makeText(Activity_Day_2_1_1.this, "设置前屏幕亮度："+currentScreenBrightness, Toast.LENGTH_SHORT).show();

                String s = editText.getText().toString();
                if (TextUtils.isEmpty(s)){
                    Toast.makeText(Activity_Day_2_1_1.this, "请输入0-255的数值", Toast.LENGTH_SHORT).show();
                    return;
                }
                int i = Integer.parseInt(s);
                if (i < 0 || i > 255) {
                    Toast.makeText(Activity_Day_2_1_1.this, "请输入0-255的数值", Toast.LENGTH_SHORT).show();
                    return;
                }
                setScreenBrightness(i);
                Toast.makeText(Activity_Day_2_1_1.this, "设置后屏幕亮度："+getCurrentScreenBrightness(), Toast.LENGTH_SHORT).show();
            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutoScreenBrightness();
                Toast.makeText(Activity_Day_2_1_1.this, "已开启自动亮度", Toast.LENGTH_SHORT).show();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAutoScreenBrightness();
                Toast.makeText(Activity_Day_2_1_1.this, "已去除自动亮度", Toast.LENGTH_SHORT).show();
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Activity_Day_2_1_1.this, isAutoScreenBrightness() ? "自动亮度" : "未设置自动亮度", Toast.LENGTH_SHORT).show();
            }
        });

        btnPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCanWrite()) {
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
//                            Uri.parse("package:" + getPackageName()));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS), 1101);
                }
                else
                    Toast.makeText(Activity_Day_2_1_1.this, "权限申请成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //获取当前屏幕亮度
    private int getCurrentScreenBrightness() {
        try {
            int anInt = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            return anInt;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
    //设置屏幕亮度
    private void setScreenBrightness(int brightness) {
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
    }

    private void openAutoScreenBrightness() {
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    private void closeAutoScreenBrightness() {
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    //SCREEN_BRIGHTNESS_MODE判断是否设置自动亮度
    private boolean isAutoScreenBrightness() {
        try {
            int mode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
            return mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
    //    检查当前应用是否具有写入系统设置的权限
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isCanWrite() {
        return Settings.System.canWrite(this);
    }
}