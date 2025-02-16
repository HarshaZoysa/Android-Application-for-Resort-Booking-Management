package com.example.luxevistaresort;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.VideoView;

public class Testpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testpage);


        VideoView videoView = findViewById(R.id.videoBackground);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.bg2);
        videoView.start();
        videoView.setOnPreparedListener(mp -> mp.setLooping(true));



    }
}