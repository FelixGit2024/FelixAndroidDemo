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
        taskList.add(new ModelTask("  1  ","Camera2相机预览"));
        taskList.add(new ModelTask("  2  ","Camera2相机拍照"));
        taskList.add(new ModelTask("  3  ","Camera2相机测试"));
        taskList.add(new ModelTask("  4  ","相机切换与拍照"));
        taskList.add(new ModelTask("  5  ","AudioManager"));
        taskList.add(new ModelTask("  6  ","MediaRecorder"));



        ListViewTaskAdapt taskAdapt = new ListViewTaskAdapt(this, R.layout.listview_item_task, taskList);
        lv22.setAdapter(taskAdapt);
        lv22.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = view.findViewById(R.id.taskNum);
                String taskNum = tv.getText().toString();
                if(taskNum=="  1  "){
                    startActivity(new Intent(Activity_Week_2_2.this, Activity_Day_2_2_1.class));
                } else if (taskNum=="  2  ") {
                    startActivity(new Intent(Activity_Week_2_2.this, Activity_Day_2_2_2.class));
                }else if (taskNum=="  3  ") {
                    startActivity(new Intent(Activity_Week_2_2.this, Activity_Day_2_2_3.class));
                }else if (taskNum=="  4  ") {
                    startActivity(new Intent(Activity_Week_2_2.this, Activity_Day_2_2_4.class));
                }else if (taskNum=="  5  ") {
                    startActivity(new Intent(Activity_Week_2_2.this, Activity_Day_2_2_5.class));
                }else if (taskNum=="  6  ") {
                    startActivity(new Intent(Activity_Week_2_2.this, Activity_Day_2_2_6.class));
                }
            }
        });
    }
}