package com.firstproject.androiddemofx.month1;

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
import com.firstproject.androiddemofx.listviewadapt.ListViewWeekAdapt;
import com.firstproject.androiddemofx.model.ModelWeek;
import com.firstproject.androiddemofx.month1.week1.Activity_Week_1_1;
import com.firstproject.androiddemofx.month1.week2.Activity_Week_1_2;
import com.firstproject.androiddemofx.month1.week3.Activity_Week_1_3;
import com.firstproject.androiddemofx.month1.week4.Activity_Week_1_4;

import java.util.ArrayList;

public class Activity_Month_1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_month1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_month1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView lv1 = (ListView) findViewById(R.id.list_view_month_1);
        ArrayList<ModelWeek> weekList=new ArrayList<>();
        weekList.add(new ModelWeek(R.drawable.numeric_box_1,"第一周"));
        weekList.add(new ModelWeek(R.drawable.numeric_box_2,"第二周"));
        weekList.add(new ModelWeek(R.drawable.numeric_box_3,"第三周"));
        weekList.add(new ModelWeek(R.drawable.numeric_box_4,"第四周"));
        ListViewWeekAdapt weeksAdapt = new ListViewWeekAdapt(this, R.layout.listview_item_week, weekList);
        lv1.setAdapter(weeksAdapt);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.week);
                String weekStr = tv.getText().toString();
                if(weekStr=="第一周"){
                    Intent intent = new Intent(Activity_Month_1.this, Activity_Week_1_1.class);
                    startActivity(intent);
                } else if (weekStr=="第二周") {
                    Intent intent = new Intent(Activity_Month_1.this, Activity_Week_1_2.class);
                    startActivity(intent);
                }else if (weekStr=="第三周") {
                    Intent intent = new Intent(Activity_Month_1.this, Activity_Week_1_3.class);
                    startActivity(intent);
                }else if (weekStr=="第四周") {
                    Intent intent = new Intent(Activity_Month_1.this, Activity_Week_1_4.class);
                    startActivity(intent);
                }
            }
        });
    }
}