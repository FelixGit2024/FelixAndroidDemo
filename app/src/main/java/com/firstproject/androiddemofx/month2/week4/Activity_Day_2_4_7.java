package com.firstproject.androiddemofx.month2.week4;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;
import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileInputStream;
import com.github.mjdev.libaums.partition.Partition;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Activity_Day_2_4_7 extends AppCompatActivity {
    private static final String TAG = "OTG";
    EditText mUDiskEdt;
    Button mUDiskWrite;
    Button mUDiskRead;
    TextView mUDiskShow;

    private UsbMassStorageDevice[] storageDevices;
    private UsbFile cFolder;

    //自定义U盘读写权限
    public static final String ACTION_USB_PERMISSION = "com.example.usbreadwriterdaemon.USB_PERMISSION";
    private final static String U_DISK_FILE_NAME = "u_disk.txt";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    Log.d(TAG, "保存成功");
                    break;
                case 101:
                    String txt = msg.obj.toString();
                    if (!TextUtils.isEmpty(txt))
                        mUDiskShow.setText("读取到的数据是：" + txt);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day247);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mUDiskEdt=findViewById(R.id.u_disk_edt);
        mUDiskWrite=findViewById(R.id.u_disk_write);
        mUDiskRead=findViewById(R.id.u_disk_read);
        mUDiskShow=findViewById(R.id.u_disk_show);

        mUDiskWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = mUDiskEdt.getText().toString().trim();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        saveText2UDisk(content);
                    }
                });
            }
        });
        mUDiskRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFromUDisk();
            }
        });

        EventBus.getDefault().register(this);//EventBus 注册
        registerUDiskReceiver();//usb插拔广播 注册

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChangeEvent(UsbStatusChangeEvent event) {
        if (event.isConnected) {
            //接收到U盘插入广播，尝试读取U盘设备数据
            redUDiskDevsList();
        } else if (event.isGetPermission) {
            UsbDevice usbDevice = event.usbDevice;

            //用户已授权，可以进行读取操作
            Log.i(TAG, "onNetworkChangeEvent: ");
//            ToastUtil.showToast("onReceive: 权限已获取");
            Log.d(TAG, "权限已获取");
            readDevice(getUsbMass(usbDevice));
        } else {

        }
    }

    private void saveText2UDisk(String content) {
        //项目中也把文件保存在了SD卡，其实可以直接把文本读取到U盘指定文件
        File file = FileUtil2.getSaveFile(getPackageName() + File.separator + FileUtil2.DEFAULT_BIN_DIR, U_DISK_FILE_NAME);
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null != cFolder) {
            FileUtil2.saveSDFile2OTG(file, cFolder);
            mHandler.sendEmptyMessage(100);
        }
    }

    StringBuffer stringBuffer = new StringBuffer();

    private void readFromUDisk() {
        UsbFile[] usbFiles = new UsbFile[0];
        try {
            usbFiles = cFolder.listFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null != usbFiles && usbFiles.length > 0) {

            for (UsbFile usbFile : usbFiles) {
                stringBuffer.append(", " + usbFile.getName());
                if (usbFile.getName().equals(U_DISK_FILE_NAME)) {
                    readTxtFromUDisk(usbFile);
                }
            }
            //mUDiskShow.setText("文件名：" + stringBuffer.toString());
        }
    }

    private void redUDiskDevsList() {
        //设备管理器
        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        //获取U盘存储设备
        storageDevices = UsbMassStorageDevice.getMassStorageDevices(this);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE);
        //一般手机只有1个OTG插口
        for (UsbMassStorageDevice device : storageDevices) {
            //读取设备是否有权限，
            if (usbManager.hasPermission(device.getUsbDevice())) {
//                ToastUtil.showToast("有权限");
                Log.d(TAG, "有权限");
                readDevice(device);
            } else {
//                ToastUtil.showToast("没有权限，进行申请");
                Log.d(TAG, "没有权限，进行申请");
                //没有权限，进行申请，此时系统会有个弹框，询问你是否同意，当然我们应该同意啦！
                usbManager.requestPermission(device.getUsbDevice(), pendingIntent);
            }
        }
        if (storageDevices.length == 0) {
//            ToastUtil.showToast("请插入可用的U盘");
            Log.d(TAG, "请插入可用的U盘");
        }
    }


    private UsbMassStorageDevice getUsbMass(UsbDevice usbDevice) {
        for (UsbMassStorageDevice device : storageDevices) {
            if (usbDevice.equals(device.getUsbDevice())) {
                return device;
            }
        }
        return null;
    }

    private void readDevice(UsbMassStorageDevice device) {
        try {
            device.init();//初始化
            //设备分区
            Partition partition = device.getPartitions().get(0);

            //文件系统
            FileSystem currentFs = partition.getFileSystem();
            currentFs.getVolumeLabel();//可以获取到设备的标识

            //通过FileSystem可以获取当前U盘的一些存储信息，包括剩余空间大小，容量等等
            Log.e("Capacity: ", currentFs.getCapacity() + "");
            Log.e("Occupied Space: ", currentFs.getOccupiedSpace() + "");
            Log.e("Free Space: ", currentFs.getFreeSpace() + "");
            Log.e("Chunk size: ", currentFs.getChunkSize() + "");

//            ToastUtil.showToast("可用空间：" + currentFs.getFreeSpace());
            Log.d(TAG, "可用空间：" + currentFs.getFreeSpace());


            cFolder = currentFs.getRootDirectory();//设置当前文件对象为根目录


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readTxtFromUDisk(UsbFile usbFile) {
        Log.i(TAG, "readTxtFromUDisk: ");
        UsbFile descFile = usbFile;
        //读取文件内容
        InputStream is = new UsbFileInputStream(descFile);
        //读取秘钥中的数据进行匹配
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            String read;
            while ((read = bufferedReader.readLine()) != null) {
                sb.append(read);
            }
            Message msg = mHandler.obtainMessage();
            msg.what = 101;
            msg.obj = sb;
            mHandler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * usb插拔广播 注册
     */
    private void registerUDiskReceiver() {
        IntentFilter usbDeviceStateFilter = new IntentFilter();
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        usbDeviceStateFilter.addAction("android.hardware.usb.action.USB_STATE");

        usbDeviceStateFilter.addAction(ACTION_USB_PERMISSION); //自定义广播

        registerReceiver(new UsbStateChangeReceiver(), usbDeviceStateFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}