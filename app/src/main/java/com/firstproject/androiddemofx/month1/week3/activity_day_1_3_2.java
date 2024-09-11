package com.firstproject.androiddemofx.month1.week3;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class activity_day_1_3_2 extends AppCompatActivity {
    private boolean isBound=false;
    private TextView tvService1321;
    private TextView tvService1322;
    private BroadcastReceiver startServiceStatusReceiverTest=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = intent.getStringExtra("status");
            String currentText = tvService1321.getText().toString();
            tvService1321.setText(currentText+status);
            int scrollAmount = tvService1321.getLayout().getLineTop(tvService1321.getLineCount()) - tvService1321.getHeight();
            if (scrollAmount > 0) {
                tvService1321.scrollTo(0, scrollAmount);
            } else {
                tvService1321.scrollTo(0, 0);
            }
        }
    };
    private BroadcastReceiver bindServiceStatusReceiverTest=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = intent.getStringExtra("status");
            String currentText = tvService1322.getText().toString();
            tvService1322.setText(currentText+status);
            int scrollAmount = tvService1322.getLayout().getLineTop(tvService1322.getLineCount()) - tvService1322.getHeight();
            if (scrollAmount > 0) {
                tvService1322.scrollTo(0, scrollAmount);
            } else {
                tvService1322.scrollTo(0, 0);
            }
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, android.os.IBinder service) {
            serviceForBindDay132.MyBinder binder = (serviceForBindDay132.MyBinder) service;
            Log.d("MainActivity", "Service connected");
            String currentText = tvService1322.getText().toString();
            tvService1322.setText(currentText+"Service connected\n");
            int scrollAmount = tvService1321.getLayout().getLineTop(tvService1322.getLineCount()) - tvService1322.getHeight();
            if (scrollAmount > 0) {
                tvService1322.scrollTo(0, scrollAmount);
            } else {
                tvService1322.scrollTo(0, 0);
            }
            binder.service_connect_Activity();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            String currentText = tvService1322.getText().toString();
            tvService1322.setText(currentText+"Service Disconnected\n");
            int scrollAmount = tvService1322.getLayout().getLineTop(tvService1322.getLineCount()) - tvService1322.getHeight();
            if (scrollAmount > 0) {
                tvService1322.scrollTo(0, scrollAmount);
            } else {
                tvService1322.scrollTo(0, 0);
            }
            Log.d("MainActivity", "Service disconnected");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day132);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvService1321=findViewById(R.id.tvService1321);
        tvService1321.setVerticalScrollBarEnabled(true);

        tvService1322=findViewById(R.id.tvService1322);
        tvService1322.setVerticalScrollBarEnabled(true);

        IntentFilter start_filter = new IntentFilter("START_SERVICE_LIFECYCLE_BROADCAST_TEST");
        IntentFilter bind_filter = new IntentFilter("BIND_SERVICE_LIFECYCLE_BROADCAST_TEST");
        registerReceiver(startServiceStatusReceiverTest, start_filter);
        registerReceiver(bindServiceStatusReceiverTest, bind_filter);

        Button startServiceButton=findViewById(R.id.btnService1321);
        Button stopServiceButton=findViewById(R.id.btnService1322);
        Button bindServiceButton=findViewById(R.id.btnService1323);
        Button unbindServiceButton=findViewById(R.id.btnService1324);

        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_day_1_3_2.this, serviceForStartDay132.class);
                startService(intent);
            }
        });

        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_day_1_3_2.this, serviceForStartDay132.class);
                stopService(intent);
            }
        });

        bindServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_day_1_3_2.this, serviceForBindDay132.class);
                bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                isBound = true;
            }
        });

        unbindServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBound) {
                    unbindService(serviceConnection);
                    isBound=false;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(startServiceStatusReceiverTest);
        unregisterReceiver(bindServiceStatusReceiverTest);
//        if (serviceConnection!=null){
//            unbindService(serviceConnection);
//            serviceConnection=null;
//        }
        if (isBound) {
            unbindService(serviceConnection);
            isBound=false;
        }
    }
}