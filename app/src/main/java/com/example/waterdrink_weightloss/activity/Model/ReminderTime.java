package com.example.waterdrink_weightloss.activity.Model;

import com.example.waterdrink_weightloss.activity.ReminderActivity;

public class ReminderTime {

    int hour , min;

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

    public ReminderTime(int hour , int min){
        this.hour = hour;
        this.min = min;
    }
}

