package com.firstproject.androiddemofx.month2.week2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.io.File;
import java.io.IOException;

public class Activity_Day_2_2_7 extends AppCompatActivity {
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";

    private MediaRecorder recorder = null;
    private String mFileName = null;
    private MediaPlayer mediaPlayer = null;

    private Button btnStartRecording, btnStopAndPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day227);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnStartRecording = findViewById(R.id.btnStartRecording);
        btnStopAndPlay = findViewById(R.id.btnStopAndPlay);

        setupListeners();
    }
    private void setupListeners() {
        btnStartRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    startRecording();
                } else {
                    requestPermissions();
                }
            }
        });

        btnStopAndPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
                playRecording();
            }
        });
    }

    private boolean checkPermissions() {
        int permissionRecord = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int permissionWriteExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionRecord == PackageManager.PERMISSION_GRANTED && permissionWriteExternalStorage == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_RECORD_AUDIO_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
            } else {
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startRecording() {
        mFileName = getFilePath();
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(mFileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
            recorder.start();
            Toast.makeText(this, "开始录音...", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("AudioRecorder", "录音失败: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "录音失败", Toast.LENGTH_SHORT).show();
            releaseRecorder();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            Toast.makeText(this, "录音已保存", Toast.LENGTH_SHORT).show();
        }
    }

    private void playRecording() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

        try {
            mediaPlayer.setDataSource(mFileName);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this, "正在播放录音", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("AudioRecorder", "播放录音失败: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "播放录音失败", Toast.LENGTH_SHORT).show();
            releaseMediaPlayer();
        }
    }

    private String getFilePath() {
        // 使用getExternalFilesDir来获取应用私有的外部存储目录
        File audioFolder = new File(getExternalFilesDir(null), AUDIO_RECORDER_FOLDER);
        if (!audioFolder.exists()) {
            audioFolder.mkdirs(); // 创建文件夹
        }
        return audioFolder.getAbsolutePath() + "/audiorecord" + System.currentTimeMillis() + AUDIO_RECORDER_FILE_EXT_3GP;
    }

    private void releaseRecorder() {
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseRecorder();
        releaseMediaPlayer();
    }
}