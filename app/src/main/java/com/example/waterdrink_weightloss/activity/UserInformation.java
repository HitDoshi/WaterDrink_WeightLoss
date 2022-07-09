package com.example.waterdrink_weightloss.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.fragment.AgeWightFragment;
import com.example.waterdrink_weightloss.fragment.GenderFragment;
import com.example.waterdrink_weightloss.fragment.TimeFragment;
import com.google.android.material.tabs.TabLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInformation extends AppCompatActivity {

    private ViewPager viewPager;

    private ViewAdapter viewAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setContentView(R.layout.activity_user_information);

        // Checking for first time launch - before calling setContentView()
        PrefManager prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            Intent intent = new Intent(this,WaterIntakeActivity.class);
            startActivity(intent);
            finish();
        }

/*
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
*/

        setContentView(R.layout.activity_user_information);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(viewPager, true);
        // setting up the adapter
        viewAdapter = new ViewAdapter(getSupportFragmentManager());
        // add the fragments
        viewAdapter.add(new GenderFragment());
        viewAdapter.add(new AgeWightFragment());
        viewAdapter.add(new TimeFragment());

        // Set the adapter
        viewPager.setAdapter(viewAdapter);
/*
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(UserInformation.this, "Select", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });*/
    }
}