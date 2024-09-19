package com.firstproject.androiddemofx.month2.week1;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class Activity_Day_2_1_7 extends AppCompatActivity {
    private Button btnVibratorStart;
    private Button btnVibratorStop;
    private Vibrator mVibrator;
    private long[] pattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day217);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnVibratorStart=findViewById(R.id.btnVibratorStart);
        btnVibratorStop=findViewById(R.id.btnVibratorStop);
        mVibrator=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (!mVibrator.hasVibrator()){
            Toast.makeText(this, "该设备不含振动器硬件", Toast.LENGTH_SHORT).show();
        }

        pattern=new long[]{400,400};
        btnVibratorStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVibrator.vibrate(pattern,0);
            }
        });
        btnVibratorStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVibrator.cancel();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            pattern[0]*=2;
            pattern[1]*=2;
            mVibrator.cancel();
            mVibrator.vibrate(pattern,0);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            pattern[0]/=2;
            pattern[1]/=2;
            mVibrator.cancel();
            mVibrator.vibrate(pattern,0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mVibrator!=null) mVibrator.cancel();
        super.onDestroy();
    }
}