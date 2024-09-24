package com.firstproject.androiddemofx.month2.week2;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstproject.androiddemofx.R;

public class Activity_Day_2_2_5 extends AppCompatActivity {
    private boolean isPlaying = false;
    private Button btnIsPlaying;
    private Button btnChange;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day225);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tv = findViewById(R.id.tv225);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.flowersea);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
//        try {
//            mediaPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String resourceName = getResources().getResourceEntryName(R.raw.flowersea);

        btnIsPlaying = findViewById(R.id.btnStart225);
        btnIsPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    isPlaying = false;
                    mediaPlayer.pause();
                } else {
                    isPlaying = true;
                    mediaPlayer.start();
                    tv.setText(resourceName);
                }
            }
        });

        btnChange = findViewById(R.id.btnChange225);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioManager.getMode() == AudioManager.MODE_NORMAL) {
                    // 当前是喇叭播放，切换到听筒播放
                    audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                    audioManager.setSpeakerphoneOn(false);
                } else {
                    // 当前是听筒播放，切换到喇叭播放
                    audioManager.setMode(AudioManager.MODE_NORMAL);
                    audioManager.setSpeakerphoneOn(true);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}