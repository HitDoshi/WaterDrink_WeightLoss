package com.example.waterdrink_weightloss.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.waterdrink_weightloss.Database.DataModel;
import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.activity.Model.ReminderTime;
import com.example.waterdrink_weightloss.activity.Recevier.ReminderBroadCast;
import com.example.waterdrink_weightloss.databinding.ActivityReminderBinding;
import com.example.waterdrink_weightloss.reclyclerview.ReminderListAdapter;
import com.example.waterdrink_weightloss.reclyclerview.ReminderListData;
import com.example.waterdrink_weightloss.reclyclerview.ReminderSetAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import io.paperdb.Paper;

public class SetReminderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    SharedPreferences reminderSharedPreferences;

    List<ReminderListData> reminderListDataList = new ArrayList<>();
    public ArrayList<PendingIntent> pendingIntentArrayList = new ArrayList<PendingIntent>();
    List<ReminderTime> reminderTime = new ArrayList<ReminderTime>();
    Drawable upArrow;

    //dialog
    AlertDialog set_ReminderDialog;
    AlertDialog.Builder builder;
    View dialogView;
    TextView ok,cancel,k;
    NumberPicker hour,min;
    Calendar h,calendar;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getSupportActionBar().setTitle("Reminder");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        upArrow = getResources().getDrawable(R.drawable.arrow_back);
        setContentView(R.layout.activity_set_reminder);
        recyclerView = findViewById(R.id.setReminderRecylerView);
        floatingActionButton = findViewById(R.id.fab_AddReminder);
        Paper.init(this);

        reminderSharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        builder = new AlertDialog.Builder(this);//this -- important not write other
        dialogView = getLayoutInflater().inflate(R.layout.set_reminder_dialog,null);
        //Custom Dialog box add
        builder.setView(dialogView);

        set_ReminderDialog = builder.create();
        set_ReminderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //dialog
        k = dialogView.findViewById(R.id.t);
        hour = dialogView.findViewById(R.id.hour);
        min = dialogView.findViewById(R.id.minute);
        ok = dialogView.findViewById(R.id.ok);
        cancel = dialogView.findViewById(R.id.cancel);

        h = Calendar.getInstance();
        calendar = Calendar.getInstance();

        if(reminderSharedPreferences.getBoolean("Theme",false)){
            darkStatusBar();
        }else{
            lightStatusBar();
        }

        reminderTime = Paper.book().read("ReminderTimeList");

        if(reminderTime!=null && reminderTime.size()!=0) {
            setRecylerView();
        }
        else{
            reminderTime = new ArrayList<ReminderTime>();
        }

        boolean theme = reminderSharedPreferences.getBoolean("Theme",false);

        if(theme){
            dialogView.setBackgroundResource(R.drawable.dark_dialog_shape);
            k.setTextColor(Color.WHITE);
            //setWeatherDialogDarkMode();
        }else{
            dialogView.setBackgroundResource(R.drawable.light_dialog_shape);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                h = Calendar.getInstance();
                hour.setMinValue(h.get(Calendar.HOUR_OF_DAY));
                hour.setMaxValue(23);
                hour.setValue(h.get(Calendar.HOUR_OF_DAY));

                min.setMinValue(h.get(Calendar.MINUTE));
                min.setMaxValue(59);
                min.setValue(h.get(Calendar.MINUTE));

                set_ReminderDialog.show();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_ReminderDialog.dismiss();

                int temp=0;
                reminderTime.clear();
                pendingIntentArrayList.clear();
                reminderTime = Paper.book().read("ReminderTimeList");
                if(reminderTime!=null && reminderTime.size()!=0) {
                    temp = reminderSharedPreferences.getInt("Reminder",0);
                    temp = temp + 1;
                    Log.d("Temp",temp+"");
                }
                else{
                    reminderTime = new ArrayList<ReminderTime>();
                    reminderSharedPreferences.edit().putInt("Reminder",temp).apply();
                }

                calendar.set(Calendar.HOUR_OF_DAY,hour.getValue());
                calendar.set(Calendar.MINUTE,min.getValue());
                calendar.set(Calendar.SECOND,0);

                Intent intent = new Intent(getApplicationContext(), ReminderBroadCast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                        temp, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pendingIntent );
                //for repeting
                AlarmManager alarmManager = (AlarmManager) getApplicationContext().
                        getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), pendingIntent);
                //Log.d("Time", calendar.getTimeInMillis() + "");
                pendingIntentArrayList.add(pendingIntent);

                reminderTime.add(new ReminderTime(hour.getValue(),min.getValue(),intent,temp));

                Collections.sort(reminderTime, new Comparator<ReminderTime>() {
                    @Override
                    public int compare(ReminderTime reminderTime, ReminderTime t) {
                        return Integer.compare(reminderTime.getMin(), t.getMin());
                    }
                });

                Collections.sort(reminderTime, new Comparator<ReminderTime>() {
                    @Override
                    public int compare(ReminderTime reminderTime, ReminderTime t) {
                        return Integer.compare(reminderTime.getHour(), t.getHour());
                    }
                });

                Log.d("Total set Reminder",reminderTime.size()+" temp(RequestCode)= "+temp);
                for (int i=0;i<reminderTime.size();i++){
                    Log.d("ReminderTime_Sort",reminderTime.get(i).getHour()+" : "+
                            reminderTime.get(i).getMin());
                }

                Paper.book().write("ReminderTimeList", reminderTime);
                reminderSharedPreferences.edit().putInt("Reminder",temp).apply();
                setRecylerView();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_ReminderDialog.dismiss();
            }
        });
    }

    private void setRecylerView() {
        ReminderSetAdapter adapter = new ReminderSetAdapter(this,reminderTime);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public void darkStatusBar(){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    public void lightStatusBar(){
//        reminderBinding.t1.setTextColor(getResources().getColor(R.color.black));
//        reminderBinding.t2.setTextColor(Color.BLACK);
//        reminderBinding.t3.setTextColor(Color.BLACK);
//        reminderBinding.t4.setTextColor(Color.BLACK);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        Spannable text = new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(), WaterIntakeActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}