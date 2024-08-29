package com.firstproject.androiddemofx.month1.week2;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class activity_day_1_2_1 extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day121);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btn1211 = findViewById(R.id.btn1211);
        btn1211.setOnClickListener(v -> Toast.makeText(activity_day_1_2_1.this, "点击了LinearLayout中的按钮", Toast.LENGTH_SHORT).show());

        Button btn1212 = findViewById(R.id.btnOnClick1212);
        btn1212.setOnClickListener(v -> Toast.makeText(activity_day_1_2_1.this, "点击了RelativeLayout中的按钮", Toast.LENGTH_SHORT).show());

        Button btn1213 = findViewById(R.id.btnOnLongClick1213);
        btn1213.setOnLongClickListener(v -> {
            Toast.makeText(activity_day_1_2_1.this, "长按了RelativeLayout中的按钮", Toast.LENGTH_SHORT).show();
            return true;
        });

        Button btn1214=findViewById(R.id.btnOnTouch1214);
        btn1214.setOnTouchListener((v, event) -> {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    Toast.makeText(activity_day_1_2_1.this, "按下操作", Toast.LENGTH_SHORT).show();
                    break;
                case MotionEvent.ACTION_MOVE:
                    Toast.makeText(activity_day_1_2_1.this, "滑动操作", Toast.LENGTH_SHORT).show();
                    break;
                case  MotionEvent.ACTION_UP:
                    Toast.makeText(activity_day_1_2_1.this, "抬起操作", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        });
    }
}