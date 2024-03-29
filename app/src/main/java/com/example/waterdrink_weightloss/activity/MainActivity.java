package com.example.waterdrink_weightloss.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.Model.PrefKey;

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

        userDataSharedPreferences = getSharedPreferences(PrefKey.SharePrefName , Context.MODE_PRIVATE);

        if(userDataSharedPreferences.getBoolean( PrefKey.Theme ,true)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            userDataSharedPreferences.edit().putBoolean(PrefKey.Theme,false).apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        Log.d("MainActivity","Call");
        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
        startActivity(intent);
        finish();

    }
}