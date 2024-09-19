package com.firstproject.androiddemofx.month2.week2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;
import com.firstproject.androiddemofx.listviewadapt.ListViewTaskAdapt;
import com.firstproject.androiddemofx.model.ModelTask;
import com.firstproject.androiddemofx.month2.week1.Activity_Day_2_1_1;
import com.firstproject.androiddemofx.month2.week1.Activity_Day_2_1_3;

import java.util.ArrayList;

public class Activity_Week_2_2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_week22);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView lv22=findViewById(R.id.list_view_2_2);
        ArrayList<ModelTask> taskList=new ArrayList<>();
        taskList.add(new ModelTask("  1  ","屏幕亮度基础设置"));
        taskList.add(new ModelTask("  2  ","屏幕亮度自动变化"));
        taskList.add(new ModelTask("  3  ","音量键切换图片"));
        taskList.add(new ModelTask("  4  ","手指滑动图片"));
        taskList.add(new ModelTask("  5  ","按键响应"));
        taskList.add(new ModelTask("  6  ","振动器"));
        taskList.add(new ModelTask("  7  ","按键控制振动器频率"));
        taskList.add(new ModelTask("  8  ","闪光灯"));

        ListViewTaskAdapt taskAdapt = new ListViewTaskAdapt(this, R.layout.listview_item_task, taskList);
        lv22.setAdapter(taskAdapt);
        lv22.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = view.findViewById(R.id.taskNum);
                String taskNum = tv.getText().toString();
                if(taskNum=="  1  "){
                    startActivity(new Intent(Activity_Week_2_2.this, Activity_Day_2_1_1.class));
                } else if (taskNum=="  3  ") {
                    startActivity(new Intent(Activity_Week_2_2.this, Activity_Day_2_1_3.class));
                }
            }
        });
    }
}