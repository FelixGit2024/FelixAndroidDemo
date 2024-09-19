package com.firstproject.androiddemofx.month2.week1;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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

public class Activity_Week_2_1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_week21);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView lv21=findViewById(R.id.list_view_2_1);
        ArrayList<ModelTask> taskList=new ArrayList<>();
        taskList.add(new ModelTask("  1  ","屏幕亮度基础设置"));
        taskList.add(new ModelTask("  2  ","屏幕亮度自动变化"));
        taskList.add(new ModelTask("  3  ","音量键切换图片"));
        taskList.add(new ModelTask("  4  ","手指滑动图片"));
        taskList.add(new ModelTask("  5  ","按键响应"));
        taskList.add(new ModelTask("  6  ","振动器"));
        taskList.add(new ModelTask("  7  ","按键控制振动器频率"));
        taskList.add(new ModelTask("  8  ","手电筒开关"));
        taskList.add(new ModelTask("  9  ","手电筒开关练习"));

        ListViewTaskAdapt taskAdapt = new ListViewTaskAdapt(this, R.layout.listview_item_task, taskList);
        lv21.setAdapter(taskAdapt);
        lv21.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = view.findViewById(R.id.taskNum);
                String taskNum = tv.getText().toString();
                if(taskNum=="  1  "){
                    startActivity(new Intent(Activity_Week_2_1.this,Activity_Day_2_1_1.class));
                } else if (taskNum=="  2  ") {
                    Intent intent=new Intent(Activity_Week_2_1.this,Activity_Day_2_1_2.class);
                    intent.putExtra("CurrentScreenBrightness",getCurrentScreenBrightness());
                    startActivity(intent);
                }else if (taskNum=="  3  ") {
                    startActivity(new Intent(Activity_Week_2_1.this,Activity_Day_2_1_3.class));
                }else if (taskNum=="  4  ") {
                    startActivity(new Intent(Activity_Week_2_1.this,Activity_Day_2_1_4.class));
                }else if (taskNum=="  5  ") {
                    startActivity(new Intent(Activity_Week_2_1.this,Activity_Day_2_1_5.class));
                }else if (taskNum=="  6  ") {
                    startActivity(new Intent(Activity_Week_2_1.this, Activity_Day_2_1_6.class));
                }else if (taskNum=="  7  ") {
                    startActivity(new Intent(Activity_Week_2_1.this, Activity_Day_2_1_7.class));
                }else if (taskNum=="  8  ") {
                    startActivity(new Intent(Activity_Week_2_1.this, Activity_Day_2_1_8.class));
                }else if (taskNum=="  9  ") {
                    startActivity(new Intent(Activity_Week_2_1.this, Activity_Day_2_1_9.class));
                }
            }
        });
    }

    private int getCurrentScreenBrightness() {
        try {
            int anInt = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            return anInt;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}