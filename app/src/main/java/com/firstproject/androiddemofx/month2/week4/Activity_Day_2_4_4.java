package com.firstproject.androiddemofx.month2.week4;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

public class Activity_Day_2_4_4 extends AppCompatActivity {
    private static final String TAG = "BLUETOOTH";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver receiver;
    private Button startBlueTooth, stopSearch;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day244);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startBlueTooth = findViewById(R.id.startBlueTooth);
        stopSearch = findViewById(R.id.stopSearch);
        listView = findViewById(R.id.listView);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkPermission();
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    String type="";
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (0==device.getType()) type="未知设备类型";
                    else if (1==device.getType()) type="传统蓝牙设备";
                    else if (2==device.getType()) type="低功耗蓝牙设备";
                    else if (3==device.getType()) type="双模设备";

                    deviceList.add(device.getName() + "————>" + device.getAddress()+ "\n" + type);
                    adapter.notifyDataSetChanged();
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    // 搜索完成
                    Log.d(TAG, "搜索完成");
                }
            }
        };

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.d(TAG, "设备不支持蓝牙");
            // 设备不支持蓝牙
            finish();
            return;
        }

        deviceList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceList);
        listView.setAdapter(adapter);

        startBlueTooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                if (!bluetoothAdapter.isEnabled()) {
                    // 请求打开蓝牙
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, 1);
                } else {
                    startDiscovery();
                }
            }
        });

        stopSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDiscovery();
                deviceList.clear();
                adapter.notifyDataSetChanged();
            }
        });

        // 注册接收已配对设备的广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);//接收已配对设备的广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//接收搜索完成的广播
        registerReceiver(receiver, filter);
    }


    private void startDiscovery() {
        checkPermission();
        // 确保蓝牙已开启
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
            return;
        }
        //取消蓝牙设备的搜索过程
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        //清空设备列表
        adapter.clear();
        //启动蓝牙设备的搜索
        bluetoothAdapter.startDiscovery();
    }

    private void stopDiscovery(){
        checkPermission();
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        // 取消注册广播接收器
        if (receiver != null) {
            try {
                unregisterReceiver(receiver);
                receiver=null;
            } catch (IllegalArgumentException e) {
                if (!e.getMessage().contains("Receiver not registered")) {
                    // 如果不是因为未注册接收器而抛出的异常，进行处理
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkPermission();
        if (bluetoothAdapter!= null && bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        try {
            unregisterReceiver(receiver);
            receiver=null;
        } catch (IllegalArgumentException e) {
            if (!e.getMessage().contains("Receiver not registered")) {
                // 如果不是因为未注册接收器而抛出的异常，进行处理
                e.printStackTrace();
            }
        }
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限已授予，开始搜索
                startDiscovery();
            } else {
                // 权限被拒绝，提示用户
                Toast.makeText(this, "需要位置权限来扫描蓝牙设备", Toast.LENGTH_SHORT).show();
            }
        }
    }
}