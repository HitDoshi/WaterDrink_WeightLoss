package com.example.waterdrink_weightloss.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.waterdrink_weightloss.Database.DBHandler;
import com.example.waterdrink_weightloss.Database.DataModel;
import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.adapter.CalendarAdapter;
import com.example.waterdrink_weightloss.databinding.FragmentWeekGraphBinding;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekGraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekGraphFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    FragmentWeekGraphBinding fragmentWeekGraphBinding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DBHandler dbHandler;
    int target_ml;
    int day1,month1,year1,day7,month7,year7,sum=0;
    SharedPreferences sharedPreferences;
    ArrayList<DataModel> arrayList = new ArrayList<>();
    Calendar c1;
    int j=1 , total=0;
    ArrayList barEntriesArrayList;
    final ArrayList<String> xAxisLabel = new ArrayList<>();
    ArrayList<Integer> setWeekData =  new ArrayList<Integer>();

    // variable for our bar data.
    BarData barData;

    // variable for our bar data set.
    BarDataSet barDataSet;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeekGraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeekGraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeekGraphFragment newInstance(String param1, String param2) {
        WeekGraphFragment fragment = new WeekGraphFragment();
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
        c1 = Calendar.getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sharedPreferences = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        xAxisLabel.add("Sun");
        xAxisLabel.add("Mon");
        xAxisLabel.add("Tue");
        xAxisLabel.add("Wed");
        xAxisLabel.add("Thu");
        xAxisLabel.add("Fri");
        xAxisLabel.add("Sat");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentWeekGraphBinding = FragmentWeekGraphBinding.inflate(getLayoutInflater());

        return fragmentWeekGraphBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean theme = sharedPreferences.getBoolean("Theme",false);
        if(theme){
            fragmentWeekGraphBinding.leftArrow.setColorFilter(Color.WHITE);
            fragmentWeekGraphBinding.rightArrow.setColorFilter(Color.WHITE);
        }


        fragmentWeekGraphBinding.graph.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));

        fragmentWeekGraphBinding.month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment1 =new MonthGraphFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,fragment1);
                fragmentTransaction.commit();
            }
        });

        fragmentWeekGraphBinding.year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment1 =new YearGraphFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,fragment1);
                fragmentTransaction.commit();
            }
        });

        fragmentWeekGraphBinding.leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c1.add(c1.DATE,-7);
                //MMMM - Full Month Name
                SimpleDateFormat FirstDay = new SimpleDateFormat("dd MMM yyyy");//MMM = Short Month Name
                SimpleDateFormat LastDay = new SimpleDateFormat("dd MMM yyyy");

                c1.set(Calendar.DAY_OF_WEEK, 1);
                year1 = c1.get(Calendar.YEAR);
                month1 = c1.get(Calendar.MONTH);
                day1 = c1.get(Calendar.DAY_OF_MONTH);

                String first = FirstDay.format(c1.getTime());
                //Log.d("day1",day1+"");
                //last day of week
                c1.set(Calendar.DAY_OF_WEEK, 7);

                year7 = c1.get(Calendar.YEAR);
                month7 = c1.get(Calendar.MONTH);
                day7 = c1.get(Calendar.DAY_OF_MONTH);
                String last = LastDay.format(c1.getTime());

                //Log.d("day7",day7+"");

                fragmentWeekGraphBinding.selectedWeek.setText(first+" To "+last);
                setData();
            }
        });

        fragmentWeekGraphBinding.rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c1.add(c1.DATE,7);
                //MMMM - Full Month Name
                SimpleDateFormat FirstDay = new SimpleDateFormat("dd MMM yyyy");//MMM = Short Month Name
                SimpleDateFormat LastDay = new SimpleDateFormat("dd MMM yyyy");

                c1.set(Calendar.DAY_OF_WEEK, 1);
                year1 = c1.get(Calendar.YEAR);
                month1 = c1.get(Calendar.MONTH);
                day1 = c1.get(Calendar.DAY_OF_MONTH);

                String first = FirstDay.format(c1.getTime());
                //Log.d("day1",day1+"");
                //last day of week
                c1.set(Calendar.DAY_OF_WEEK, 7);

                year7 = c1.get(Calendar.YEAR);
                month7 = c1.get(Calendar.MONTH);
                day7 = c1.get(Calendar.DAY_OF_MONTH);
                String last = LastDay.format(c1.getTime());

                //Log.d("day7",day7+"");

                fragmentWeekGraphBinding.selectedWeek.setText(first+" To "+last);
                setData();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        target_ml = sharedPreferences.getInt("target_ml", 1500);

        SimpleDateFormat FirstDay = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat LastDay = new SimpleDateFormat("dd MMM yyyy");

        //first day of week
        c1.set(Calendar.DAY_OF_WEEK, 1);

        year1 = c1.get(Calendar.YEAR);
        month1 = c1.get(Calendar.MONTH);
        day1 = c1.get(Calendar.DAY_OF_MONTH);
        String first = FirstDay.format(c1.getTime());
        Log.d("day1",day1+"");
        //last day of week
        c1.set(Calendar.DAY_OF_WEEK, 7);

        year7 = c1.get(Calendar.YEAR);
        month7 = c1.get(Calendar.MONTH);
        day7 = c1.get(Calendar.DAY_OF_MONTH);
        String last = LastDay.format(c1.getTime());

        Log.d("day7",day7+"");

        fragmentWeekGraphBinding.selectedWeek.setText(first+" To "+last);

        /*c1.add(Calendar.DATE, -14);
        int a = c1.get(Calendar.DAY_OF_MONTH);
        Log.d("Remove", a+ "");*/

        getTodayRecord();
        setData();
    }

    private void setData() {

        int size=7;
        Log.d("DaysOfMonth",size+"");
        j=1;
        arrayList.clear();
        arrayList = dbHandler.readDataWeekWise(day1,month1+1,year1,day7,month7+1,year7);
        Log.d("size",arrayList.size()+"");
        Collections.sort(arrayList, new Comparator<DataModel>() {
            @Override
            public int compare(DataModel dataModel, DataModel t1) {
                return Integer.compare(dataModel.getDay(), t1.getDay());
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            arrayList.stream().sorted(Comparator.comparing((DataModel a) -> a.getMonth()).
                    thenComparing((DataModel a) -> a.getDay())).collect(Collectors.toList());
        }

        for (int i=0;i<arrayList.size();i++)
        {
            Log.d("Month",arrayList.get(i).getDay()+"");
        }
        // creating a new array list
        barEntriesArrayList = new ArrayList<>();
        setWeekData.clear();

        c1.set(Calendar.DAY_OF_WEEK,1);

        for(int i=0;i<7;i++)
        {
            c1.set(Calendar.DAY_OF_WEEK,i+1);

            for (int j=0;j<arrayList.size();j++) {
                if (c1.get(Calendar.DAY_OF_MONTH)==arrayList.get(j).getDay()){
                    total += arrayList.get(j).getAchievement();
                    sum = arrayList.get(j).getAchievement();
                }
            }

            barEntriesArrayList.add(new BarEntry(i,sum));
            setWeekData.add(i,sum);
            sum = 0;
        }

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
    /*    barEntriesArrayList.add(new BarEntry(1f, 4));
        barEntriesArrayList.add(new BarEntry(2f, 6));
        barEntriesArrayList.add(new BarEntry(3f, 8));
        barEntriesArrayList.add(new BarEntry(4f, 2));*/
/*
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
        }*/

        if(arrayList.size()>0) {
            // creating a new bar data set.
            barDataSet = new BarDataSet(barEntriesArrayList, null);

            // creating a new bar data and
            // passing our bar data set.

            // below line is to set data
            // to our bar chart.
            barData = new BarData(barDataSet);
            fragmentWeekGraphBinding.graph.setData(barData);
            fragmentWeekGraphBinding.graph.setScaleEnabled(true);
            fragmentWeekGraphBinding.graph.animateY(1500);
            fragmentWeekGraphBinding.graph.setPinchZoom(false);
            fragmentWeekGraphBinding.graph.setScaleEnabled(false);
            fragmentWeekGraphBinding.graph.getAxisLeft().setStartAtZero(true);
            fragmentWeekGraphBinding.graph.getDescription().setEnabled(false);
            fragmentWeekGraphBinding.graph.getLegend().setEnabled(false);//small color cube hide

            if(sharedPreferences.getBoolean("Theme",false)){
                fragmentWeekGraphBinding.graph.getXAxis().setTextColor(Color.WHITE);
                fragmentWeekGraphBinding.graph.getAxisLeft().setTextColor(Color.WHITE);
                // setting text color.
                barDataSet.setValueTextColor(Color.WHITE);
            }

            barDataSet.setColors(Color.parseColor("#14BFF5"));//water color
            fragmentWeekGraphBinding.graph.getAxisRight().setEnabled(false);

            // adding color to our bar data set.

            fragmentWeekGraphBinding.graph.notifyDataSetChanged();
            fragmentWeekGraphBinding.graph.invalidate();
        }
        else
        {
            fragmentWeekGraphBinding.graph.clear();
        }

        fragmentWeekGraphBinding.completedMl.setText(total/size+"ml");
        int temp = ((int) (  ( (float) (int)(total/size)) / (float) (target_ml) * 100));
        fragmentWeekGraphBinding.avgProgressBar.setProgress(temp);
        if (temp<99)
            fragmentWeekGraphBinding.avgTextviewProgress.setText(temp+"%");
        else
            fragmentWeekGraphBinding.avgTextviewProgress.setText(100+"%");
        total = 0;//important

        //progressbar();
        setWeeklyData();
    }

    private void getTodayRecord()
    {
        Calendar calendar = Calendar.getInstance();
        ArrayList<DataModel> todayRecord = new ArrayList<>();
        todayRecord = dbHandler.readDataDateWise(calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.YEAR));

        if (todayRecord.size()!=0)
        {
            fragmentWeekGraphBinding.completedMlToday.setText(todayRecord.get(0).getAchievement()+"ml");
            int temp = ((int) (  ( (float) (int)(todayRecord.get(0).getAchievement())) / (float) (target_ml) * 100));
            fragmentWeekGraphBinding.progressBarToday.setProgress(temp);
            if (temp<99)
                fragmentWeekGraphBinding.textviewProgressToday.setText(temp+"%");
            else
                fragmentWeekGraphBinding.textviewProgressToday.setText(100+"%");
        }
        else
        {
            fragmentWeekGraphBinding.completedMlToday.setText("0 ml");
            fragmentWeekGraphBinding.progressBarToday.setProgress(0);
            fragmentWeekGraphBinding.textviewProgressToday.setText("0%");
        }
    }

    private void setWeeklyData(){
        CalendarAdapter calendarAdapter = new CalendarAdapter( getActivity(), setWeekData,xAxisLabel,null);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 7);
        fragmentWeekGraphBinding.calendarRecyclerView.setLayoutManager(layoutManager);
        fragmentWeekGraphBinding.calendarRecyclerView.setAdapter(calendarAdapter);
    }

}