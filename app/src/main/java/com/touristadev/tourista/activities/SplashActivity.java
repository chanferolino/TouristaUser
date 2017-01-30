package com.touristadev.tourista.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.touristadev.tourista.R;

public class SplashActivity extends AppCompatActivity {

    private TextView t;

    private final int seconds = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash);

        //FONTS
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Poppins-Bold.ttf");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startIntent(intent);
            }
        }, seconds);
    }

    public void startIntent(Intent intent) {
        startActivity(intent);
    }
}
