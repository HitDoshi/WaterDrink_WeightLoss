package com.example.waterdrink_weightloss.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.waterdrink_weightloss.Model.PrefKey;
import com.example.waterdrink_weightloss.R;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences userDataSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        userDataSharedPreferences = getSharedPreferences(PrefKey.SharePrefName , Context.MODE_PRIVATE);

        Log.d("Splash_Activity","Call");

        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                //buttons[inew][jnew].setBackgroundColor(Color.BLACK);

                if (userDataSharedPreferences.getBoolean("FirstTime", true)){
                    startActivity(new Intent(getApplicationContext(), FirstTimeActivity.class));
                    finish();
                }
                else
                {
                    startActivity(new Intent(getApplicationContext(),WaterIntakeActivity.class));
                    finish();
                }
            }
        }, 2000);

    }

}