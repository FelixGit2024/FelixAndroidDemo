package com.firstproject.androiddemofx.month1.week3;

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

public class Activity_Week_1_3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_week13);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView lv13=findViewById(R.id.list_view_1_3);
        ArrayList<ModelDay> dayList=new ArrayList<>();
        dayList.add(new ModelDay(R.drawable.number_b_1,"1-3-1","四大组件-Service\n生命周期","已完成"));
        dayList.add(new ModelDay(R.drawable.number_b_2,"1-3-2","任务与输出\nActivity\nService\nBroadcast","已完成"));
        ListViewDayAdapt dayAdapt = new ListViewDayAdapt(this, R.layout.listview_item_day, dayList);
        lv13.setAdapter(dayAdapt);

        lv13.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.day);
                String dayStr = tv.getText().toString();
                if(dayStr=="1-3-1"){
                    Intent intent = new Intent(Activity_Week_1_3.this, Activity_Day_1_3_1.class);
                    startActivity(intent);
                } else if (dayStr=="1-3-2") {
                    Intent intent = new Intent(Activity_Week_1_3.this, Activity_Day_1_3_2.class);
                    startActivity(intent);
                }
            }
        });
    }
}