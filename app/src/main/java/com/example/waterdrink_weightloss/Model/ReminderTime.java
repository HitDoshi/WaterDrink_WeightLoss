package com.example.waterdrink_weightloss.Model;

import android.content.Intent;

public class ReminderTime {

    int hour , min , temp;
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

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public Intent getPendingIntent() {
        return pendingIntent;
    }

    public void setPendingIntent(Intent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }

    public ReminderTime(int hour , int min , Intent pendingIntent,int temp){
        this.hour = hour;
        this.min = min;
        this.pendingIntent = pendingIntent;
        this.temp = temp;
    }
}

