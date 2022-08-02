package com.example.waterdrink_weightloss.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.waterdrink_weightloss.R;

public class MainActivity extends AppCompatActivity {

    Button start ;
    SharedPreferences userDataSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
        startActivity(intent);

        userDataSharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        userDataSharedPreferences.edit().putBoolean("ReminderOnOff",true).apply();

        if(userDataSharedPreferences.getBoolean("Theme",false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        //startActivity(new Intent(getApplicationContext(),SplashActivity.class));

        start = findViewById(R.id.start);
        PrefManager prefManager = new PrefManager(this);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!prefManager.isFirstTimeLaunch()) {
                    Intent intent = new Intent(getApplicationContext(),WaterIntakeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, UserInformation.class);
                    startActivity(intent);
                }
            }
        });
    }
}