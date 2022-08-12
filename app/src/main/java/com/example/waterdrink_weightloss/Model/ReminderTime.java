package com.example.waterdrink_weightloss.Model;

import android.content.Intent;

public class ReminderTime {

    int hour , min , temp;
    Intent intent;
    long milli_time;

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

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public long getMilli_time() {
        return milli_time;
    }

    public void setMilli_time(long milli_time) {
        this.milli_time = milli_time;
    }

    public ReminderTime(int hour , int min , Intent intent, int temp,long milli_time){
        this.hour = hour;
        this.min = min;
        this.intent = intent;
        this.temp = temp;
        this.milli_time = milli_time ;
    }
}

