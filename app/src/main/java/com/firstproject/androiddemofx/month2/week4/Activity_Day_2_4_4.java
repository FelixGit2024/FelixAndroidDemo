package com.firstproject.androiddemofx.month2.week4;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

public class Activity_Day_2_4_4 extends AppCompatActivity {
    private static final String TAG = "BLUETOOTH";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int CONNECTION_SUCCESS = 1;
    private static final int CONNECTION_FAILED = 0;
    private BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver bluetoothReceiver;
    private Button startBlueTooth, stopSearch;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> deviceList;
    private Handler handler;
    private String deviceAddress;

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

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case CONNECTION_SUCCESS:
                        // 连接成功后的处理
                        Log.d(TAG, "handleMessage: 连接成功");
                        break;
                    case CONNECTION_FAILED:
                        // 连接失败后的处理
                        Log.d(TAG, "handleMessage: 连接失败");
                        break;
                }
                return false;
            }
        });

        bluetoothReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkPermission();
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    String type = "";
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (0 == device.getType()) type = "未知设备类型";
                    else if (1 == device.getType()) type = "传统蓝牙设备";
                    else if (2 == device.getType()) type = "低功耗蓝牙设备";
                    else if (3 == device.getType()) type = "双模设备";

                    String status = "";
                    if (device.getBondState() == BluetoothDevice.BOND_BONDED) status = "已配对";
                    else if (device.getBondState() == BluetoothDevice.BOND_BONDING)
                        status = "配对中";
                    else if (device.getBondState() == BluetoothDevice.BOND_NONE) status = "未配对";

                    deviceList.add(device.getName() + "\n" + device.getAddress() + "\n" + type + "\n" + status);
                    arrayAdapter.notifyDataSetChanged();
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
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                // 蓝牙未开启，请求用户开启蓝牙
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }

        deviceList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDeviceInfo = arrayAdapter.getItem(position);
                String[] parts = selectedDeviceInfo.split("\n");
                String deviceName = parts[0];
                deviceAddress = parts[1];
                new BluetoothConnectThread().start();
//                connectToBluetoothDevice(deviceName, deviceAddress);
            }
        });

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
            }
        });

        // 注册接收已配对设备的广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);//接收已配对设备的广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//接收搜索完成的广播
        registerReceiver(bluetoothReceiver, filter);
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
        arrayAdapter.clear();
        //启动蓝牙设备的搜索
        bluetoothAdapter.startDiscovery();
    }

    private void stopDiscovery() {
        checkPermission();
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        // 取消注册广播接收器
        if (bluetoothReceiver != null) {
            try {
                unregisterReceiver(bluetoothReceiver);
                bluetoothReceiver = null;
            } catch (IllegalArgumentException e) {
                if (!e.getMessage().contains("Receiver not registered")) {
                    // 如果不是因为未注册接收器而抛出的异常，进行处理
                    e.printStackTrace();
                }
            }
        }
        deviceList.clear();
        arrayAdapter.notifyDataSetChanged();
    }

    private void connectToBluetoothDevice(String deviceName, String address) {
        checkPermission();
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        BluetoothSocket socket = null;
        try {
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socket.connect();

            // 连接成功后的处理
            Toast.makeText(this, "与" + deviceName + "连接成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            // 连接失败的处理
            Toast.makeText(this, "与" + deviceName + "连接失败", Toast.LENGTH_SHORT).show();

            try {
                Method m;
                m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                socket = (BluetoothSocket) m.invoke(device, Integer.valueOf(1));
                socket.connect();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkPermission();
        if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        try {
            unregisterReceiver(bluetoothReceiver);
            bluetoothReceiver = null;
        } catch (IllegalArgumentException e) {
            if (!e.getMessage().contains("Receiver not registered")) {
                // 如果不是因为未注册接收器而抛出的异常，进行处理
                e.printStackTrace();
            }
        }
    }

    public void checkPermission() {
        // 检查并请求位置权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                return;
            } else {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // 蓝牙已开启
                Toast.makeText(this, "蓝牙已开启", Toast.LENGTH_SHORT).show();
            } else {
                // 用户拒绝开启蓝牙
                Toast.makeText(this, "用户拒绝开启蓝牙", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class BluetoothConnectThread extends Thread {
        @Override
        public void run() {
            try {
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
                if (ActivityCompat.checkSelfPermission(Activity_Day_2_4_4.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                socket.connect();
                // 连接成功，通过 Handler 通知主线程
                Message msg = new Message();
                msg.what = CONNECTION_SUCCESS;
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
                // 连接失败，通过 Handler 通知主线程
                Message msg = new Message();
                msg.what = CONNECTION_FAILED;
                handler.sendMessage(msg);
            }
        }
    }

}