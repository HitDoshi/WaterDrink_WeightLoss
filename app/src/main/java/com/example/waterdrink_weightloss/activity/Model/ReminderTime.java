package com.example.waterdrink_weightloss.activity.Model;

import android.app.PendingIntent;
import android.content.Intent;

import com.example.waterdrink_weightloss.activity.ReminderActivity;

public class ReminderTime {

    int hour , min;
    Intent pendingIntent;

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public Intent getPendingIntent() {
        return pendingIntent;
    }

    public void setPendingIntent(Intent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }

    public ReminderTime(int hour , int min , Intent pendingIntent){
        this.hour = hour;
        this.min = min;
        this.pendingIntent = pendingIntent;
    }
}

