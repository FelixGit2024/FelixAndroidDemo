package com.firstproject.androiddemofx.month2.week4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

import java.util.HashMap;

public class Activity_Day_2_4_1 extends AppCompatActivity {
    private TextView tvText1,tvText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day241);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BroadcastReceiver receiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                StringBuilder statusMessage = new StringBuilder();
                if (Intent.ACTION_POWER_CONNECTED.equals(action)){
//                    Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();
                    statusMessage.append("充电器连接\n");
                }
                if (Intent.ACTION_POWER_DISCONNECTED.equals(action)) {
//                    Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();

                    statusMessage.append("充电器断开\n");
                }
                if ("android.hardware.usb.action.USB_STATE".equals(action)) {
                    boolean connected = intent.getExtras().getBoolean("connected");
                    boolean adbEnabled = intent.getExtras().getBoolean("adb_enabled");
                    int function = intent.getExtras().getInt("function", -1);

                    if (connected) {
                        statusMessage.append("USB 设备已连接\n");
                    } else {
                        statusMessage.append("USB 设备已断开\n");
                    }

                    if (adbEnabled) {
                        statusMessage.append(" ADB 调试已启用\n");
                    } else {
                        statusMessage.append(" ADB 调试未启用\n");
                    }

                    switch (function) {
                        case 0:
                            statusMessage.append(" 当前 USB 功能模式为特定模式\n");
                            break;
                        default:
                            statusMessage.append(" 当前 USB 功能模式未知\n");
                            break;
                    }
//                    Toast.makeText(context, statusMessage, Toast.LENGTH_SHORT).show();
                }
                tvText1.setText(statusMessage);
                Log.d("TAG", "onReceive: "+intent.getAction());
            }
        };
        tvText1=findViewById(R.id.tvText1);
        tvText2=findViewById(R.id.tvText2);

        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        filter.addAction("android.hardware.usb.action.USB_STATE");
        registerReceiver(receiver,filter);

        if (isUDiskConnection()){
            tvText2.setText("有外部存储设备（U盘或读卡器）");
        }else {
            tvText2.setText("没有外部存储设备（U盘或读卡器）");
        }
    }

    public boolean isUDiskConnection(){
        UsbManager usbManager = (UsbManager) getSystemService(USB_SERVICE);

        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        for(String key:deviceList.keySet()){
            UsbDevice usbDevice = deviceList.get(key);
            if (usbDevice!=null){
                for (int i=0;i<usbDevice.getInterfaceCount();i++){
                    UsbInterface usbInterface = usbDevice.getInterface(i);
                    int interfaceClass = usbInterface.getInterfaceClass();
                    if (interfaceClass== UsbConstants.USB_CLASS_MASS_STORAGE){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}