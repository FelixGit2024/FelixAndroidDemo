package com.firstproject.androiddemofx.month1.week1;

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
import com.firstproject.androiddemofx.listviewadapt.ListViewDayAdapt;
import com.firstproject.androiddemofx.model.ModelDay;
import com.firstproject.androiddemofx.month1.week2.Activity_Week_1_2;
import com.firstproject.androiddemofx.month1.week3.Activity_Week_1_3;
import com.firstproject.androiddemofx.month1.week4.Activity_Week_1_4;

import java.util.ArrayList;

public class Activity_Week_1_1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_week11);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView lv11 = (ListView) findViewById(R.id.list_view_1_1);
        ArrayList<ModelDay> dayList=new ArrayList<>();
        dayList.add(new ModelDay(R.drawable.number_b_1,"1-1-1","Android开发环境搭建","已完成"));
        dayList.add(new ModelDay(R.drawable.number_b_2,"1-1-2","调试基础-adb","已完成"));
        dayList.add(new ModelDay(R.drawable.number_b_3,"1-1-3","调试基础-Logcat","已完成"));
        dayList.add(new ModelDay(R.drawable.number_b_4,"1-1-4","程序清单 AndroidMainmest","已完成"));
        dayList.add(new ModelDay(R.drawable.number_b_5,"1-1-5","四大组件-Activity","已完成"));
        ListViewDayAdapt dayAdapt = new ListViewDayAdapt(this, R.layout.listview_item_day, dayList);
        lv11.setAdapter(dayAdapt);

        lv11.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.day);
                String dayStr = tv.getText().toString();
                if(dayStr=="1-1-1"){
                    Intent intent = new Intent(Activity_Week_1_1.this, Activity_Day_1_1_1.class);
                    startActivity(intent);
                } else if (dayStr=="1-1-2") {
                    Intent intent = new Intent(Activity_Week_1_1.this, Activity_Week_1_2.class);
                    startActivity(intent);
                }else if (dayStr=="1-1-3") {
                    Intent intent = new Intent(Activity_Week_1_1.this, Activity_Week_1_3.class);
                    startActivity(intent);
                }else if (dayStr=="1-1-4") {
                    Intent intent = new Intent(Activity_Week_1_1.this, Activity_Week_1_4.class);
                    startActivity(intent);
                }else if (dayStr=="1-1-5") {
                    Intent intent = new Intent(Activity_Week_1_1.this, Activity_Week_1_4.class);
                    startActivity(intent);
                }
            }
        });
    }
}