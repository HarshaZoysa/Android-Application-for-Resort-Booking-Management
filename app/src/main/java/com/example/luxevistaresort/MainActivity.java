package com.example.luxevistaresort;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    Button regbtn, logbtn;
    private VideoView videoBackground;

    private void playVideo() {
        VideoView videoBackground = findViewById(R.id.videoBackground);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.bg2;

        videoBackground.setVideoURI(Uri.parse(videoPath));
        videoBackground.setOnPreparedListener(mp -> {
            mp.setLooping(true); // Loop the video
            mp.start();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        playVideo();
    }
    @Override
    protected void onPause() {
        super.onPause();
        videoBackground.stopPlayback();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        regbtn = findViewById(R.id.Reg_button);
        logbtn = findViewById(R.id.Login_button);

        videoBackground = findViewById(R.id.videoBackground);
        playVideo();





        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register_page.class);
                startActivity(intent);
            }

        });
//Login page Open Button
        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login_page.class);
                startActivity(intent);
            }

        });

        ImageButton adminbtn = findViewById(R.id.adminLoginBtn);
        adminbtn. setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, adminLoginScreen.class);
                startActivity(intent);
            }
        });



    }
}