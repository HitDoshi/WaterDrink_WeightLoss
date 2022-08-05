package com.example.waterdrink_weightloss.fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.activity.WaterIntakeActivity;

import pl.droidsonroids.gif.GifImageView;

public class LoadingActivity extends AppCompatActivity {

    GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loding);
        
        gifImageView = findViewById(R.id.gif);

        gifImageView.setBackgroundColor(Color.TRANSPARENT);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Intent intent = new Intent(getApplicationContext(), WaterIntakeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2500);

    }
}