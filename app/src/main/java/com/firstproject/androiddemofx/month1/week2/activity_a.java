package com.firstproject.androiddemofx.month1.week2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class activity_a extends AppCompatActivity {

    TextView tvIntentA1232;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day123a);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvIntentA1232=findViewById(R.id.tvIntentA1232);
        ActivityResultLauncher launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
//                    Log.d("ab", result.getData().getStringExtra("B_to_A").toString());
                    tvIntentA1232.setText(result.getData().getStringExtra("B_to_A").toString());
                }
            }
        });

        Button btnIntentA1231=findViewById(R.id.btnIntentA1231);
        btnIntentA1231.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToB=new Intent(activity_a.this,activity_b.class);
                EditText viewById = (EditText) findViewById(R.id.edtIntentA1231);
                String val = viewById.getText().toString();
                if (val!=null && val!=""){
                    intentToB.putExtra("A_to_B",val);
                    setResult(Activity.RESULT_OK,intentToB);
                }
                launcher.launch(intentToB);
            }
        });

        Button btnIntentA1232 =findViewById(R.id.btnIntentA1232);
        btnIntentA1232.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}