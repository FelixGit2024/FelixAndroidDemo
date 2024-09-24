package com.firstproject.androiddemofx.month2.week2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.net.Uri;
import android.os.Build;
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

public class Activity_Day_2_2_4 extends AppCompatActivity {
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
    private Button switchCameraButton,takePictureButton,openAlbumButton;
    private HandlerThread backgroundThread;
    private Handler backgroundHandler;
    private boolean isFrontCamera=false;
    private ImageReader imageReader;
    private CaptureRequest.Builder captureRequestBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day224);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initCamera(0);
        initSurface();
        initButton();

        checkPermission();

    }





    private void initCamera(int Id){
        cameraManager=(CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
        String[] cameraIdList;
        try {
            cameraIdList = cameraManager.getCameraIdList();
            cameraId = cameraIdList[Id];
            cameraCharacteristics=cameraManager.getCameraCharacteristics(cameraId);
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void initSurface() {
        surfaceView=findViewById(R.id.surfaceView224);
        surfaceView.getHolder().addCallback(surfaceStateCallback);
    }

    private void initButton() {
        takePictureButton = findViewById(R.id.takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        switchCameraButton = findViewById(R.id.switchCameraButton);
        switchCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCamera();
            }
        });

        openAlbumButton = findViewById(R.id.openAlbumButton);
        openAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum();
            }
        });
    }

    private void openAlbum() {
        openImageToFile();
    }

    private void reSetCamera(){
        stopCameraPreview();
        stopBackgroundThread();
        startBackgroundThread();
        startCameraPreview();
    }

    private void switchCamera() {
        stopCameraPreview();
        stopBackgroundThread();
        if (isFrontCamera){
            isFrontCamera=false;
            initCamera(0);
        }else {
            isFrontCamera=true;
            initCamera(1);
        }
        startBackgroundThread();
        startCameraPreview();
    }

    private void takePicture() {
        try {
            if (cameraDevice == null) {
                return;
            }
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
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

            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION,orientation);
            cameraDevice.createCaptureSession(Arrays.asList(imageReader.getSurface()),takeStateCallback, backgroundHandler);
//            cameraCaptureSession.capture(captureRequestBuilder.build(), sessionCaptureCallback, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
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
                ActivityCompat.requestPermissions(Activity_Day_2_2_4.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {

                Size[] size = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(pixelFormat);
                int sizeWidth = size[0].getWidth();
                int sizeHeight = size[0].getHeight();
                imageReader = ImageReader.newInstance(sizeWidth, sizeHeight, pixelFormat, IMAGE_BUFFER_SIZE);

                cameraManager.openCamera(cameraId,cameraStateCallback, backgroundHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void createCameraPreviewSession() {
        try {
            Surface surface = surfaceView.getHolder().getSurface();
            cameraDevice.createCaptureSession(Arrays.asList(surface),sessionStateCallback,backgroundHandler);
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
        if (imageReader!= null) {
            imageReader.close();
            imageReader = null;
        }
        stopBackgroundThread();
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


    private void openImageToFile(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("image/*");
        if (intent.resolveActivity(this.getPackageManager())!= null) {
            this.startActivity(intent);
        }
    }

    private void addImageToGallery(File file) {
        // 将照片信息插入到媒体库
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }

    private void saveImageToFile(byte[] data) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("Camera", "Error creating media file, check storage permissions");
            return;
        }
        addImageToGallery(pictureFile);
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            Log.d("Camera", "Image saved: " + pictureFile.getAbsolutePath());
            Toast.makeText(this, "Image saved: "+ pictureFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("Camera", "Error saving image: " + e.getMessage());
        }
    }

    private final CameraCaptureSession.StateCallback takeStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            try {
                session.capture(captureRequestBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                        super.onCaptureCompleted(session, request, result);
//                                cameraCaptureSession=session;
                        Image image = imageReader.acquireLatestImage();
                        if (image != null) {
                            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                            byte[] data = new byte[buffer.remaining()];
                            buffer.get(data);
                            saveImageToFile(data);
                            image.close();
                            session.close();
                            startCameraPreview();
                        }
                    }
                }, backgroundHandler);
            } catch (CameraAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {

        }
    };

    private final SurfaceHolder.Callback surfaceStateCallback=new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(@NonNull SurfaceHolder holder) {
            startBackgroundThread();
            startCameraPreview();
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
            stopCameraPreview();
        }
    };

    private final CameraCaptureSession.StateCallback sessionStateCallback=new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            if (cameraDevice == null) {
                return;
            }
            cameraCaptureSession = session;

            try {
                Surface surface = surfaceView.getHolder().getSurface();
                CaptureRequest.Builder previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                previewRequestBuilder.addTarget(surface);
                CaptureRequest previewRequest = previewRequestBuilder.build();
                cameraCaptureSession.setRepeatingRequest(previewRequest, null, backgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Toast.makeText(Activity_Day_2_2_4.this, "Camera configuration failed", Toast.LENGTH_SHORT).show();
        }
    };

    private final CameraDevice.StateCallback cameraStateCallback=new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice = camera;
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
            Toast.makeText(Activity_Day_2_2_4.this, "Camera error: " + error, Toast.LENGTH_SHORT).show();
        }
    };

    private final CameraCaptureSession.CaptureCallback sessionCaptureCallback=new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            cameraCaptureSession=session;
            Image image = imageReader.acquireLatestImage();
            if (image != null) {
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                saveImageToFile(data);
                image.close();
            }
        }
    };

    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            }
        }
    }
}