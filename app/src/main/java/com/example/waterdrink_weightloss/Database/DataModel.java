package com.example.waterdrink_weightloss.Database;

public class DataModel {

    private int day;
    private int month;
    private int year;
    private int achievement;
    private int id;
    private String glass_add_record;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAchievement() {
        return achievement;
    }

    public void setAchievement(int achievement) {
        this.achievement = achievement;
    }

    public String getGlass_add_record() {
        return glass_add_record;
    }

    public void setGlass_add_record(String glass_add_record) {
        this.glass_add_record = glass_add_record;
    }

    // constructor
    public DataModel(int id, int day, int month, int year, int achievement, String glass_add_record) {
        this.id = id;
        this.day = day;
        this.month = month;
        this.year = year;
        this.achievement = achievement;
        this.glass_add_record = glass_add_record;
    }
}

