package com.example.lucky;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageButton option_btn;

    home home;
    random_menu random_menu;
    search_store search_store;
    double latitude;
    double longtitude;

    Geocoder geocoder;
    String result_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        geocoder = new Geocoder(this);

        option_btn = findViewById(R.id.option_btn);
        option_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup= new PopupMenu(getApplicationContext(), view);//v는 클릭된 뷰를 의미
                getMenuInflater().inflate(R.menu.option_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.m1:
                                Toast.makeText(getApplication(),"메뉴1",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.m2:
                                Toast.makeText(getApplication(),"메뉴2",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }

                        return false;
                    }
                });
                popup.show();
            }
        });

        AndPermission.with(this)
                .runtime()
                .permission(
                        Permission.ACCESS_FINE_LOCATION,
                        Permission.ACCESS_COARSE_LOCATION)
                .onGranted(new Action<List<String>>(){
                    public void onAction(List<String> permission) {}
                })
                .onDenied(new Action<List<String>>(){
                    public void onAction(List<String> permission){}
                })
                .start();

        home = (home) getSupportFragmentManager().findFragmentById(R.id.fragmenthome);
        random_menu = new random_menu();
        search_store = new search_store();





    }

    public void onFragmentChanged(int index){
        if(index == 0){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, home).commit();
        }
        else if(index == 1){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, random_menu).commit();
        }
        else if(index == 2){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, search_store).commit();
        }
    }

    public void startLocationService(){
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Bundle bundle = new Bundle();
        try{
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null){
                double latitude = location.getLatitude();
                double longtitude = location.getLongitude();
                search_store.setArguments(bundle);
            }

            GPSListener gpsListener = new GPSListener();
            long minTime = 10000;
            float minDistance = 0;

            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime, minDistance, gpsListener);

        }catch(SecurityException e){
            e.printStackTrace();
        }
    }


    class GPSListener implements LocationListener{
        @Override
        public void onLocationChanged(@NonNull Location location) {
            latitude = location.getLatitude();
            longtitude = location.getLongitude();

//            Bundle bundle = new Bundle();
//            bundle.putDouble("LATITUDE",latitude);
//            bundle.putDouble("LONGTITUDE",longtitude);
//            search_store.setArguments(bundle);
            Log.d("MM","위경main"+latitude+" / "+longtitude);
            //search_store.textView.setText("위도 : "+latitude + "\n경도 : " + longtitude);
            reverseCoding();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }

        @Override
        public void onProviderEnabled(@NonNull String provider) { }

        @Override
        public void onProviderDisabled(@NonNull String provider) { }
    }

    public void reverseCoding(){
        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(latitude, longtitude, 10); // 위도, 경도, 얻어올 값의 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
        }

        if (list != null) {
            if (list.size() == 0) {
                search_store.textView.setText("해당되는 주소 정보는 없습니다");
            } else {
                String cut[] = list.get(0).toString().split(" ");
                for (int i = 0; i < cut.length; i++) {
                    System.out.println("cut[" + i + "] : " + cut[i]);
                }
                //search_store.textView.setText(cut[1] + " / " + cut[2] + " / " + cut[3]);

                result_address = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query="+cut[1]+"+"+cut[2]+"+"+cut[3]+"+"+random_menu.menu_res;

                urlOpen(result_address);

            }

        }
    }

    public void urlOpen(String url){
        Log.d("menu", url);
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(myIntent);


    }

}