package com.firstproject.androiddemofx.month4;

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
import com.firstproject.androiddemofx.month4.week1.Activity_Week_4_1;

import java.util.ArrayList;

public class Activity_Month_4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_month4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView lv4 = (ListView) findViewById(R.id.list_view_month_4);
        ArrayList<ModelWeek> weekList=new ArrayList<>();
        weekList.add(new ModelWeek(R.drawable.east,"providerDemo"));

        ListViewWeekAdapt weeksAdapt = new ListViewWeekAdapt(this, R.layout.listview_item_week, weekList);
        lv4.setAdapter(weeksAdapt);
        lv4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.week);
                String weekStr = tv.getText().toString();
                if(weekStr=="providerDemo"){
                    Intent intent = new Intent(Activity_Month_4.this, Activity_Week_4_1.class);
                    startActivity(intent);
                }
            }
        });
    }
}