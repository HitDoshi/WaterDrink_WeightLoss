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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.waterdrink_weightloss.Database.DBHandler;
import com.example.waterdrink_weightloss.Database.DataModel;
import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.adapter.CalendarAdapter;
import com.example.waterdrink_weightloss.databinding.FragmentMonthGraphBinding;
import com.example.waterdrink_weightloss.databinding.FragmentYearGraphBinding;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YearGraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YearGraphFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentYearGraphBinding fragmentYearGraphBinding;
    Calendar calendar;
    Resources r ;
    int target_ml;
    SharedPreferences sharedPreferences;
    String packageName ;
    ArrayList barEntriesArrayList;
    ArrayList<String> months = new ArrayList<String>();
    ArrayList<DataModel> arrayList = new ArrayList<>();
    final ArrayList<String> xAxisLabel = new ArrayList<>();
    ArrayList<Integer> setYearData = new ArrayList<>();

    // variable for our bar data.
    BarData barData;

    // variable for our bar data set.
    BarDataSet barDataSet;

    int position , j=1 , size , year , total = 0 , sum = 0;
    DBHandler dbHandler;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public YearGraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YearGraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YearGraphFragment newInstance(String param1, String param2) {
        YearGraphFragment fragment = new YearGraphFragment();
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
        xAxisLabel.add("Jan");
        xAxisLabel.add("Feb");
        xAxisLabel.add("Mar");
        xAxisLabel.add("Apr");
        xAxisLabel.add("May");
        xAxisLabel.add("Jun");
        xAxisLabel.add("Jul");
        xAxisLabel.add("Aug");
        xAxisLabel.add("Sep");
        xAxisLabel.add("Oct");
        xAxisLabel.add("Nov");
        xAxisLabel.add("Dec");
        if (getArguments() != null) {
        mParam1 = getArguments().getString(ARG_PARAM1);
        mParam2 = getArguments().getString(ARG_PARAM2);
    }
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fragmentYearGraphBinding = FragmentYearGraphBinding.inflate(getLayoutInflater());

        fragmentYearGraphBinding.week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment1 =new WeekGraphFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,fragment1);
                fragmentTransaction.commit();
            }
        });

        fragmentYearGraphBinding.month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment1 =new MonthGraphFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,fragment1);
                fragmentTransaction.commit();
            }
        });

        return fragmentYearGraphBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentYearGraphBinding.graph.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));

        fragmentYearGraphBinding.leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year--;
                fragmentYearGraphBinding.selectedYear.setText(year+"");
                setData(year);
            }
        });

        fragmentYearGraphBinding.rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year++;
                fragmentYearGraphBinding.selectedYear.setText(year+"");
                setData(year);
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
        fragmentYearGraphBinding.selectedYear.setText(year+"");
        calendar = new GregorianCalendar(year, position, 1);
        size = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        setData(year);
        getTodayRecord();

        //setId();
    }

    private void setData(int year) {

        months.clear();
        for(int i=0;i<size;i++){
            months.add(String.valueOf(i+1));
        }

        Log.d("DaysOfMonth",size+"");
        j=1;
        arrayList.clear();
        arrayList = dbHandler.readDataYearWise(year);
        Log.d("size",arrayList.size()+"");
        Collections.sort(arrayList, new Comparator<DataModel>() {
            @Override
            public int compare(DataModel dataModel, DataModel t1) {
                return Integer.compare(dataModel.getMonth(), t1.getMonth());
            }
        });

        /*for (int i=0;i<arrayList.size();i++){
            Log.d("a",arrayList.get(i).getDay()+"");
        }*/

        // creating a new array list
        barEntriesArrayList = new ArrayList<>();
        setYearData.clear(); //clear list otherwise added more data at last

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
    /*    barEntriesArrayList.add(new BarEntry(1f, 4));
        barEntriesArrayList.add(new BarEntry(2f, 6));
        barEntriesArrayList.add(new BarEntry(3f, 8));
        barEntriesArrayList.add(new BarEntry(4f, 2));*/

        for(int i=0;i<12;i++)
        {
            for(int j=0;j<arrayList.size();j++){
                if(arrayList.get(j).getMonth()==i+1){
                    total += arrayList.get(j).getAchievement();
                    sum += arrayList.get(j).getAchievement();
                }
            }

            if(total!=0) {
                Calendar c = Calendar.getInstance();
                c = new GregorianCalendar(year, i + 1, 1);
                int h = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                sum = sum / h;
            }

            barEntriesArrayList.add(new BarEntry(i,total));
            setYearData.add(i,total);
            total = 0;
        }

       /* for (int i=0;i<arrayList.size();i++) {
            //Log.d("Achievement", arrayList.get(i).getAchievement() + "");

            if (j != arrayList.get(i).getDay()) {
                for (int k = j; k < arrayList.get(i).getDay(); k++) {
                    barEntriesArrayList.add(new BarEntry(j, 50));
                    //Log.d("Enter", arrayList.get(i).getAchievement() + "");
                    j++;
                }
            }

            barEntriesArrayList.add(new BarEntry(j, arrayList.get(i).getAchievement()));
            total += arrayList.get(i).getAchievement();
            //Log.d("Achieve", arrayList.get(i).getAchievement() + "");
            j++;
        }*/
/*

        for (int i=j;i<size;i++){
            barEntriesArrayList.add(new BarEntry(i,0));
        }
*/

        if(arrayList.size()>0) {
            // creating a new bar data set.
            barDataSet = new BarDataSet(barEntriesArrayList,null);

            // creating a new bar data and
            // passing our bar data set.

            // below line is to set data
            // to our bar chart.
            barData = new BarData(barDataSet);
            fragmentYearGraphBinding.graph.setData(barData);
            fragmentYearGraphBinding.graph.setScaleEnabled(true);
            fragmentYearGraphBinding.graph.animateY(1500);
            fragmentYearGraphBinding.graph.setPinchZoom(false);
            fragmentYearGraphBinding.graph.setScaleEnabled(false);
            fragmentYearGraphBinding.graph.getAxisRight().setEnabled(false);

            // adding color to our bar data set.
            barDataSet.setColors(R.color.water_color);

            // setting text color.
            barDataSet.setValueTextColor(Color.BLACK);
            fragmentYearGraphBinding.graph.notifyDataSetChanged();
            fragmentYearGraphBinding.graph.invalidate();
        }
        else
        {
            fragmentYearGraphBinding.graph.clear();
        }

        fragmentYearGraphBinding.completedMl.setText(sum/12+"ml");
        int temp = ((int) (  ( (float) (int)(sum/12)) / (float) (target_ml) * 100));
        fragmentYearGraphBinding.avgProgressBar.setProgress(temp);
        Log.d("Temp1",temp+"");
        if (temp<99)
            fragmentYearGraphBinding.avgTextviewProgress.setText(temp+"%");
        else
            fragmentYearGraphBinding.avgTextviewProgress.setText(100+"%");
        sum=0;//important

        setMYearlyData();
        //progressbar();
    }

/*
    private void progressbar() {

        MonthReportFragmentBinding.w1.setProgress((Integer) barEntriesArrayList.get(1));
        int temp = ((int) (  ( (float) (int)(barEntriesArrayList.get(1))) / (float) (target_ml) * 100));
        MonthReportFragmentBinding.nameW1.setText(temp+"%");
    }*/

    private void setId(){
/*
        r = getResources();
        packageName = getActivity().getPackageName();

        for (int i=0;i<31;i++){
            progressbar[i] = r.getIdentifier("R.id.w"+i+1,"id",packageName);
        }

        Log.d("abc",String.valueOf(progressbar[1]));
        MonthReportFragmentBinding.w1.setProgress(50);
        MonthReportFragmentBinding.nameW1.setText(50+"%");*/
    }

    private void getTodayRecord()
    {
        Calendar calendar = Calendar.getInstance();
        ArrayList<DataModel> todayRecord = new ArrayList<>();
        todayRecord = dbHandler.readDataDateWise(calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.YEAR));

        if (todayRecord.size()!=0)
        {
            fragmentYearGraphBinding.completedMlToday.setText(todayRecord.get(0).getAchievement()+"ml");
            int temp = ((int) (  ( (float) (int)(todayRecord.get(0).getAchievement())) / (float) (target_ml) * 100));
            Log.d("Temp2",temp+"");
            fragmentYearGraphBinding.progressBarToday.setProgress(temp);
            if (temp<99)
                fragmentYearGraphBinding.textviewProgressToday.setText(temp+"%");
            else
                fragmentYearGraphBinding.textviewProgressToday.setText(100+"%");
        }
        else
        {
            fragmentYearGraphBinding.completedMlToday.setText("0 ml");
            fragmentYearGraphBinding.progressBarToday.setProgress(0);
            fragmentYearGraphBinding.textviewProgressToday.setText("0%");
        }
    }

    private void setMYearlyData(){
        CalendarAdapter calendarAdapter = new CalendarAdapter( getActivity(), setYearData,xAxisLabel,year);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 6);
        fragmentYearGraphBinding.calendarRecyclerView.setLayoutManager(layoutManager);
        fragmentYearGraphBinding.calendarRecyclerView.setAdapter(calendarAdapter);
    }
}