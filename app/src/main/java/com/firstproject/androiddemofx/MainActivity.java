package com.firstproject.androiddemofx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.month1.activity_month_1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnM1;
    private Button btnM2;
    private Button btnM3;
    private Button btnM4;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnM1=(Button)findViewById(R.id.btnM1);
        btnM1.setOnClickListener(MainActivity.this);
        btnM2=(Button)findViewById(R.id.btnM2);
        btnM2.setOnClickListener(MainActivity.this);
        btnM3=(Button)findViewById(R.id.btnM3);
        btnM3.setOnClickListener(MainActivity.this);
        btnM4=(Button)findViewById(R.id.btnM4);
        btnM4.setOnClickListener(MainActivity.this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnM1){
            Intent intent=new Intent(MainActivity.this, activity_month_1.class);
            startActivity(intent);
        } else if (v.getId()==R.id.btnM2) {

        } else if (v.getId()==R.id.btnM3) {

        } else if (v.getId()==R.id.btnM4) {

        }
    }
}