package com.firstproject.androiddemofx.month4.week1;

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

public class Activity_Week_4_1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_week41);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView lv41=findViewById(R.id.list_view_4_1);
        ArrayList<ModelTask> taskList=new ArrayList<>();
        taskList.add(new ModelTask("  1  ","TrafficStats"));


        ListViewTaskAdapt taskAdapt = new ListViewTaskAdapt(this, R.layout.listview_item_task, taskList);
        lv41.setAdapter(taskAdapt);
        lv41.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = view.findViewById(R.id.taskNum);
                String taskNum = tv.getText().toString();
                if(taskNum=="  1  "){
                    startActivity(new Intent(Activity_Week_4_1.this, Activity_Day_4_1_1.class));
                }
            }
        });
    }
}