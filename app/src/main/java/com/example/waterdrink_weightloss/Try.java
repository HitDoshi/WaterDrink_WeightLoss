package com.example.waterdrink_weightloss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.waterdrink_weightloss.Database.DBHandler;
import com.example.waterdrink_weightloss.Database.DataModel;
import com.example.waterdrink_weightloss.adapter.CalendarAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class Try extends AppCompatActivity {

    DBHandler dbHandler;
    ArrayList<DataModel> arrayList;
    RecyclerView calendarRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try);
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        dbHandler = new DBHandler(this);

        Calendar c1 = Calendar.getInstance();
        //first day of week
        c1.set(Calendar.DAY_OF_WEEK, 1);

        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);

        Log.d("day1",day1+"");
        //last day of week
        c1.set(Calendar.DAY_OF_WEEK, 7);

        int year7 = c1.get(Calendar.YEAR);
        int month7 = c1.get(Calendar.MONTH);
        int day7 = c1.get(Calendar.DAY_OF_MONTH);

        arrayList = dbHandler.readDataWeekWise(day1,month1+1,year1,day7,month7+1,year7);

        CalendarAdapter calendarAdapter = new CalendarAdapter( this,arrayList,c1);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

    }
}