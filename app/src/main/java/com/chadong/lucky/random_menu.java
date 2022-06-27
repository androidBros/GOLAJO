package com.chadong.lucky;


import android.content.Intent;
import android.net.Uri;
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
    String url_string=null;

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
        viewFlipper.setFlipInterval(50);

        start.setOnClickListener(new View.OnClickListener() { // viewflipper 시작
            @Override
            public void onClick(View view) {
                Log.d("random_menu","음식 고르기 시작");
                viewFlipper.startFlipping();
                menu_res = null;
                stop.setEnabled(true);
                start.setEnabled(false);


                Log.d("mainActivity","위치서비스 메소드 실행 - startLocationService() ");
            }

        });

        stop.setOnClickListener(new View.OnClickListener() { // viewflipper 정지
            @Override
            public void onClick(View view) {
                Log.d("random_menu","음식 고르기 정지");
                viewFlipper.stopFlipping();
                view_num = viewFlipper.getDisplayedChild();
                String str_view_num = Integer.toString(view_num);
                menu = "menu"+str_view_num;
                resId = getResources().getIdentifier(menu,"string", getActivity().getPackageName());
                select_menu.setText(resId);
                menu_res = getResources().getString(resId); //Integer.toString(resId).split(" ")[0];
                menu_res = menu_res.split(" ")[0]; // 선택한 음식 이름
                start.setEnabled(true);
                stop.setEnabled(false);

                Bundle bundle = getArguments();
                if(bundle != null){
                    url_string = bundle.getString("address");
                    url_string = url_string +menu_res;
                    Log.d("random_menu",url_string);
                }


            }
        });

        // 선택한 음식 webview에서 검색
        search_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("random_menu","음식점 찾기 버튼 클릭");

                MainActivity activity = (MainActivity) getActivity();
                if (menu_res == null) {
                    Toast.makeText(activity,"메뉴를 골라주세용",Toast.LENGTH_SHORT).show();
                }


                if(url_string!=null){
                    Log.d("random_menu","음식점 엑티비티 ON");
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_string));
                    startActivity(myIntent);
                }
                else{
                    Log.d("random_menu","음식점 엑티비티 faild");
                }


            }
        });


        return rootView;
    }


}