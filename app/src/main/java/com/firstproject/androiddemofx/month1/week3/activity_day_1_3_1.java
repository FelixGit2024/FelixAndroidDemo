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

public class activity_day_1_3_1 extends AppCompatActivity {

    private boolean isBound;
    private TextView tvService1311;
    private TextView tvService1312;

    private BroadcastReceiver startServiceStatusReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = intent.getStringExtra("status");
            String currentText = tvService1311.getText().toString();
            tvService1311.setText(currentText+status);
            int scrollAmount = tvService1311.getLayout().getLineTop(tvService1311.getLineCount()) - tvService1311.getHeight();
            if (scrollAmount > 0) {
                tvService1311.scrollTo(0, scrollAmount);
            } else {
                tvService1311.scrollTo(0, 0);
            }
        }
    };
    private BroadcastReceiver bindServiceStatusReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = intent.getStringExtra("status");
            String currentText = tvService1312.getText().toString();
            tvService1312.setText(currentText+status);
            int scrollAmount = tvService1312.getLayout().getLineTop(tvService1312.getLineCount()) - tvService1312.getHeight();
            if (scrollAmount > 0) {
                tvService1312.scrollTo(0, scrollAmount);
            } else {
                tvService1312.scrollTo(0, 0);
            }
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, android.os.IBinder service) {
            serviceForBindDay131.MyBinder binder = (serviceForBindDay131.MyBinder) service;
            Log.d("MainActivity", "Service connected");
            String currentText = tvService1312.getText().toString();
            tvService1312.setText(currentText+"Service connected\n");
            int scrollAmount = tvService1312.getLayout().getLineTop(tvService1312.getLineCount()) - tvService1312.getHeight();
            if (scrollAmount > 0) {
                tvService1312.scrollTo(0, scrollAmount);
            } else {
                tvService1312.scrollTo(0, 0);
            }
            binder.service_connect_Activity();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            String currentText = tvService1312.getText().toString();
            tvService1312.setText(currentText+"Service Disconnected\n");
            int scrollAmount = tvService1312.getLayout().getLineTop(tvService1312.getLineCount()) - tvService1312.getHeight();
            if (scrollAmount > 0) {
                tvService1312.scrollTo(0, scrollAmount);
            } else {
                tvService1312.scrollTo(0, 0);
            }
            Log.d("MainActivity", "Service disconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day131);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvService1311=findViewById(R.id.tvService1311);
        tvService1311.setVerticalScrollBarEnabled(true);
        tvService1312=findViewById(R.id.tvService1312);
        tvService1312.setVerticalScrollBarEnabled(true);

        IntentFilter start_filter = new IntentFilter("START_SERVICE_LIFECYCLE_BROADCAST");
        IntentFilter bind_filter = new IntentFilter("BIND_SERVICE_LIFECYCLE_BROADCAST");
        registerReceiver(startServiceStatusReceiver, start_filter);
        registerReceiver(bindServiceStatusReceiver, bind_filter);

        Button startServiceButton=findViewById(R.id.btnService1311);
        Button stopServiceButton=findViewById(R.id.btnService1312);
        Button bindServiceButton=findViewById(R.id.btnService1313);
        Button unbindServiceButton=findViewById(R.id.btnService1314);

        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_day_1_3_1.this, serviceForStartDay131.class);
                startService(intent);
            }
        });

        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_day_1_3_1.this, serviceForStartDay131.class);
                stopService(intent);
            }
        });

        bindServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_day_1_3_1.this, serviceForBindDay131.class);
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
        unregisterReceiver(startServiceStatusReceiver);
        unregisterReceiver(bindServiceStatusReceiver);
        if (isBound) {
            unbindService(serviceConnection);
            isBound=false;
        }
    }
}