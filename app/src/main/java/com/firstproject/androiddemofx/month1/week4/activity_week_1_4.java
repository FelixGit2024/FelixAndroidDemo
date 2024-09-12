package com.firstproject.androiddemofx.month1.week4;

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

public class activity_week_1_4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_week14);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView lv14=findViewById(R.id.list_view_1_4);
        ArrayList<ModelDay> dayList=new ArrayList<>();
        dayList.add(new ModelDay(R.drawable.number_b_1,"1-4-1","异步处理-计时器","已完成"));
        dayList.add(new ModelDay(R.drawable.number_b_2,"1-4-2","数据存储","已完成"));
        dayList.add(new ModelDay(R.drawable.number_b_3,"1-4-3","异常处理","已完成"));
        ListViewDayAdapt dayAdapt = new ListViewDayAdapt(this, R.layout.listview_item_day, dayList);
        lv14.setAdapter(dayAdapt);
        lv14.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.day);
                String dayStr = tv.getText().toString();
                if(dayStr=="1-4-1"){
                    Intent intent = new Intent(activity_week_1_4.this, activity_day_1_4_1.class);
                    startActivity(intent);
                } else if (dayStr=="1-4-2") {
                    Intent intent = new Intent(activity_week_1_4.this, activity_day_1_4_2.class);
                    startActivity(intent);
                }
            }
        });
    }
}