package com.firstproject.androiddemofx.month2.week2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Activity_Day_2_2_2 extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final String TAG = "CameraInfo";
    private static final int pixelFormat = ImageFormat.JPEG;
    private static final int IMAGE_BUFFER_SIZE = 3;
    private CameraManager cameraManager;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private CameraCharacteristics cameraCharacteristics;
    private String cameraId;
    private SurfaceView surfaceView;
    private Button btnTakePicture;
    private HandlerThread backgroundThread;
    private Handler backgroundHandler;
    private ImageReader imageReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day222);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
        String[] cameraIdList;
        try {
            cameraIdList = cameraManager.getCameraIdList();
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
        cameraId = cameraIdList[0]; //设置使用后置摄像头，1为前置摄像头
        try {
            cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        for (String i : cameraIdList) {
            try {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(i);
                int cameraLensFacing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cameraLensFacing == characteristics.LENS_FACING_FRONT) {
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

        surfaceView = findViewById(R.id.surface_view222);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                startCameraPreview();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                stopCameraPreview();
            }
        });

        btnTakePicture = findViewById(R.id.btn_take_picture222);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    private void takePicture() {
        try {
            if (cameraDevice == null) {
                return;
            }

            CaptureRequest.Builder captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            captureRequestBuilder.addTarget(imageReader.getSurface());

            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            int orientation;
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = 90;
                    break;
                case Surface.ROTATION_90:
                    orientation = 0;
                    break;
                case Surface.ROTATION_180:
                    orientation = 270;
                    break;
                case Surface.ROTATION_270:
                    orientation = 180;
                    break;
                default:
                    orientation = 90;
                    break;
            }
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, orientation);
            cameraCaptureSession.capture(captureRequestBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Image image = imageReader.acquireLatestImage();
                    if (image != null) {
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] data = new byte[buffer.remaining()];
                        buffer.get(data);
                        saveImageToFile(data);
                        image.close();
                    }
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void saveImageToFile(byte[] data) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("Camera", "Error creating media file, check storage permissions");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            Log.d("Camera", "Image saved: " + pictureFile.getAbsolutePath());
        } catch (Exception e) {
            Log.d("Camera", "Error saving image: " + e.getMessage());
        }
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

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
                ActivityCompat.requestPermissions(Activity_Day_2_2_2.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                //配置imageReader
                Size[] size = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(pixelFormat);
                int sizeWidth = size[0].getWidth();
                int sizeHeight = size[0].getHeight();
                imageReader = ImageReader.newInstance(sizeWidth, sizeHeight, pixelFormat, IMAGE_BUFFER_SIZE);

                cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                    // 1. 相机打开成功的回调onOpened
                    @Override
                    public void onOpened(CameraDevice camera) {
                        cameraDevice = camera;
                        startBackgroundThread();
                        createCameraPreviewSession();
                    }
                    @Override
                    public void onDisconnected(CameraDevice camera) {
                        camera.close();
                        cameraDevice = null;
                    }

                    @Override
                    public void onError(CameraDevice camera, int error) {
                        camera.close();
                        cameraDevice = null;
                        Toast.makeText(Activity_Day_2_2_2.this, "Camera error: " + error, Toast.LENGTH_SHORT).show();
                    }
                }, null);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

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
            Surface surface = surfaceView.getHolder().getSurface();
            CaptureRequest.Builder previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            previewRequestBuilder.addTarget(surface);

            previewRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
            previewRequestBuilder.set(CaptureRequest.SCALER_CROP_REGION, null);

            cameraDevice.createCaptureSession(Arrays.asList(surface, imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    if (cameraDevice == null) {
                        return;
                    }
                    cameraCaptureSession = session;
                    try {
                        CaptureRequest previewRequest = previewRequestBuilder.build();
                        cameraCaptureSession.setRepeatingRequest(previewRequest, null, backgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                    Toast.makeText(Activity_Day_2_2_2.this, "Camera configuration failed", Toast.LENGTH_SHORT).show();
                }
            }, null);
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