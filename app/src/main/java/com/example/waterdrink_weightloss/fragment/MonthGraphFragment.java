package com.example.waterdrink_weightloss.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.waterdrink_weightloss.BuildConfig;
import com.example.waterdrink_weightloss.Database.DBHandler;
import com.example.waterdrink_weightloss.Database.DataModel;
import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.databinding.FragmentMonthGraphBinding;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MonthGraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MonthGraphFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentMonthGraphBinding MonthReportFragmentBinding;
    int target_ml;
    SharedPreferences sharedPreferences;
    String[] month = {"January","February","March","April","May","Jun","July","August","September",
            "October","November","December"};
    String[] xAxisLables = new String[]{"1","2", "3", "4","5","6","7","8","9","10",
            "1","2", "3", "4","5","6","7","8","9","10",
            "1","2", "3", "4","5","6","7","8","9","10",
            };

    ArrayList<String> days = new ArrayList<String>();
    ArrayList<DataModel> arrayList = new ArrayList<>();
    ArrayList<Integer> linearlayout = new ArrayList<Integer>();
    int[] progressbar = new int[31];
    ArrayList<Integer> textview = new ArrayList<>();
    Resources r ;
    String packageName ;
    Calendar calendar;

    int position , j=1 , size , year , total=0;
    DBHandler dbHandler;
    ArrayList barEntriesArrayList;

    // variable for our bar data.
    BarData barData;

    // variable for our bar data set.
    BarDataSet barDataSet;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MonthGraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthGraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthGraphFragment newInstance(String param1, String param2) {
        MonthGraphFragment fragment = new MonthGraphFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = new DBHandler(getContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MonthReportFragmentBinding = FragmentMonthGraphBinding.inflate(getLayoutInflater());

        View view = inflater.inflate(R.layout.fragment_month_graph, container, false);
        setId();
        /*ProgressBar a = view.findViewById(progressbar.get(1));
        a.setProgress(50);*/

        return MonthReportFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MonthReportFragmentBinding.leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position--;

                if (position==-1)
                {
                    year--;
                    position=11;
                }

                calendar = new GregorianCalendar(year, position, 1);
                size = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                MonthReportFragmentBinding.selectedMonth.setText(month[position] + "  " + year);
                setData(position,year,size);

            }
        });

        MonthReportFragmentBinding.rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position++;

                if(position==12)
                {
                    position=0;
                    year++;
                }

                MonthReportFragmentBinding.selectedMonth.setText(month[position] + "  " + year);
                calendar = new GregorianCalendar(year, position, 1);
                size = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                setData(position,year,size);

            }
        });

        MonthReportFragmentBinding.week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment1 =new WeekGraphFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,fragment1);
                fragmentTransaction.commit();
            }
        });

        MonthReportFragmentBinding.year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment1 =new YearGraphFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,fragment1);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        sharedPreferences = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        target_ml = sharedPreferences.getInt("target_ml", 1500);
        calendar = Calendar.getInstance();
        position = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        Log.d("year",year+"");
        MonthReportFragmentBinding.selectedMonth.setText(month[position] + "  " + year);
        calendar = new GregorianCalendar(year, position, 1);
        size = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        setData(position,year,size);
        getTodayRecord();

        //setId();
    }

    private void setData(int position,int year,int size) {

        days.clear();
        for(int i=0;i<size;i++){
            days.add(String.valueOf(i+1));
        }

        Log.d("DaysOfMonth",size+"");
        j=1;
        arrayList.clear();
        arrayList = dbHandler.readDataMonthWise(position+1,year);
        Log.d("size",arrayList.size()+"");
        Collections.sort(arrayList, new Comparator<DataModel>() {
            @Override
            public int compare(DataModel dataModel, DataModel t1) {
                return Integer.compare(dataModel.getDay(), t1.getDay());
            }
        });

        for (int i=0;i<arrayList.size();i++){
            Log.d("a",arrayList.get(i).getDay()+"");
        }

        // creating a new array list
        barEntriesArrayList = new ArrayList<>();

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
    /*    barEntriesArrayList.add(new BarEntry(1f, 4));
        barEntriesArrayList.add(new BarEntry(2f, 6));
        barEntriesArrayList.add(new BarEntry(3f, 8));
        barEntriesArrayList.add(new BarEntry(4f, 2));*/

        for (int i=0;i<arrayList.size();i++) {
            Log.d("Achievement", arrayList.get(i).getAchievement() + "");

            if (j != arrayList.get(i).getDay()) {
                for (int k = j; k < arrayList.get(i).getDay(); k++) {
                    barEntriesArrayList.add(new BarEntry(j, 0));
                    //Log.d("Enter", arrayList.get(i).getAchievement() + "");
                    j++;
                }
            }

            barEntriesArrayList.add(new BarEntry(j, arrayList.get(i).getAchievement()));
            total += arrayList.get(i).getAchievement();
            //Log.d("Achieve", arrayList.get(i).getAchievement() + "");
            j++;
        }

        for (int i=j;i<=size;i++){
            barEntriesArrayList.add(new BarEntry(i,0));
        }

        if(arrayList.size()>0) {
            // creating a new bar data set.
            barDataSet = new BarDataSet(barEntriesArrayList,null);

            // creating a new bar data and
            // passing our bar data set.

            // below line is to set data
            // to our bar chart.
            barData = new BarData(barDataSet);
            MonthReportFragmentBinding.graph.setData(barData);
            MonthReportFragmentBinding.graph.setScaleEnabled(true);
            MonthReportFragmentBinding.graph.animateY(1500);
            MonthReportFragmentBinding.graph.setPinchZoom(false);
            MonthReportFragmentBinding.graph.setScaleEnabled(false);

            // adding color to our bar data set.
            barDataSet.setColors(R.color.water_color);

            // setting text color.
            barDataSet.setValueTextColor(Color.BLACK);
            MonthReportFragmentBinding.graph.notifyDataSetChanged();
            MonthReportFragmentBinding.graph.invalidate();
        }
        else
        {
            MonthReportFragmentBinding.graph.clear();
        }

        MonthReportFragmentBinding.completedMl.setText(total/size+"ml");
        int temp = ((int) (  ( (float) (int)(total/size)) / (float) (target_ml) * 100));
        MonthReportFragmentBinding.avgProgressBar.setProgress(temp);
        if (temp<99)
            MonthReportFragmentBinding.avgTextviewProgress.setText(temp+"%");
        else
            MonthReportFragmentBinding.avgTextviewProgress.setText(100+"%");
        total = 0;//important

        //progressbar();
    }

/*
    private void progressbar() {

        MonthReportFragmentBinding.w1.setProgress((Integer) barEntriesArrayList.get(1));
        int temp = ((int) (  ( (float) (int)(barEntriesArrayList.get(1))) / (float) (target_ml) * 100));
        MonthReportFragmentBinding.nameW1.setText(temp+"%");
    }*/

    private void setId(){

        r = getResources();
        packageName = getActivity().getPackageName();

        for (int i=0;i<31;i++){
            progressbar[i] = r.getIdentifier("R.id.w"+i+1,"id",packageName);
        }

        Log.d("abc",String.valueOf(progressbar[1]));
        MonthReportFragmentBinding.w1.setProgress(50);
        MonthReportFragmentBinding.nameW1.setText(50+"%");
    }

    private void getTodayRecord()
    {
        Calendar calendar = Calendar.getInstance();
        ArrayList<DataModel> todayRecord = new ArrayList<>();
        todayRecord = dbHandler.readDataDateWise(calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.YEAR));

        if (todayRecord.size()!=0)
        {
            MonthReportFragmentBinding.completedMlToday.setText(todayRecord.get(0).getAchievement()+"ml");
            int temp = ((int) (  ( (float) (int)(todayRecord.get(0).getAchievement())) / (float) (target_ml) * 100));
            MonthReportFragmentBinding.progressBarToday.setProgress(temp);
            if (temp<99)
                MonthReportFragmentBinding.textviewProgressToday.setText(temp+"%");
            else
                MonthReportFragmentBinding.textviewProgressToday.setText(100+"%");
        }
        else
        {
            MonthReportFragmentBinding.completedMlToday.setText("0 ml");
            MonthReportFragmentBinding.progressBarToday.setProgress(0);
            MonthReportFragmentBinding.textviewProgressToday.setText("0%");
        }
    }
}