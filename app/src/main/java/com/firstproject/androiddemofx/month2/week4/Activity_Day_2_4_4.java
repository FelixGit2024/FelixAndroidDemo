package com.firstproject.androiddemofx.month2.week4;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

import java.util.Set;

public class Activity_Day_2_4_4 extends AppCompatActivity {

    private static final String TAG = "BLUETOOTH";
    private static final int REQUEST_ENABLE_BT = 100;
    private BluetoothManager bluetoothManager;
    private BroadcastReceiver bluetoothReceiver;
    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> bondedDevices;

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
        checkPermission();
        //蓝牙管理对象
        bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        //蓝牙适配器
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) Log.d(TAG, "设备不支持蓝牙");

        bondedDevices=bluetoothAdapter.getBondedDevices();

        //开启蓝牙
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        //搜索蓝牙设备

        //开始搜索蓝牙设备
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();

        //蓝牙接受广播
        bluetoothReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Bundle b = intent.getExtras();
                Object[] lstName = b.keySet().toArray();
                checkPermission();
                // 显示所有收到的消息及其细节
                for (int i = 0; i < lstName.length; i++) {
                    String keyName = lstName[i].toString();
                    Log.e("bluetooth", keyName + ">>>" + String.valueOf(b.get(keyName)));
                }
                BluetoothDevice device;
                // 搜索发现设备时，取得设备的信息；注意，这里有可能重复搜索同一设备
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    Log.d(TAG, "找到新设备了");
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

//                    Method method = null;
//                    try {
//                        method = device.getClass().getMethod("createBond");
//                        method.invoke(device);
//                        Method m = device.getClass().getMethod("createRfcommSocket", int.class);
//                        BluetoothSocket socket = (BluetoothSocket) m.invoke(device, 1);
//                        socket.connect();
//                    } catch (NoSuchMethodException e) {
//                        e.printStackTrace();
//                    } catch (InvocationTargetException e) {
//                        e.printStackTrace();
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    Log.d(TAG, device.getName()+device.getType()+device.getBondState());
                } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                    if (bondState==BluetoothDevice.BOND_BONDING){
                        Log.d(TAG, "蓝牙正在配对......");
                    } else if (bondState==BluetoothDevice.BOND_BONDED) {
                        Log.d(TAG, "蓝牙设备完成配对");
                    } else if (bondState==BluetoothDevice.BOND_NONE) {
                        Log.d(TAG, "蓝牙设备取消配对");
                    }
                }
            }
        };

        //注册广播接收器，监听蓝牙设备的发现
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);//搜索发现设备
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//状态改变
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//行动扫描模式改变了
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//动作状态发生了变化
        registerReceiver(bluetoothReceiver, filter);
    }

    //蓝牙回调广播
    private void blueToothRegister(){

    }

    public void unregisterReceiver(Context context) {
        unregisterReceiver(bluetoothReceiver);
        if (bluetoothAdapter != null){
            checkPermission();
            bluetoothAdapter.cancelDiscovery();
        }
    }

    public void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN,Manifest.permission.BLUETOOTH_CONNECT}, 1);
            } else {
                Log.d(TAG, "有BLUETOOTH_SCAN权限和BLUETOOTH_CONNECT权限");
            }
        }
    }

    //处理蓝牙开启的结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == this.RESULT_OK) {
                // 蓝牙已开启
                Log.d(TAG, "蓝牙已开启");
            } else {
                // 用户拒绝开启蓝牙
                Log.d(TAG, "用户拒绝开启蓝牙");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 ){
            boolean bluetoothScanGranted = false;
            boolean bluetoothConnectGranted = false;

            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.BLUETOOTH_SCAN)) {
                    bluetoothScanGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                } else if (permissions[i].equals(Manifest.permission.BLUETOOTH_CONNECT)) {
                    bluetoothConnectGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                }
            }

            if (bluetoothScanGranted && bluetoothConnectGranted) {
                Log.d(TAG, "可以进行蓝牙相关操作");
                // 两个权限都已授予，可以进行蓝牙相关操作
            } else {
                Log.d(TAG, "蓝牙权限被拒绝，无法进行蓝牙操作。");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothReceiver!= null) {
            unregisterReceiver(bluetoothReceiver);
        }
    }
}