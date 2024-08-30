package com.firstproject.androiddemofx.month1.week2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class activity_b extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day123b);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intentFromA = getIntent();
        String aToB = intentFromA.getStringExtra("A_to_B");

        TextView tvIntentB1232=findViewById(R.id.tvIntentB1232);
        tvIntentB1232.setText(aToB);

        Button btnIntentB1=findViewById(R.id.btnIntentB1);
        btnIntentB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent=new Intent();
                EditText viewById = (EditText) findViewById(R.id.edtIntentB1231);
                String val=viewById.getText().toString();

                if (val!=null && val!=""){
                    returnIntent.putExtra("B_to_A",val);
                    setResult(Activity.RESULT_OK,returnIntent);
                }
                finish();
            }
        });
    }
}