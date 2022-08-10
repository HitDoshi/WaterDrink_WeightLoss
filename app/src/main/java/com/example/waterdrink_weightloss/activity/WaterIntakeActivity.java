package com.example.waterdrink_weightloss.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.Model.PrefKey;
import com.example.waterdrink_weightloss.fragment.CupSizeFragment;
import com.example.waterdrink_weightloss.fragment.HomeFragment;
import com.example.waterdrink_weightloss.fragment.WeekGraphFragment;
import com.google.android.material.navigation.NavigationView;

public class WaterIntakeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    LinearLayout bar;
    //rate_app dialog
    AlertDialog rateApp_Dialog;
    AlertDialog.Builder builder1;
    View dialogView1;
    SharedPreferences userDataSharedPreferences;
    Toolbar toolbar;
    TextView title;
    ImageView bar1,bar2,bar3,bar4,bell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        /*this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_water_intake);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        bar = findViewById(R.id.bar);
        toolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.title);
        bar1 = findViewById(R.id.bar1);
        bar2 = findViewById(R.id.bar2);
        bar3 = findViewById(R.id.bar3);
        bar4 = findViewById(R.id.bar4);
        bell = findViewById(R.id.bell);

        userDataSharedPreferences = getSharedPreferences(PrefKey.SharePrefName, Context.MODE_PRIVATE);

        if(userDataSharedPreferences.getBoolean(PrefKey.Theme,true)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        if(userDataSharedPreferences.getBoolean(PrefKey.Theme,true)){
            title.setTextColor(Color.WHITE);
            bar1.setColorFilter(Color.WHITE);
            bar2.setColorFilter(Color.WHITE);
            bar3.setColorFilter(Color.WHITE);
            bar4.setColorFilter(Color.WHITE);
            bell.setColorFilter(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
        }
        else {
        }
//            toolbar.setBackgroundColor(Integer.parseInt(String.valueOf(R.color.black)));
            toolbar.getBackground().setAlpha(0);

        builder1 = new AlertDialog.Builder(getApplicationContext());
        dialogView1 = getLayoutInflater().inflate(R.layout.rate_app_dialog,null);
        //Custom Dialog box add
        builder1.setView(dialogView1);

        rateApp_Dialog = builder1.create();
        rateApp_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        navigationView = findViewById(R.id.navigation);

        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userDataSharedPreferences.getBoolean(PrefKey.ReminderOnOff,true)){
                    bell.setImageResource(R.drawable.bell_off);
                    userDataSharedPreferences.edit().putBoolean(PrefKey.ReminderOnOff,false).apply();
                }else{
                    bell.setImageResource(R.drawable.bell_on);
                    userDataSharedPreferences.edit().putBoolean(PrefKey.ReminderOnOff,true).apply();
                }
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.fragment_container,new HomeFragment());
        ft.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (item.getItemId())
        {
            case R.id.home:
            {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                ft.replace(R.id.fragment_container,new HomeFragment());
                ft.commit();
                return true;
            }

            case  R.id.cupSize:
            {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                ft.replace(R.id.fragment_container,new CupSizeFragment());
                ft.commit();
                return true;
            }

            case R.id.drink_report:
            {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                ft.replace(R.id.fragment_container,new WeekGraphFragment());
                ft.commit();
                return true;
            }

            case R.id.reminder:
            {
                startActivity(new Intent(this,SetReminderActivity.class));
                return true;
            }

            case R.id.setting:
            {
                startActivity(new Intent(this,SettingActivity.class));
                return true;
            }

            case R.id.share_app:
            {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"http://play.google.com/store/apps/details?id=" + getPackageName() +
                        "\n" + "This App Use For Water Drink Reminder \n " +
                        "And also user see drink record Day,Month & Year Wise"
                );
                startActivity(intent);
                break;
            }

            case R.id.rate_app:
            {
                userDataSharedPreferences.edit().putBoolean(PrefKey.RateApp,true).apply();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                ft.replace(R.id.fragment_container,new HomeFragment());
                ft.commit();
                return true;
            }

            case R.id.feedback:
            {
                Intent feedbackEmail = new Intent(Intent.ACTION_SEND);

                feedbackEmail.setType("text/email");
                feedbackEmail.putExtra(Intent.EXTRA_EMAIL, new String[] {"hit1.quad@gmail.com"});
                feedbackEmail.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                startActivity(Intent.createChooser(feedbackEmail, "Send Feedback:"));

                return true;
            }

            case R.id.more_app:
            {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/search?q=water+drink+reminder&c=apps") ));
                return true;
            }

            case R.id.privacy_policy:
            {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.freeprivacypolicy.com/live/921751a3-7807-40eb-9f1d-6ba5a221b22b") ));
                return true;
            }

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {

            if(getSupportFragmentManager().
                    findFragmentById(R.id.fragment_container).getClass().getSimpleName().equals("HomeFragment"))
            {
                //finishAffinity();
                startActivity(new Intent(this,ExitActivity.class));
            }
            else{
                Intent intent = new Intent(this, WaterIntakeActivity.class);
                startActivity(intent);
                finish();
            }
            super.onBackPressed();
        }
    }

}