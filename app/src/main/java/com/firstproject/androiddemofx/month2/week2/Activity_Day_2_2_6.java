package com.firstproject.androiddemofx.month2.week2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Activity_Day_2_2_6 extends AppCompatActivity {
    private TextView textView;
    private Button btnStart;
    private Button btnStopAndSave;
    private MediaRecorder mediaRecorder=null;
    private String outputFile=null;
    public static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day226);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textView = findViewById(R.id.tv226);
        btnStart = findViewById(R.id.btnStart226);
        btnStopAndSave = findViewById(R.id.btnStopAndSave);

//        mediaRecorder = new MediaRecorder();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });
        btnStopAndSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAndSaveRecording();
            }
        });

        //检查是否有录音和写入外部存储的权限，如果没有的话会向用户请求
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            } else {
                Toast.makeText(this, "具有录音权限\n和写入外部存储权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initRecorder(){
        File dir = new File(Environment.getExternalStorageDirectory(), AUDIO_RECORDER_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //创建一个SimpleDateFormat对象，指定日期格式为 “年（yyyy）月（MM）日（dd）_时（HH）分（mm）秒（ss）
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        //获取当前的日期时间字符串
        String currentDate = dateFormat.format(new Date());
        //构建一个输出文件的路径，将其存储在外部存储目录下，文件名以 “recording_” 加上当前日期时间，并以 “.mp4” 作为文件扩展名
        outputFile = dir.getAbsolutePath() + "/recording_" + currentDate + ".mp4";
        File mpFile = new File(outputFile);

        mediaRecorder = new MediaRecorder();
        //设置音频录制的来源为麦克风（AudioSource.MIC），即从设备的麦克风采集音频
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置录制音频的输出格式为 MPEG-4。这决定了录制的音频文件的存储格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        //设置音频的编码器为高级音频编码（AAC）
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        mediaRecorder.setOutputFile(mpFile.getAbsolutePath());
    }

    private void startRecording() {
        initRecorder();
        if (mediaRecorder != null) {
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                Toast.makeText(this, "开始录音", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopAndSaveRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
            } catch (IllegalStateException e) {
                Log.e("Error", "Error stopping MediaRecorder: " + e.getMessage());
            }
            mediaRecorder.release();
            mediaRecorder = null;
            Toast.makeText(this, "录音已保存", Toast.LENGTH_SHORT).show();
            textView.setText(outputFile);
            playRecording();
        }
    }

    private void playRecording() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被授予，可以进行录音操作
                Toast.makeText(this, "录音权限", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "没有录音权限", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被授予，可以进行录音操作
                Toast.makeText(this, "外部写入权限", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "没有外部写入权限", Toast.LENGTH_SHORT).show();
            }
        }
    }
}