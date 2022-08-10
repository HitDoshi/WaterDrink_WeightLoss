package com.example.waterdrink_weightloss.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.Model.PrefKey;
import com.example.waterdrink_weightloss.Model.ReminderTime;
import com.example.waterdrink_weightloss.Recevier.ReminderBroadCast;
import com.example.waterdrink_weightloss.databinding.ActivityReminderBinding;
import com.example.waterdrink_weightloss.reclyclerview.ReminderListData;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.paperdb.Paper;

public class ReminderActivity extends AppCompatActivity {

    SwitchCompat reminder;
    EditText reminder_interval;
    ImageView edit_reminder;
    List<String> a = new ArrayList<>();
    TextView time;
    List<ReminderListData> reminderListDataList = new ArrayList<>();
    public ArrayList<PendingIntent> pendingIntentArrayList = new ArrayList<PendingIntent>();
    int temp=0;
    Boolean isChange=false;
    SharedPreferences reminderSharedPreferences;
    int wakeupHour , wakeupMin ,  badHour , badMin , interval = 60 ;
    Drawable upArrow;
    List<ReminderTime> reminderTime = new ArrayList<ReminderTime>();
    ActivityReminderBinding reminderBinding;
    ImageView sound;
    TextView set;

    AlertDialog set_ReminderDialog;
    AlertDialog.Builder builder;
    View dialogView;
    TextView ok,cancel,k;
    NumberPicker hour,min;
    Calendar h;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getSupportActionBar().setTitle("Reminder");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        upArrow = getResources().getDrawable(R.drawable.arrow_back);

        PrefManager prefManager = new PrefManager(getApplicationContext());
        reminderBinding = ActivityReminderBinding.inflate(getLayoutInflater());
        Paper.init(this);

        reminderSharedPreferences = getSharedPreferences(PrefKey.SharePrefName, Context.MODE_PRIVATE);
        interval = reminderSharedPreferences.getInt(PrefKey.Interval,60);

        builder = new AlertDialog.Builder(this);//this -- important not write other
        dialogView = getLayoutInflater().inflate(R.layout.set_reminder_dialog,null);
        //Custom Dialog box add
        builder.setView(dialogView);

        set_ReminderDialog = builder.create();
        set_ReminderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        reminder = findViewById(R.id.reminder);
        reminder_interval = findViewById(R.id.reminder_interval);
        edit_reminder = findViewById(R.id.edit_reminder);
        time = findViewById(R.id.time);
        sound = findViewById(R.id.sound);
        set = findViewById(R.id.set);
        //dialog
        k = dialogView.findViewById(R.id.t);
        hour = dialogView.findViewById(R.id.hour);
        min = dialogView.findViewById(R.id.minute);
        ok = dialogView.findViewById(R.id.ok);
        cancel = dialogView.findViewById(R.id.cancel);

        h = Calendar.getInstance();

        reminder_interval.setText(interval+"");

        if(reminderSharedPreferences.getBoolean(PrefKey.ReminderOnOff,false)){
            reminder.setChecked(true);
        }//Important.. Write this code before reminder Onclick CheckedChange Listener.

        if(reminderSharedPreferences.getBoolean(PrefKey.Theme,true)){
            darkStatusBar();
        }else{
            lightStatusBar();
        }

        hour.setMinValue(0);
        hour.setMaxValue(23);
        hour.setValue(h.get(Calendar.HOUR_OF_DAY));

        min.setMinValue(0);
        min.setMaxValue(59);
        min.setValue(h.get(Calendar.MINUTE));

        boolean theme = reminderSharedPreferences.getBoolean(PrefKey.Theme,true);

        if(theme){
            dialogView.setBackgroundResource(R.drawable.dark_dialog_shape);
            k.setTextColor(Color.WHITE);
            //setWeatherDialogDarkMode();
        }else{
            dialogView.setBackgroundResource(R.drawable.light_dialog_shape);
        }

        reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    try {
                        setSharedPreference();
                        setReminder();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    getSharedPreference();
                    removeReminder();
                }
            }
        });

        edit_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefManager.setFirstTimeLaunch(true);
                startActivity(new Intent(getApplicationContext(), UserInformation.class));
            }
        });

        reminder_interval.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isChange = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reminderSharedPreferences.getBoolean(PrefKey.Sound,true)){
                    sound.setImageResource(R.drawable.sound_off);
                    reminderSharedPreferences.edit().putBoolean(PrefKey.Sound,false).apply();
                }else{
                    sound.setImageResource(R.drawable.sound_on);
                    reminderSharedPreferences.edit().putBoolean(PrefKey.Sound,true).apply();
                }
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_ReminderDialog.show();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_ReminderDialog.dismiss();

                reminderTime.clear();
                pendingIntentArrayList.clear();
                reminderTime = Paper.book().read("ReminderTimeList");
                if(reminderTime!=null) {
                    temp = reminderTime.size()+1;
                }
                else{
                    reminderTime = new ArrayList<ReminderTime>();
                }
                Intent intent = new Intent(getApplicationContext(), ReminderBroadCast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), temp, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pendingIntent );
                //for repeting
                AlarmManager alarmManager = (AlarmManager) getApplicationContext().
                        getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP,( (hour.getValue()*60*60*1000) + (min.getValue()*60*1000) )
                        , pendingIntent);
                //Log.d("Time", calendar.getTimeInMillis() + "");
                pendingIntentArrayList.add(pendingIntent);
                
                reminderTime.add(new ReminderTime(hour.getValue(),min.getValue(),intent,temp));

                Paper.book().write("ReminderTimeList", reminderTime);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_ReminderDialog.dismiss();
            }
        });
    }

    void setReminder() throws ParseException {

        int i=wakeupHour , j=wakeupMin , min = badMin , hour = badHour ;
        temp = 0 ;
        reminderTime.clear();
        for (int k=0;k<pendingIntentArrayList.size();k++){
            pendingIntentArrayList.get(k).cancel();
        }
        pendingIntentArrayList.clear();
        reminderTime = Paper.book().read("ReminderTimeList");
        if(reminderTime!=null) {
            for (int k = 0; k < reminderTime.size(); k++) {
                AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                PendingIntent ped = PendingIntent.getBroadcast(getApplicationContext(), k,
                        reminderTime.get(k).getPendingIntent(), 0);
                alarmManager.cancel(ped);
                ped.cancel();
            }
            reminderTime.clear();
        }
        else{
            reminderTime = new ArrayList<ReminderTime>();
        }


        Calendar calendar = Calendar.getInstance();
        Paper.book().write("ReminderTimeList",reminderTime);
        
        while (i<badHour)
        {
            calendar.set(Calendar.HOUR_OF_DAY,i);
            calendar.set(Calendar.MINUTE,j);
            calendar.set(Calendar.SECOND,0);
            calendar.add(Calendar.MINUTE,interval);
            Log.d("Calender",calendar.get(Calendar.HOUR_OF_DAY)+" "+calendar.get(Calendar.MINUTE));

            i = calendar.get(Calendar.HOUR_OF_DAY);
            j = calendar.get(Calendar.MINUTE);
            ReminderListData data = new ReminderListData();
            String first , second  ;
            first = String.format("%02d", i);
            second = String.format("%02d", j);
            data.setTime(first+":"+second);
            int x = i;

            if(System.currentTimeMillis()<calendar.getTimeInMillis()) {
                if(temp==0){
                    setSharedPreference();
                }
                reminderListDataList.add(data);
//}
                Intent intent = new Intent(getApplicationContext(), ReminderBroadCast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), temp, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pendingIntent );
                //for repeting
                AlarmManager alarmManager = (AlarmManager) getApplicationContext().
                        getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                //Log.d("Time", calendar.getTimeInMillis() + "");
                pendingIntentArrayList.add(pendingIntent);
                reminderTime.add(new ReminderTime(i,j,intent,temp));

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

                a.add("abc");
                temp++;
                Log.d("set ",temp+"");
            }
            
        }

        Paper.book().write("ReminderTimeList", reminderTime);
        //Paper.book().write("PendingReminderList", pendingIntentArrayList);
        reminderTime.clear();
        reminderTime = Paper.book().read("ReminderTimeList");
        Log.d("SET", reminderTime.get(1).getHour() + " " + reminderTime.get(1).getMin());
        Log.d("PED",pendingIntentArrayList.size()+"");
        //Log.d("Cal",cal.getTime()+"");
    }

    void removeReminder(){

            reminderTime.clear();
            reminderTime = Paper.book().read("ReminderTimeList");
            for (int k = 0; k < reminderTime.size(); k++){
                AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                PendingIntent p = PendingIntent.getBroadcast(getApplicationContext(), k,
                        reminderTime.get(k).getPendingIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(p);
                Log.d("remove ",k+"");
                p.cancel();
            }

            reminderTime.clear();
            Paper.book().write("ReminderTimeList",reminderTime);
    }

    void setSharedPreference(){
        reminderSharedPreferences.edit().putInt(PrefKey.Reminder_Count,temp).apply();
        reminderSharedPreferences.edit().putBoolean(PrefKey.ReminderOnOff,true).apply();
    }

    void getSharedPreference(){
        temp = reminderSharedPreferences.getInt(PrefKey.Reminder_Count,0);
        reminderSharedPreferences.edit().putInt(PrefKey.Reminder_Count,0).apply();
        reminderSharedPreferences.edit().putBoolean(PrefKey.ReminderOnOff,false).apply();
        reminderSharedPreferences.getBoolean(PrefKey.ReminderOnOff,false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        wakeupHour = reminderSharedPreferences.getInt(PrefKey.Wake_up_Hour,7);
        wakeupMin = reminderSharedPreferences.getInt(PrefKey.Wake_up_Min,0);

        badHour = reminderSharedPreferences.getInt(PrefKey.Bed_Hour,22);
        badMin = reminderSharedPreferences.getInt(PrefKey.Bed_Min,0);

        time.setText(String.format("%02d",wakeupHour) + ":" + String.format("%02d",wakeupMin)
                +" - " + String.format("%02d",badHour) +":" + String.format("%02d",badMin));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(isChange) {
            if (!reminder_interval.getText().toString().equals("")) {
                if (Integer.parseInt(reminder_interval.getText().toString()) > 0) {
                    reminderSharedPreferences.edit().putInt(PrefKey.Interval, Integer.parseInt(reminder_interval.getText().toString())).apply();
                } else {
                    reminder_interval.setText("60");
                    reminderSharedPreferences.edit().putInt(PrefKey.Interval, 60).apply();
                    Toast.makeText(this, "Please Enter Reminder Interval Greater Then 0 min", Toast.LENGTH_SHORT).show();
                }
            } else {
                reminder_interval.setText("60");
                reminderSharedPreferences.edit().putInt(PrefKey.Interval, 60).apply();
                Toast.makeText(this, "Please Enter Reminder Interval Greater Then 0 min", Toast.LENGTH_SHORT).show();
            }

            interval = reminderSharedPreferences.getInt(PrefKey.Interval,60);

            try {
                setReminder();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //removeReminder();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),WaterIntakeActivity.class));
        finish();
    }

    public void darkStatusBar(){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        reminderBinding.t1.setTextColor(Color.WHITE);
        reminderBinding.t2.setTextColor(Color.WHITE);
        reminderBinding.t3.setTextColor(Color.WHITE);
        reminderBinding.t4.setTextColor(Color.WHITE);
        reminderBinding.t5.setTextColor(Color.WHITE);
    }

    public void lightStatusBar(){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        Spannable text = new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }
}