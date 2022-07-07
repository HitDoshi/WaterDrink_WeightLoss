package com.example.waterdrink_weightloss.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.waterdrink_weightloss.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setTitle("Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}