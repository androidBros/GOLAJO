package com.example.lucky;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_random_menu, container, false);
        Button start = rootView.findViewById(R.id.start_btn);
        Button stop = rootView.findViewById(R.id.stop_btn);
        final ViewFlipper viewFlipper;
        TextView select_menu = rootView.findViewById(R.id.select_menu);

        viewFlipper = (ViewFlipper)rootView.findViewById(R.id.viewFlipper1);
        viewFlipper.setFlipInterval(100);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.startFlipping();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.stopFlipping();
                view_num = viewFlipper.getDisplayedChild();
                String str_view_num = Integer.toString(view_num);
                menu = "menu"+str_view_num;
                resId = getResources().getIdentifier(menu,"string", getActivity().getPackageName());
                select_menu.setText(resId);

            }
        });

        return rootView;
    }

}