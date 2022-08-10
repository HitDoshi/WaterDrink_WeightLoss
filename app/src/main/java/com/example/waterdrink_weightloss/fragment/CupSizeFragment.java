package com.example.waterdrink_weightloss.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.PluralsRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.activity.Model.PrefKey;
import com.example.waterdrink_weightloss.databinding.FragmentCupSizeBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CupSizeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CupSizeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentCupSizeBinding fragmentCupSizeBinding;
    SharedPreferences sharedPreferences;
    int cup_size = 300 ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CupSizeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CupSizeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CupSizeFragment newInstance(String param1, String param2) {
        CupSizeFragment fragment = new CupSizeFragment();
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
        sharedPreferences = getActivity().getSharedPreferences(PrefKey.SharePrefName, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentCupSizeBinding = FragmentCupSizeBinding.inflate(getLayoutInflater());

        return fragmentCupSizeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentCupSizeBinding.cup100ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cup_size = 100;
                fragmentCupSizeBinding.customCupsize.setText(cup_size+"");
            }
        });

        fragmentCupSizeBinding.cup200ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cup_size = 200;
                fragmentCupSizeBinding.customCupsize.setText(cup_size+"");
            }
        });

        fragmentCupSizeBinding.cup300ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cup_size = 300;
                fragmentCupSizeBinding.customCupsize.setText(cup_size+"");
            }
        });

        fragmentCupSizeBinding.cup500ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cup_size = 500;
                fragmentCupSizeBinding.customCupsize.setText(cup_size+"");
            }
        });

        fragmentCupSizeBinding.customCup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                cup_size = 150;
//                fragmentCupSizeBinding.customCupsize.setText(cup_size+"");
            }
        });

        fragmentCupSizeBinding.cup150ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cup_size = 150 ;
                fragmentCupSizeBinding.customCupsize.setText(cup_size+"");
            }
        });

        fragmentCupSizeBinding.customCupsize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!fragmentCupSizeBinding.customCupsize.getText().toString().equals("")) {
                    cup_size = Integer.parseInt(fragmentCupSizeBinding.customCupsize.getText().toString());
                } else {
                    cup_size = 300;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        fragmentCupSizeBinding.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fragmentCupSizeBinding.customCupsize.getText() != null){
                    sharedPreferences.edit().putInt(PrefKey.CupSize,cup_size).apply();
                }
                else
                {
                    sharedPreferences.edit().putInt(PrefKey.CupSize,300).apply();
                }
                Fragment fragment1 =new HomeFragment();
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
        fragmentCupSizeBinding.customCupsize.setText(sharedPreferences.getInt(PrefKey.CupSize,300)+"");
    }
}