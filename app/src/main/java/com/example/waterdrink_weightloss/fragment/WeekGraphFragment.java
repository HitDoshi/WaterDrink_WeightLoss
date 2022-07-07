package com.example.waterdrink_weightloss.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.waterdrink_weightloss.Database.DBHandler;
import com.example.waterdrink_weightloss.Database.DataModel;
import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.databinding.FragmentMonthGraphBinding;
import com.example.waterdrink_weightloss.databinding.FragmentWeekGraphBinding;

import java.util.ArrayList;
import java.util.Calendar;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
    }

    @Override
    public void onStart() {
        super.onStart();
        getTodayRecord();
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
            int temp = ((int) (  ( (float) (int)(todayRecord.get(0).getAchievement())) / (float) (500) * 100));
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
}