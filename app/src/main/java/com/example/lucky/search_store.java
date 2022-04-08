package com.example.lucky;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

public class search_store extends Fragment {
    TextView textView;
    double latitude;
    double longtitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search_store, container, false);
        MainActivity activity = (MainActivity) getActivity();
        //activity.startLocationService();

        Bundle bundle = getArguments();

        Log.d("My", "여기까지 됨");
        if (bundle != null) {
            latitude = bundle.getDouble("LATITUDE");
            longtitude = bundle.getDouble("LONGTITUDE");
        }
        Log.d("MM","위경"+latitude+longtitude);
        textView = rootView.findViewById(R.id.textview1);
        textView.setText("위도 : "+latitude + "\n경도 : " + longtitude);

        // Inflate the layout for this fragment
        return rootView;
    }

}
