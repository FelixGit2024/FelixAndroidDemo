package com.firstproject.androiddemofx.month2.week4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

import java.util.ArrayList;
import java.util.List;

public class Activity_Day_2_4_5 extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_CHANGE_WIFI_STATE_PERMISSION = 2;
    private WifiManager wifiManager;
    private Button startWIFI,stopWIFI;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> wifiList;
    private BroadcastReceiver wifiScanReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day245);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startWIFI = findViewById(R.id.startWIFI);
        stopWIFI = findViewById(R.id.stopWIFI);
        listView = findViewById(R.id.listView);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        wifiList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wifiList);
        listView.setAdapter(adapter);

        startWIFI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(true);
                }
                startScan();
            }
        });

        stopWIFI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopScan();
            }
        });

        wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                    checkPermission();
                    List<ScanResult> scanResults = wifiManager.getScanResults();
                    wifiList.clear();
                    for (ScanResult result : scanResults) {
                        wifiList.add(result.SSID + " (" + result.capabilities + ")");
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        };

        // 注册接收已配对设备的广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//接收已配对设备的广播
        registerReceiver(wifiScanReceiver, filter);

    }

    private void startScan() {
        if (!wifiManager.isWifiEnabled()) wifiManager.setWifiEnabled(true);
        wifiManager.startScan();
    }

    private void stopScan() {
        // 这里可以根据需要进行停止搜索的操作，目前没有具体的方法来完全停止系统的 Wi-Fi 扫描，但可以忽略后续的扫描结果
        if (wifiManager!=null && wifiManager.isWifiEnabled()) wifiManager.setWifiEnabled(false);

        wifiList.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiScanReceiver);
    }


    public void checkPermission(){
        // 检查并请求位置权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                return;
            }else {
//                Log.d(TAG, "有权限");
            }
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CHANGE_WIFI_STATE)!= PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.CHANGE_WIFI_STATE}, REQUEST_CHANGE_WIFI_STATE_PERMISSION);
            }
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限已授予，开始搜索
            } else {
                // 权限被拒绝，提示用户
                Toast.makeText(this, "需要位置权限来打开设备wifi", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CHANGE_WIFI_STATE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限已授予，开始搜索
            } else {
                // 权限被拒绝，提示用户
                Toast.makeText(this, "需要wifi修改权限", Toast.LENGTH_SHORT).show();
            }
        }
    }
}