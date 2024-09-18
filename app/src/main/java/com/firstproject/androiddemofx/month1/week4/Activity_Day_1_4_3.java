package com.firstproject.androiddemofx.month1.week4;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class Activity_Day_1_4_3 extends AppCompatActivity {
    private TextView textView;
    private Button btnANR;
    private Button btnCrash;
    private Button btnException;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day143);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView = findViewById(R.id.textView);
        btnANR = findViewById(R.id.btnANR);
        btnCrash = findViewById(R.id.btnCrash);
        btnException = findViewById(R.id.btnException);

        btnANR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerANR();
            }
        });
        btnCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerCrash();
            }
        });
        btnException.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerException();
            }
        });
    }

    private void triggerException() {
        try {
            int result = 10 / 0;
        } catch (ArithmeticException e) {
            textView.setText("Exception: " + e.getMessage());
        }
    }

    private void triggerCrash() {
        Object nullObject = null;
        nullObject.toString();
    }

    private void triggerANR(){
        try {
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}