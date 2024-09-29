package com.firstproject.androiddemofx.month2.week4;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Activity_Day_2_4_2 extends AppCompatActivity {

    StorageManager storageManager;
    List<StorageVolume> storageVolumes;
    TextView stateText;
    ListView fileListView;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();
            if (Intent.ACTION_MEDIA_MOUNTED.equals(intent.getAction())) {
                String mountPointPath = intent.getDataString();
                if (mountPointPath != null) {
                    Toast.makeText(context, mountPointPath, Toast.LENGTH_SHORT).show();
                }
                stateText.setText("USB状态：U盘插入");
                String absolutePath = null;
                storageVolumes = storageManager.getStorageVolumes();
                for (StorageVolume volume : storageVolumes) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        absolutePath = volume.getDirectory().getAbsolutePath();
                    }
                }
                if (absolutePath != null) {
                    File otgDirectory = new File(absolutePath);
                    List<String> fileList = new ArrayList<>();
                    if (otgDirectory.exists()) {
                        File[] files = otgDirectory.listFiles();
                        if (files != null) {
                            for (File file : files) {
                                fileList.add(file.getName());
                            }
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Activity_Day_2_4_2.this, android.R.layout.simple_list_item_1, fileList);
                    fileListView.setAdapter(adapter);
                }

            } else if (Intent.ACTION_MEDIA_REMOVED.equals(intent.getAction())) {
                stateText.setText("USB状态：无介质");
                Toast.makeText(context, "U盘插入 无存储介质", Toast.LENGTH_SHORT).show();
            } else if (Intent.ACTION_MEDIA_UNMOUNTED.equals(intent.getAction())) {
                stateText.setText("USB状态：U盘移除");
                Toast.makeText(context, "U盘移除", Toast.LENGTH_SHORT).show();
            }
        }
    };

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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        fileListView = (ListView) findViewById(R.id.listView247);
        stateText = findViewById(R.id.usb_status_textview);
        // 注册USB状态变化广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        intentFilter.addDataScheme("file");
        registerReceiver(receiver, intentFilter);

        storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
    }

}