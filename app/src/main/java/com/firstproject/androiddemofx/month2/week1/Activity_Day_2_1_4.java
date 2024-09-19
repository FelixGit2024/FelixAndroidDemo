package com.firstproject.androiddemofx.month2.week1;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class Activity_Day_2_1_4 extends AppCompatActivity {

    private ImageView imageMove;
    private float startX;
    private float startY;
    private float dx;
    private float dy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day214);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imageMove=findViewById(R.id.imageMove);
        imageMove.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //记录触摸的起始坐标（startX和startY）
                        startX = event.getRawX();
                        startY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //计算手指移动的距离（dx和dy）
                        dx = event.getRawX() - startX;
                        dy = event.getRawY() - startY;
                        // 然后通过setTranslationX和setTranslationY方法将ImageView在水平和垂直方向上移动相应的距离
                        imageMove.setTranslationX(imageMove.getTranslationX() + dx);
                        imageMove.setTranslationY(imageMove.getTranslationY() + dy);
                        // 并更新起始坐标（startX和startY）
                        startX = event.getRawX();
                        startY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                //表示触摸事件已被处理，不会再传递给其他处理逻辑
                return true;
            }
        });
    }
}