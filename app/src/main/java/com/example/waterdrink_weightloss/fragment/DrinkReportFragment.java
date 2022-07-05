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

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.databinding.FragmentDrinkReportBinding;
import com.example.waterdrink_weightloss.databinding.FragmentHomeBinding;

public class DrinkReportFragment extends Fragment  {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Fragment fragment1;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FragmentDrinkReportBinding ReportFragmentBinding;

    private String mParam1;
    private String mParam2;

    public DrinkReportFragment() {
        // Required empty public constructor
    }

    public static DrinkReportFragment newInstance(String param1, String param2) {
        DrinkReportFragment fragment = new DrinkReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
       /* fragment1  =new WeekGraphFragment();
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_drink_report, container, false);

        ReportFragmentBinding = FragmentDrinkReportBinding.inflate(getLayoutInflater());

       /* fragmentTransaction.replace(R.id.main_fragment,fragment1);
        fragmentTransaction.commit();

        ReportFragmentBinding.week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment1 =new WeekGraphFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.change_graph_fragment,fragment1);
                fragmentTransaction.commit();
            }
        });

        ReportFragmentBinding.month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment1 =new MonthGraphFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.change_graph_fragment,fragment1);
                fragmentTransaction.commit();
            }
        });
*/
        return ReportFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}