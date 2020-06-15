package com.example.android.darktheme.Widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.example.android.darktheme.MainActivity;
import com.example.android.darktheme.R;

import static java.lang.Thread.sleep;

public class Splash_Screen extends Activity {

    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable(){
            public void run() {
                    Intent splashIntent = new Intent(Splash_Screen.this, MainActivity.class);
                    startActivity(splashIntent);
                    finish();
            }
        },SPLASH_TIME_OUT);

    }
}