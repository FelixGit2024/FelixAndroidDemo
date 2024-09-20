package com.firstproject.androiddemofx.month2.week2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

import java.util.Arrays;


public class Activity_Day_2_2_1 extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final String TAG = "CameraInfo";
    private CameraManager cameraManager;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private String cameraId;
    private SurfaceView surfaceView;
    private HandlerThread backgroundThread;
    private Handler backgroundHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day221);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //1. 获取 CameraManager
        cameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
        //2、获取 CameraIdList，包含了所有摄像头的CameraId
        String[] cameraIdList;
        try {
            cameraIdList = cameraManager.getCameraIdList();
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
        cameraId = cameraIdList[0]; //设置使用后置摄像头，1为前置摄像头
        //3、判断摄像头是前摄后摄还是外置
        for (String i : cameraIdList) {
            try {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(i);

                //LENS_FACING为摄像头方向，
                int cameraLensFacing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
//                cameraId->0->后置摄像头->LENS_FACING_BACK->1
//                cameraId->1->前置摄像头->LENS_FACING_FRONT->0
                if (cameraLensFacing == CameraCharacteristics.LENS_FACING_FRONT) {
                    Log.d(TAG, "cameraId->" + i + "->前置摄像头->LENS_FACING_FRONT->" + CameraCharacteristics.LENS_FACING_FRONT);
                } else if (cameraLensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                    Log.d(TAG, "cameraId->" + i + "->后置摄像头->LENS_FACING_BACK->" + CameraCharacteristics.LENS_FACING_BACK);
                } else {
                    Log.d(TAG, "cameraId->" + i + "->Unknown camera type");
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        //4、surface初始化
        //获取surfaceView实例
        surfaceView = findViewById(R.id.surface_view221);
        //设置SurfaceHolder.Callback
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
//            创建时初始化：当 SurfaceView 被创建时（surfaceCreated方法被调用），可以进行一些初始化操作。
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
//              为了使相机在后台操作，创建了HandlerThread和Handler，在startBackgroundThread中启动线程
                startBackgroundThread();
                startCameraPreview();
            }

            //            改变时调整：当 Surface 的大小或格式发生改变（surfaceChanged方法被调用）
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            //            销毁时清理：当 SurfaceView 将要被销毁时（surfaceDestroyed方法被调用），可以进行资源的释放操作
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                stopCameraPreview();
            }
        });

    }

    //权限处理与相机设备打开、预览设置
    private void startCameraPreview() {
        try {
            //检查相机权限，没有权限则申请
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
//                return;
                ActivityCompat.requestPermissions(Activity_Day_2_2_1.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                //有权限的话开始相机预览
                //通过CameraManager、cameraId打开相机设备，并设置相应的回调函数
                cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                    // 1. 相机打开成功的回调onOpened
                    @Override
                    public void onOpened(CameraDevice camera) {
                        cameraDevice = camera;
                        createCameraPreviewSession();
                    }

                    //2， 相机设备与应用的连接意外断开的回调onDisconnected
                    @Override
                    public void onDisconnected(CameraDevice camera) {
                        camera.close();
                        cameraDevice = null;
                    }

                    //3. 相机打开过程中发生错误时被调用
                    @Override
                    public void onError(CameraDevice camera, int error) {
                        camera.close();
                        cameraDevice = null;
                        Toast.makeText(Activity_Day_2_2_1.this, "Camera error: " + error, Toast.LENGTH_SHORT).show();
                    }
                }, backgroundHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //停止相机预览，关闭后台线程
    private void stopCameraPreview() {
        if (cameraCaptureSession != null) {
            try {
                cameraCaptureSession.stopRepeating();
                cameraCaptureSession.close();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            cameraCaptureSession = null;
        }

        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
        stopBackgroundThread();
    }

    private void createCameraPreviewSession() {
        try {
//            获取SurfaceView对应的Surface
            Surface surface = surfaceView.getHolder().getSurface();
//            创建CaptureRequest.Builder用于构建预览请求
            CaptureRequest.Builder previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//            设置目标Surface
            previewRequestBuilder.addTarget(surface);

//            创建相机捕获会话
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                //                会话配置成功
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    if (cameraDevice == null) {
                        return;
                    }
                    cameraCaptureSession = session;
                    try {
                        CaptureRequest previewRequest = previewRequestBuilder.build();
//                        构建预览请求并设置为重复请求以实现相机预览
//                        通过backgroundHandler将设置重复请求的操作放入 HandlerThread 的消息队列中，
//                        使得这个操作在单独的线程中执行，而不是在主线程中执行。
//                        这样可以避免因为相机操作可能的耗时（例如相机硬件的响应时间等）而导致主线程的阻塞，
//                        从而保证应用的界面能够保持流畅响应。
                        cameraCaptureSession.setRepeatingRequest(previewRequest, null, backgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                //                会话配置失败
                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                    Toast.makeText(Activity_Day_2_2_1.this, "Camera configuration failed", Toast.LENGTH_SHORT).show();
                }
            }, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void startBackgroundThread() {
        backgroundThread = new HandlerThread("CameraBackground");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        if (backgroundThread != null) {
            backgroundThread.quitSafely();
            try {
                backgroundThread.join();
                backgroundThread = null;
                backgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //    用于在Android中处理权限请求的结果
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
//            当用户授予相机权限，调用startCameraPreview方法来启动相机预览
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraPreview();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}