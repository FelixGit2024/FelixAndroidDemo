package com.firstproject.androiddemofx.month1.week4;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

import java.util.Timer;
import java.util.TimerTask;

public class Activity_Day_1_4_1 extends AppCompatActivity {
    private ImageView imgChange1411;
    private TextView tvTime1411;
    private Button btnStart1411, btnStop1412, btnReset1413;
    private boolean isRunning = false;
    private long startTime = 0;
    private long elapsedTime = 0;
    private Handler mHandlerTimer = new Handler();
    private Handler mHandlerImg;
    private Runnable updateTimerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                elapsedTime = System.currentTimeMillis() - startTime; //过去时间
                int seconds = (int) (elapsedTime / 1000);
                tvTime1411.setText(String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60));
                mHandlerTimer.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day141);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imgChange1411 = findViewById(R.id.imgChange1411);
        tvTime1411 = findViewById(R.id.tvTime1411);
        //定义切换的图片的数组id
        int imgids[] = new int[]{R.drawable.light, R.drawable.dark};
        final int[] imgStart = {0};
        ImageView imgChange1411 = findViewById(R.id.imgChange1411);
        mHandlerImg = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0x123) {
                    imgChange1411.setImageResource(imgids[imgStart[0]++ % 2]);
                }
            }
        };
        //使用定时器,每隔200毫秒让handler发送一个空信息
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mHandlerImg.sendEmptyMessage(0x123);
            }
        }, 0, 400);


        btnStart1411 = findViewById(R.id.btnStart1411);
        btnStop1412 = findViewById(R.id.btnStop1412);
        btnReset1413 = findViewById(R.id.btnReset1413);

        btnStart1411.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    startTime = System.currentTimeMillis() - elapsedTime;
                    isRunning = true;
                    mHandlerTimer.post(updateTimerRunnable);
                }
            }
        });

        btnReset1413.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = false;
                elapsedTime = 0;
                tvTime1411.setText("00:00:00");
            }
        });

        btnStop1412.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = false;
            }
        });
    }
}