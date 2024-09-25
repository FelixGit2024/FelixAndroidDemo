package com.firstproject.androiddemofx.month2.week3;

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

public class Activity_Week_2_3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_week23);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView lv23=findViewById(R.id.list_view_2_3);
        ArrayList<ModelTask> taskList=new ArrayList<>();
        taskList.add(new ModelTask("  1  ","传感器信息"));
        taskList.add(new ModelTask("  2  ","光线传感器"));
        taskList.add(new ModelTask("  3  ","距离传感器"));
        taskList.add(new ModelTask("  4  ","加速度传感器"));
        taskList.add(new ModelTask("  5  ","陀螺仪传感器"));
        taskList.add(new ModelTask("  6  ","地磁传感器"));
        taskList.add(new ModelTask("  7  ","重力传感器"));

        ListViewTaskAdapt taskAdapt = new ListViewTaskAdapt(this, R.layout.listview_item_task, taskList);
        lv23.setAdapter(taskAdapt);
        lv23.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = view.findViewById(R.id.taskNum);
                String taskNum = tv.getText().toString();
                if(taskNum=="  1  "){
                    startActivity(new Intent(Activity_Week_2_3.this, Activity_Day_2_3_1.class));
                } else if (taskNum=="  2  ") {
                    startActivity(new Intent(Activity_Week_2_3.this, Activity_Day_2_3_2.class));
                }else if (taskNum=="  3  ") {
                    startActivity(new Intent(Activity_Week_2_3.this, Activity_Day_2_3_3.class));
                }else if (taskNum=="  4  ") {
                    startActivity(new Intent(Activity_Week_2_3.this, Activity_Day_2_3_4.class));
                }else if (taskNum=="  5  ") {
                    startActivity(new Intent(Activity_Week_2_3.this, Activity_Day_2_3_5.class));
                }else if (taskNum=="  6  ") {
                    startActivity(new Intent(Activity_Week_2_3.this, Activity_Day_2_3_6.class));
                }else if (taskNum=="  7  ") {
                    startActivity(new Intent(Activity_Week_2_3.this, Activity_Day_2_3_7.class));
                }
            }
        });
    }
}