package com.example.waterdrink_weightloss.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.fragment.DrinkReportFragment;
import com.example.waterdrink_weightloss.fragment.HomeFragment;
import com.example.waterdrink_weightloss.fragment.MonthGraphFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class WaterIntakeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    LinearLayout bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_intake);

        bar = findViewById(R.id.bar);

        navigationView = findViewById(R.id.navigation);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.fragment_container,new MonthGraphFragment());
        ft.commit();

        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout = findViewById(R.id.my_drawer_layout);
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
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
            case R.id.share_app:
            {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,getApplicationContext().getPackageName());
                startActivity(intent);
                break;
            }

            case R.id.history:
            {
                break;
            }

            case R.id.rate_app:
            {
                break;
            }

            case R.id.drink_report:
            {

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                ft.replace(R.id.fragment_container,new DrinkReportFragment());
                ft.commit();
                return true;
            }
        }
        return false;
    }

   /* @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

}