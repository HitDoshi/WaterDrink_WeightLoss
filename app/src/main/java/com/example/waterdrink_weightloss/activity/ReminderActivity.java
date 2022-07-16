package com.example.waterdrink_weightloss.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.activity.Model.ReminderTime;
import com.example.waterdrink_weightloss.activity.Recevier.ReminderBroadCast;
import com.example.waterdrink_weightloss.databinding.ActivityReminderBinding;
import com.example.waterdrink_weightloss.reclyclerview.ReminderListData;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
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

        reminderSharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        interval = reminderSharedPreferences.getInt("Interval",60);

        reminder = findViewById(R.id.reminder);
        reminder_interval = findViewById(R.id.reminder_interval);
        edit_reminder = findViewById(R.id.edit_reminder);
        time = findViewById(R.id.time);

        reminder_interval.setText(interval+"");

        if(reminderSharedPreferences.getBoolean("ReminderOnOff",false)){
            reminder.setChecked(true);
        }//Important.. Write this code before reminder Onclick CheckedChange Listener.

        if(reminderSharedPreferences.getBoolean("Theme",false)){
            darkStatusBar();
        }else{
            lightStatusBar();
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
    }

    void setReminder() throws ParseException {
       /* int wakeupHour = 7 ;
        int wakeupMin = 30 ;
        int badHour = 22 ;
        int badMin = 0 ;
        int interval = 60 ;*/

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

/*
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date d1 = df.parse("13:30:0"); //date 1
        Date d2 = df.parse("0:120:0"); // date 2
        long sum = d1.getTime() + d2.getTime();
*/
/*
        String myTime = "12:42";
        SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");
        Date d = df1.parse(myTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, 2);
        String newTime = df1.format(cal.getTime());

        Log.d("Time",newTime);*/

        Calendar calendar = Calendar.getInstance();
        Calendar currentTime = Calendar.getInstance();
        /* SimpleDateFormat df = new SimpleDateFormat("HH");*/
        //calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        /*calendar.set(Calendar.HOUR_OF_DAY,wakeupHour);
        calendar.set(Calendar.MINUTE,wakeupMin);
        calendar.add(Calendar.MINUTE,interval);*/
/*

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getActivity(), ExecutableServices.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

        //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pendingIntent );
        //for repeting
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() ,
                interval*60*1000,pendingIntent );
*/
      /*  Log.d("aaa", String.valueOf(df.parse(String.valueOf(a)).getHours()));
        Log.d("Calender",calendar.get(Calendar.HOUR)+" "+calendar.get(Calendar.MINUTE));*/

        //Paper.book().write("PendingReminderList",pendingIntentArrayList);
        Paper.book().write("ReminderTimeList",reminderTime);
        //Log.d("RealTime",calendar.getTimeInMillis()+"");
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
                reminderTime.add(new ReminderTime(i,j,intent));
                a.add("abc");
                temp++;
                Log.d("set ",temp+"");
            }
            //Log.d("Start","start");
            //alarmManager.cancel(pendingIntent);
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
        reminderSharedPreferences.edit().putInt("Reminder",temp).apply();
        reminderSharedPreferences.edit().putBoolean("ReminderOnOff",true).apply();
    }

    void getSharedPreference(){
        temp = reminderSharedPreferences.getInt("Reminder",0);
        reminderSharedPreferences.edit().putInt("Reminder",0).apply();
        reminderSharedPreferences.edit().putBoolean("ReminderOnOff",false).apply();
        reminderSharedPreferences.getBoolean("ReminderOnOff",false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        wakeupHour = reminderSharedPreferences.getInt("wake_up_hour",7);
        wakeupMin = reminderSharedPreferences.getInt("wake_up_min",0);

        badHour = reminderSharedPreferences.getInt("bed_hour",22);
        badMin = reminderSharedPreferences.getInt("bed_min",0);

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
                    reminderSharedPreferences.edit().putInt("Interval", Integer.parseInt(reminder_interval.getText().toString())).apply();
                } else {
                    reminder_interval.setText("60");
                    reminderSharedPreferences.edit().putInt("Interval", 60).apply();
                    Toast.makeText(this, "Please Enter Reminder Interval Greater Then 0 min", Toast.LENGTH_SHORT).show();
                }
            } else {
                reminder_interval.setText("60");
                reminderSharedPreferences.edit().putInt("Interval", 60).apply();
                Toast.makeText(this, "Please Enter Reminder Interval Greater Then 0 min", Toast.LENGTH_SHORT).show();
            }

            interval = reminderSharedPreferences.getInt("Interval",60);

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
}