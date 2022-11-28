package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    ImageView imgSplash;
    Animation alpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //To vanish status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //To apply animation
        imgSplash = findViewById(R.id.imgSplash);
        alpha = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.splashanimation);

        imgSplash.setAnimation(alpha);

        //To hold the splashscreen for 4 sec
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent iHome = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(iHome);
                finish();//so that on back press splash should not be displayed
            }
        },6000);

    }
}