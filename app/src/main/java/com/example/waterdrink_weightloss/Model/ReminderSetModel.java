package com.example.waterdrink_weightloss.Model;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.waterdrink_weightloss.Recevier.ReminderBroadCast;
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

        reminderSharedPreferences = getSharedPreferences(PrefKey.SharePrefName, Context.MODE_PRIVATE);
        wakeupHour = reminderSharedPreferences.getInt(PrefKey.Wake_up_Hour, 7);
        wakeupMin = reminderSharedPreferences.getInt(PrefKey.Wake_up_Min , 0);

        badHour = reminderSharedPreferences.getInt(PrefKey.Bed_Hour, 22);
        badMin = reminderSharedPreferences.getInt(PrefKey.Bed_Min, 0);
        interval = reminderSharedPreferences.getInt(PrefKey.Interval,60);

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
                        reminderTime.get(k).getIntent(), 0);
                alarmManager.cancel(ped);
                ped.cancel();
            }
            reminderTime.clear();
        } else {
            reminderTime = new ArrayList<ReminderTime>();
        }

        Calendar calendar = Calendar.getInstance();

        Paper.book().write("ReminderTimeList", reminderTime);
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
                reminderTime.add(new ReminderTime(i, j, intent,temp,0));
                temp++;
                Log.d("set ", temp + "");
            }
        }

        Paper.book().write("ReminderTimeList", reminderTime);
        //Paper.book().write("PendingReminderList", pendingIntentArrayList);
        reminderTime.clear();
        reminderTime = Paper.book().read("ReminderTimeList");
        Log.d("SET", reminderTime.get(1).getHour() + " " + reminderTime.get(1).getMin());
        Log.d("PED", pendingIntentArrayList.size() + "");

        setSharedPreference();
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
        reminderSharedPreferences.edit().putInt(PrefKey.Reminder_Count, temp).apply();
        reminderSharedPreferences.edit().putBoolean(PrefKey.ReminderOnOff, true).apply();
    }

    void getSharedPreference() {
        temp = reminderSharedPreferences.getInt(PrefKey.Reminder_Count, 0);
        reminderSharedPreferences.edit().putInt(PrefKey.Reminder_Count, 0).apply();
        reminderSharedPreferences.edit().putBoolean(PrefKey.ReminderOnOff, false).apply();
        reminderSharedPreferences.getBoolean(PrefKey.ReminderOnOff, false);
    }

}
