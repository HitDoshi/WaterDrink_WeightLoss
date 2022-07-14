package com.example.waterdrink_weightloss.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.activity.Recevier.ReminderBroadCast;
import com.example.waterdrink_weightloss.reclyclerview.ReminderListData;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReminderActivity extends AppCompatActivity {

    SwitchCompat reminder;
    EditText reminder_interval;
    List<ReminderListData> reminderListDataList = new ArrayList<>();
    ArrayList<PendingIntent> pendingIntentArrayList = new ArrayList<PendingIntent>();
    int temp=0;
    SharedPreferences reminderSharedPreferences;
    int wakeupHour , wakeupMin ,  badHour , badMin , interval=5 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        getSupportActionBar().setTitle("Reminder");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reminderSharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        interval = reminderSharedPreferences.getInt("Interval",60);

        reminder = findViewById(R.id.reminder);
        reminder_interval = findViewById(R.id.reminder_interval);
        reminder_interval.setText(interval+"");

        if(reminderSharedPreferences.getBoolean("ReminderOnOff",false)){
            reminder.setChecked(true);
        }//Important.. Write this code before reminder Onclick CheckedChange Listener.

        reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    try {
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

    }

    void setReminder() throws ParseException {
       /* int wakeupHour = 7 ;
        int wakeupMin = 30 ;
        int badHour = 22 ;
        int badMin = 0 ;
        int interval = 60 ;*/

        int i=wakeupHour , j=wakeupMin , min = badMin , hour = badHour ;
        temp = 0 ;
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
                reminderListDataList.add(data);
            }
            Intent intent = new Intent(getApplicationContext(), ReminderBroadCast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), temp , intent,
                    0);
            //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pendingIntent );
            //for repeting
            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pendingIntent );
            Log.d("Time",calendar.getTimeInMillis()+"");
            pendingIntentArrayList.add(pendingIntent);
            //Log.d("Start","start");
            Log.d("set ",temp+"");
            //alarmManager.cancel(pendingIntent);
            temp++;
        }

        setSharedPreference();
        //Log.d("Cal",cal.getTime()+"");
    }

    void removeReminder(){

        for (int i=0;i<temp;i++) {

            Intent intent = new Intent(getApplicationContext(), ReminderBroadCast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), temp, intent,
                    0);

            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

            if(pendingIntent!=null){
                alarmManager.cancel(pendingIntent);
                Log.d("remove ",i+"");}
        }
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

        badHour = reminderSharedPreferences.getInt("bed_hour",11);
        badMin = reminderSharedPreferences.getInt("bed_min",0);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(!reminder_interval.getText().toString().equals("")){
            if(Integer.parseInt(reminder_interval.getText().toString())>0) {
                reminderSharedPreferences.edit().putInt("Interval", Integer.parseInt(reminder_interval.getText().toString())).apply();
            }else{
                reminder_interval.setText("60");
                reminderSharedPreferences.edit().putInt("Interval", 60).apply();
                Toast.makeText(this, "Please Enter Reminder Interval Greater Then 0 min", Toast.LENGTH_SHORT).show();
            }
        }
        else {
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

        removeReminder();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),WaterIntakeActivity.class));
        finish();
    }
}