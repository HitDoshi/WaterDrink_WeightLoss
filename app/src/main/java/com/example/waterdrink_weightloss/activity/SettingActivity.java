package com.example.waterdrink_weightloss.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.fragment.GenderFragment;

public class SettingActivity extends AppCompatActivity {

    TextView gender , weight , wake_up_time , bed_time , goal;
    SharedPreferences userDataSharedPreferences;
    SharedPreferences themeSharedPref;
    SwitchCompat theme , tips;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setTitle("Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        PrefManager prefManager = new PrefManager(getApplicationContext());

        gender = findViewById(R.id.gender);
        weight = findViewById(R.id.weight);
        wake_up_time = findViewById(R.id.wake_up_time);
        bed_time = findViewById(R.id.bed_time);
        theme = findViewById(R.id.theme);
        goal = findViewById(R.id.goal);
        tips = findViewById(R.id.hide_tips);

        userDataSharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        themeSharedPref = getSharedPreferences("MySharedPref",MODE_PRIVATE);

        gender.setText(userDataSharedPreferences.getString("gender","21"));
        weight.setText(userDataSharedPreferences.getString("weight","55"));
        wake_up_time.setText(userDataSharedPreferences.getInt("wake_up_hour",7) + " : "+
                userDataSharedPreferences.getInt("wake_up_min",0));
        bed_time.setText(userDataSharedPreferences.getInt("bed_hour",11) + " : "+
                userDataSharedPreferences.getInt("bed_min",0));
        goal.setText(userDataSharedPreferences.getInt("target_ml",1500)+ " ml");
        theme.setChecked(themeSharedPref.getBoolean("Theme",false));
        tips.setChecked(themeSharedPref.getBoolean("Tips",false));

        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefManager.setFirstTimeLaunch(true);
                startActivity(new Intent(getApplicationContext(), UserInformation.class));
            }
        });

        weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefManager.setFirstTimeLaunch(true);
                startActivity(new Intent(getApplicationContext(), UserInformation.class));
            }
        });

        wake_up_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefManager.setFirstTimeLaunch(true);
                startActivity(new Intent(getApplicationContext(), UserInformation.class));
            }
        });

        bed_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefManager.setFirstTimeLaunch(true);
                startActivity(new Intent(getApplicationContext(), UserInformation.class));
            }
        });

        goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefManager.setFirstTimeLaunch(true);
                startActivity(new Intent(getApplicationContext(), UserInformation.class));
            }
        });

        theme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                if(check){
                    goInDarkMode();
                }else{
                    goInLightMode();
                }
            }
        });

        tips.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                themeSharedPref.edit().putBoolean("Tips",isChecked).apply();
            }
        });

    }

    private void goInLightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        themeSharedPref.edit().putBoolean("Theme",false).apply();
    }

    private void goInDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        themeSharedPref.edit().putBoolean("Theme",true).apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),WaterIntakeActivity.class));
        finish();
    }
}