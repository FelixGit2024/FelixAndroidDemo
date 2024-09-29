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
    private TextView tvUSBText;
    private String[] stateArr=new String[4];
//    private StringBuilder statusMessage=new StringBuilder();

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

        tvUSBText=findViewById(R.id.tvUSBText);
        stateArr[0]="充电器状态：\n";
        stateArr[1]="USB连接状态：\n";
        stateArr[2]="功能模式：\n";
        stateArr[3]="外部存储设备：\n";
        updateTextView();

        BroadcastReceiver receiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();

                if (Intent.ACTION_POWER_CONNECTED.equals(action)){
//                    Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();
//                    statusMessage.append("充电器连接\n");
//                    updateTextView("充电器连接\n");
                    stateArr[0]="充电器状态：连接\n";
                }
                if (Intent.ACTION_POWER_DISCONNECTED.equals(action)) {
//                    Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();
//                    statusMessage.append("充电器断开\n");
//                    updateTextView("充电器断开\n");
                    stateArr[0]="充电器状态：断开\n";
                }



                if ("android.hardware.usb.action.USB_STATE".equals(action)) {
                    boolean connected = intent.getExtras().getBoolean("connected");
                    int function = intent.getExtras().getInt("function", -1);

                    if (connected) {
//                        updateTextView("USB 设备已连接\n");
//                        statusMessage.append("USB 设备已连接\n");
                        stateArr[1]="USB连接状态：已连接\n";
                    } else {
//                        updateTextView("USB 设备已断开\n");
//                        statusMessage.append("USB 设备已断开\n");
                        stateArr[1]="USB连接状态：未连接\n";
                    }

//                    if (adbEnabled) {
////                        updateTextView("ADB 调试已启用\n");
////                        statusMessage.append(" ADB 调试已启用\n");
//                        stateArr[2]="ADB调试：已启用\n";
//                    } else {
////                        updateTextView("ADB 调试未启用\n");
////                        statusMessage.append(" ADB 调试未启用\n");
//                        stateArr[3]="ADB调试：未启用\n";
//                    }


                    switch (function) {
                        case 0:
                            stateArr[2]="功能模式：特定模式\n";
//                            updateTextView("当前 USB 功能模式为特定模式\n");
//                            statusMessage.append(" 当前 USB 功能模式为特定模式\n");
                            break;
                        default:
                            stateArr[2]="功能模式：未知\n";
//                            updateTextView("当前 USB 功能模式未知\n");
//                            statusMessage.append(" 当前 USB 功能模式未知\n");
                            break;
                    }
//                    Toast.makeText(context, statusMessage, Toast.LENGTH_SHORT).show();
                }
                Log.d("TAG", "onReceive: "+intent.getAction());
                updateTextView();
            }
        };


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
            stateArr[3]="外部存储设备：有\n";
//            updateTextView("有外部存储设备（U盘或读卡器）\n");
//            tvText2.setText("有外部存储设备（U盘或读卡器）");
        }else {
            stateArr[3]="外部存储设备：无\n";
//            updateTextView("没有外部存储设备（U盘或读卡器）\n");
//            tvText2.setText("没有外部存储设备（U盘或读卡器）");
        }
        updateTextView();
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
    private void updateTextView(){
        StringBuilder strTemp=new StringBuilder();
        for (int i=0; i<stateArr.length;i++){
            strTemp.append(stateArr[i]);
        }
        tvUSBText.setText(strTemp);
    }
}