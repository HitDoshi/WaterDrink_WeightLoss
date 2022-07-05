package com.example.waterdrink_weightloss.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.waterdrink_weightloss.Database.DBHandler;
import com.example.waterdrink_weightloss.Database.DataModel;
import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.databinding.FragmentDrinkReportBinding;
import com.example.waterdrink_weightloss.databinding.FragmentMonthGraphBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

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
    String[] month = {"January","February","March","April","May","Jun","July","August","September",
            "October","November","December"};
    ArrayList<String> days = new ArrayList<String>();
    ArrayList<DataModel> arrayList = new ArrayList<>();
    int position , j=1 , size;
    DBHandler dbHandler;
    ArrayList barEntriesArrayList;
    BarChart barChart;
    Calendar calendar;

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

        return MonthReportFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MonthReportFragmentBinding.leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position--;
                MonthReportFragmentBinding.selectedMonth.setText(month[position]);
                if(position<11)
                {
                    MonthReportFragmentBinding.rightArrow.setEnabled(true);
                }
                if (position<1)
                {
                    MonthReportFragmentBinding.leftArrow.setEnabled(false);
                }
                setData(position);
            }
        });

        MonthReportFragmentBinding.rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position++;
                MonthReportFragmentBinding.selectedMonth.setText(month[position]);

                if(position>10)
                {
                    MonthReportFragmentBinding.rightArrow.setEnabled(false);
                }
                if (position>0)
                {
                        MonthReportFragmentBinding.leftArrow.setEnabled(true);
                }
                setData(position);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        Calendar calendar = Calendar.getInstance();
        position = calendar.get(Calendar.MONTH)+1;
        MonthReportFragmentBinding.selectedMonth.setText(month[position]);
        size = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        setData(position);
    }

    private void setData(int position) {

        days.clear();
        for(int i=0;i<size;i++){
            days.add(String.valueOf(i+1));
        }

        Log.d("DaysOfMonth",size+"");
        j=1;
        arrayList.clear();
        arrayList = dbHandler.readDataMonthWise(position);
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
            //Log.d("Achieve", arrayList.get(i).getAchievement() + "");
            j++;
        }

        for (int i=j;i<size;i++){
            barEntriesArrayList.add(new BarEntry(i,0));
        }

        if(arrayList.size()>0) {
            // creating a new bar data set.
            barDataSet = new BarDataSet(barEntriesArrayList, "Geeks for Geeks");

            // creating a new bar data and
            // passing our bar data set.

            // below line is to set data
            // to our bar chart.
            XAxis xAxis = MonthReportFragmentBinding.graph.getXAxis();

            xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
            xAxis.setPosition(XAxis.XAxisPosition.TOP);
            xAxis.setGranularity(1);
            xAxis.setCenterAxisLabels(true);
            xAxis.setGranularityEnabled(true);

            barData = new BarData(barDataSet);
            MonthReportFragmentBinding.graph.setData(barData);
            MonthReportFragmentBinding.graph.setScaleEnabled(true);
            MonthReportFragmentBinding.graph.animateY(1500);
            MonthReportFragmentBinding.graph.setPinchZoom(false);
            MonthReportFragmentBinding.graph.setScaleEnabled(false);

            // adding color to our bar data set.
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

            // setting text color.
            barDataSet.setValueTextColor(Color.BLACK);
            MonthReportFragmentBinding.graph.notifyDataSetChanged();
            MonthReportFragmentBinding.graph.invalidate();
        }
        else
        {
            MonthReportFragmentBinding.graph.clear();
        }
    }
}