package com.example.waterdrink_weightloss.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.activity.PrefManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class TimeFragment extends Fragment {

    NumberPicker wake_up_hour , wake_up_min ;
    NumberPicker bed_hour , bed_min ;
    Button set;
    SharedPreferences userDataSharedPreferences;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TimeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeFragment newInstance(String param1, String param2) {
        TimeFragment fragment = new TimeFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_time, container, false);
        wake_up_hour = view.findViewById(R.id.wake_up_hour);
        wake_up_min = view.findViewById(R.id.wake_up_min);

        bed_hour = view.findViewById(R.id.bed_hour);
        bed_min = view.findViewById(R.id.bed_min);

        set = view.findViewById(R.id.set);
        userDataSharedPreferences = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        int h1,m1,h2,m2;
        h1 = userDataSharedPreferences.getInt("wake_up_hour",7);
        m1 = userDataSharedPreferences.getInt("wake_up_min",0);

        h2 = userDataSharedPreferences.getInt("bed_hour",22);
        m2 = userDataSharedPreferences.getInt("bed_min",0);

        wake_up_hour.setMaxValue(23);
        wake_up_hour.setMinValue(0);
        wake_up_min.setMinValue(0);
        wake_up_min.setMaxValue(59);
        wake_up_hour.setValue(h1);
        wake_up_min.setValue(m1);

        bed_hour.setMaxValue(23);
        bed_hour.setMinValue(0);
        bed_min.setMinValue(0);
        bed_min.setMaxValue(59);
        bed_hour.setValue(h2);
        bed_min.setValue(m2);

        wake_up_hour.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        wake_up_min.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        bed_hour.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        bed_min.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDataSharedPreferences.edit().putInt("wake_up_hour",wake_up_hour.getValue()).apply();
                userDataSharedPreferences.edit().putInt("wake_up_min",wake_up_min.getValue()).apply();
                userDataSharedPreferences.edit().putInt("bed_hour",bed_hour.getValue()).apply();
                userDataSharedPreferences.edit().putInt("bed_min",bed_min.getValue()).apply();

                PrefManager prefManager = new PrefManager(getActivity());
                prefManager.setFirstTimeLaunch(false);
                Intent intent = new Intent(getContext(), LoadingActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}