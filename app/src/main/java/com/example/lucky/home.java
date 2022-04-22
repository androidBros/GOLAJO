package com.example.lucky;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;

//
public class home extends Fragment {
    ImageButton button;
    GpsTracker gpsTracker;

    double latitude; // 위도
    double longitude; // 경도

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        button = rootView.findViewById(R.id.button); // home -> random_menu 이동
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();

                gpsTracker = new GpsTracker(getActivity());
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
                activity.reverseCoding(latitude, longitude);
                Log.e("test", "home에서 reversecoding 함수 호출");

                activity.onFragmentChanged(1);
            }
        });



        return rootView;
    }
}