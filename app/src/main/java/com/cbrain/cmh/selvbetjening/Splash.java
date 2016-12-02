package com.cbrain.cmh.selvbetjening;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.widget.ImageView;

import static com.cbrain.cmh.selvbetjening.R.color.colorAccent;


public class Splash extends Activity {

    private static int SPLASH_TIME_OUT = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView iv = (ImageView)findViewById(R.id.logo);
        iv.setBackgroundColor(Color.BLUE);
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                Intent i = new Intent(Splash.this, Controller.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
