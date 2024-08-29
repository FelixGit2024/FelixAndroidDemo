package com.firstproject.androiddemofx.month1.week2;

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

import java.util.ArrayList;

public class activity_week_1_2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_week12);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView lv12 = (ListView) findViewById(R.id.list_view_1_2);
        ArrayList<ModelDay> dayList=new ArrayList<>();
        dayList.add(new ModelDay(R.drawable.number_b_1,"1-2-1","Android UI-控件与布局","已完成"));
        dayList.add(new ModelDay(R.drawable.number_b_2,"1-2-2","Android对话框","已完成"));
        dayList.add(new ModelDay(R.drawable.number_b_3,"1-2-3","Android组件交互","已完成"));
        ListViewDayAdapt dayAdapt = new ListViewDayAdapt(this, R.layout.listview_item_day, dayList);
        lv12.setAdapter(dayAdapt);

        lv12.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.day);
                String dayStr = tv.getText().toString();
                if(dayStr=="1-2-1"){
                    Intent intent = new Intent(activity_week_1_2.this, activity_day_1_2_1.class);
                    startActivity(intent);
                } else if (dayStr=="1-2-2") {
                    Intent intent = new Intent(activity_week_1_2.this, activity_day_1_2_2.class);
                    startActivity(intent);
                }else if (dayStr=="1-2-3") {
                    Intent intent = new Intent(activity_week_1_2.this, activity_day_1_2_3.class);
                    startActivity(intent);
                }
            }
        });
    }
}