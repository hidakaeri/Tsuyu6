package com.example.tsuyu6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    //待ち時間用のHandlerと判定
    final Handler handler = new Handler();
    private boolean wait_time = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        wait_time();


    }

    public void wait_time(){
        wait_time = false;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                wait_time = true;
                Intent intent = new Intent(SplashScreen.this, Look.class);
                startActivity(intent);

                finish();
            }
        }, 1000);
    }
}