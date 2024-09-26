package com.firstproject.androiddemofx.month2.week4;

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

public class Activity_Week_2_4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_week24);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView lv24=findViewById(R.id.list_view_2_4);
        ArrayList<ModelTask> taskList=new ArrayList<>();
        taskList.add(new ModelTask("  1  ","USB连接状态"));
        taskList.add(new ModelTask("  2  ","OTG连接和读取"));
        taskList.add(new ModelTask("  3  ","电池状态"));
        taskList.add(new ModelTask("  4  ","蓝牙"));

        ListViewTaskAdapt taskAdapt = new ListViewTaskAdapt(this, R.layout.listview_item_task, taskList);
        lv24.setAdapter(taskAdapt);
        lv24.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = view.findViewById(R.id.taskNum);
                String taskNum = tv.getText().toString();
                if(taskNum=="  1  "){
                    startActivity(new Intent(Activity_Week_2_4.this, Activity_Day_2_4_1.class));
                } else if (taskNum=="  2  ") {
                    startActivity(new Intent(Activity_Week_2_4.this, Activity_Day_2_4_2.class));
                }else if (taskNum=="  3  ") {
                    startActivity(new Intent(Activity_Week_2_4.this, Activity_Day_2_4_3.class));
                }else if (taskNum=="  4  ") {
                    startActivity(new Intent(Activity_Week_2_4.this, Activity_Day_2_4_4.class));
                }
            }
        });
    }
}