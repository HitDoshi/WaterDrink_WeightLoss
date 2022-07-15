package com.example.waterdrink_weightloss.activity.Model;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.waterdrink_weightloss.activity.Recevier.ReminderBroadCast;
import com.example.waterdrink_weightloss.reclyclerview.ReminderListData;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.paperdb.Paper;

public class ReminderSetModel extends Application {

    int wakeupHour, wakeupMin, badHour, badMin, interval = 60, temp = 0;
    List<ReminderTime> reminderTime = new ArrayList<ReminderTime>();
    public ArrayList<PendingIntent> pendingIntentArrayList = new ArrayList<PendingIntent>();
    List<ReminderListData> reminderListDataList = new ArrayList<>();
    SharedPreferences reminderSharedPreferences;

    void setReminder() throws ParseException {
        Paper.init(this);
       /* int wakeupHour = 7 ;
        int wakeupMin = 30 ;
        int badHour = 22 ;
        int badMin = 0 ;
        int interval = 60 ;*/

        reminderSharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        wakeupHour = reminderSharedPreferences.getInt("wake_up_hour", 7);
        wakeupMin = reminderSharedPreferences.getInt("wake_up_min", 0);

        badHour = reminderSharedPreferences.getInt("bed_hour", 22);
        badMin = reminderSharedPreferences.getInt("bed_min", 0);
        interval = reminderSharedPreferences.getInt("Interval",60);

        int i = wakeupHour, j = wakeupMin, min = badMin, hour = badHour;
        temp = 0;
        reminderTime.clear();
        for (int k = 0; k < pendingIntentArrayList.size(); k++) {
            pendingIntentArrayList.get(k).cancel();
        }

        pendingIntentArrayList.clear();
        reminderTime = Paper.book().read("ReminderTimeList");
        if (reminderTime != null) {
            for (int k = 0; k < reminderTime.size(); k++) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                PendingIntent ped = PendingIntent.getBroadcast(getApplicationContext(), k,
                        reminderTime.get(k).getPendingIntent(), 0);
                alarmManager.cancel(ped);
                ped.cancel();
            }
            reminderTime.clear();
        } else {
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
        Paper.book().write("ReminderTimeList", reminderTime);
        //Log.d("RealTime",calendar.getTimeInMillis()+"");
        while (i < badHour) {
            calendar.set(Calendar.HOUR_OF_DAY, i);
            calendar.set(Calendar.MINUTE, j);
            calendar.set(Calendar.SECOND, 0);
            calendar.add(Calendar.MINUTE, interval);
            Log.d("Calender", calendar.get(Calendar.HOUR_OF_DAY) + " " + calendar.get(Calendar.MINUTE));

            i = calendar.get(Calendar.HOUR_OF_DAY);
            j = calendar.get(Calendar.MINUTE);
            ReminderListData data = new ReminderListData();
            String first, second;
            first = String.format("%02d", i);
            second = String.format("%02d", j);
            data.setTime(first + ":" + second);
            int x = i;

            if (System.currentTimeMillis() < calendar.getTimeInMillis()) {
                reminderListDataList.add(data);
//}
                Intent intent = new Intent(getApplicationContext(), ReminderBroadCast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), temp, intent,
                        0);
                //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pendingIntent );
                //for repeting
                AlarmManager alarmManager = (AlarmManager) getApplicationContext().
                        getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                //Log.d("Time", calendar.getTimeInMillis() + "");
                pendingIntentArrayList.add(pendingIntent);
                reminderTime.add(new ReminderTime(i, j, intent));
                temp++;
                Log.d("set ", temp + "");
            }
            //Log.d("Start","start");
            //alarmManager.cancel(pendingIntent);
        }

        Paper.book().write("ReminderTimeList", reminderTime);
        //Paper.book().write("PendingReminderList", pendingIntentArrayList);
        reminderTime.clear();
        reminderTime = Paper.book().read("ReminderTimeList");
        Log.d("SET", reminderTime.get(1).getHour() + " " + reminderTime.get(1).getMin());
        Log.d("PED", pendingIntentArrayList.size() + "");

        setSharedPreference();
        //Log.d("Cal",cal.getTime()+"");
    }

    void removeReminder() {

        for (int i = 0; i < temp; i++) {

            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

            if (pendingIntentArrayList.get(i) != null) {
                alarmManager.cancel(pendingIntentArrayList.get(i));
                Log.d("remove ", i + "");
            }
        }
    }

    void setSharedPreference() {
        reminderSharedPreferences.edit().putInt("Reminder", temp).apply();
        reminderSharedPreferences.edit().putBoolean("ReminderOnOff", true).apply();
    }

    void getSharedPreference() {
        temp = reminderSharedPreferences.getInt("Reminder", 0);
        reminderSharedPreferences.edit().putInt("Reminder", 0).apply();
        reminderSharedPreferences.edit().putBoolean("ReminderOnOff", false).apply();
        reminderSharedPreferences.getBoolean("ReminderOnOff", false);
    }

}
