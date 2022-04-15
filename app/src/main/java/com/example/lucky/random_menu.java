package com.example.lucky;


import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class random_menu extends Fragment {

    int view_num;
    String menu = "";
    int resId;
    String menu_res;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_random_menu, container, false);
        Button start = rootView.findViewById(R.id.start_btn);
        Button stop = rootView.findViewById(R.id.stop_btn);
        Button search_store = rootView.findViewById(R.id.search_store);
        TextView select_menu = rootView.findViewById(R.id.select_menu);

        final ViewFlipper viewFlipper; // 음식 사진 전환
        viewFlipper = (ViewFlipper)rootView.findViewById(R.id.viewFlipper1);
        viewFlipper.setFlipInterval(100);

        start.setOnClickListener(new View.OnClickListener() { // viewflipper 시작
            @Override
            public void onClick(View view) {
                viewFlipper.startFlipping();
                menu_res = null;
                stop.setEnabled(true);
                start.setEnabled(false);
            }

        });

        stop.setOnClickListener(new View.OnClickListener() { // viewflipper 정지
            @Override
            public void onClick(View view) {
                viewFlipper.stopFlipping();
                view_num = viewFlipper.getDisplayedChild();
                String str_view_num = Integer.toString(view_num);
                menu = "menu"+str_view_num;
                resId = getResources().getIdentifier(menu,"string", getActivity().getPackageName());
                select_menu.setText(resId);
                menu_res = getResources().getString(resId); //Integer.toString(resId).split(" ")[0];
                menu_res = menu_res.split(" ")[0];
                start.setEnabled(true);
                stop.setEnabled(false);

            }
        });

        // 선택한 음식 webview에서 검색
        search_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                if (menu_res != null) {
                    activity.startLocationService();
                }else{
                    Toast.makeText(activity,"메뉴를 골라주세용",Toast.LENGTH_SHORT).show();
                }
            }
        });


        return rootView;
    }


}