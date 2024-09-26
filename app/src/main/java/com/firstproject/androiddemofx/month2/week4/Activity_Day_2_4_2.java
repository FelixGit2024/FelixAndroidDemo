package com.firstproject.androiddemofx.month2.week4;

import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class Activity_Day_2_4_2 extends AppCompatActivity {

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private USBBroadcastReceiver usbBroadcastReceiver=new USBBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day242);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        IntentFilter usbDeviceStateFilter = new IntentFilter();
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(usbBroadcastReceiver, usbDeviceStateFilter);

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(usbBroadcastReceiver, filter);
    }
}