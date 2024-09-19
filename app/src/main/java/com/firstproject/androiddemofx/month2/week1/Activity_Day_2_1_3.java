package com.firstproject.androiddemofx.month2.week1;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class Activity_Day_2_1_3 extends AppCompatActivity {
    private ImageView imgToggle;
    private int[] imageArr;
    private int index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day213);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imgToggle=findViewById(R.id.imgToggle);
        imageArr=new int[]{R.drawable.numeric_circle_1,
                R.drawable.numeric_circle_2,
                R.drawable.numeric_circle_3,
                R.drawable.numeric_circle_4,
                R.drawable.numeric_circle_5};
        imgToggle.setImageResource(imageArr[index]);
    }

    // 自定义音量触发事件
    //方法一：重写onKeyDown方法和onKeyUp方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            // 处理音量上键按下事件
            index=(index+1)%imageArr.length;
            imgToggle.setImageResource(imageArr[index]);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            // 处理音量下键按下事件
            index=(index-1+imageArr.length)%imageArr.length;
            imgToggle.setImageResource(imageArr[index]);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

//    方法二：重写dispatchKeyEvent方法
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        int keyCode = event.getKeyCode();
//        int action = event.getAction();
//        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP && action==KeyEvent.ACTION_DOWN){
//            index=(index+1)%imageArr.length;
//        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN && action==KeyEvent.ACTION_DOWN) {
//            index=(index-1+imageArr.length)%imageArr.length;
//        }
//        imgToggle.setImageResource(imageArr[index]);
//        return super.dispatchKeyEvent(event);
//    }
}