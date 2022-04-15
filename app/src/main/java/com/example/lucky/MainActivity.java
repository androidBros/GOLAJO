package com.example.lucky;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import androidx.fragment.app.FragmentManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//
public class MainActivity extends AppCompatActivity {
    ImageButton option_btn;  // 설정 버튼
    ImageButton back_btn; // 뒤로가기 버튼

    home home; // 프레그먼트
    random_menu random_menu; // 프레그먼트

    double latitude; // 위도
    double longtitude; // 경도

    Geocoder geocoder;
    String result_address;

    String locale;
    SharedPreferences sharedPreferences;
    int locale_number;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home = new home();
        random_menu = new random_menu();
        onFragmentChanged(0);
        geocoder = new Geocoder(this);

        sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);
        //버전 확인후 sharedpreferences에 locale키 값의 value값을 가져옵니다.
        // 값이 없을 경우 기본언어 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = sharedPreferences.getString("locale",getResources().getConfiguration().getLocales().get(0).getLanguage());
        }
        else {
            locale = sharedPreferences.getString("locale", Resources.getSystem().getConfiguration().locale.getLanguage());
        }
        //스피너 기본 선택값을 주기 위해서 해당 언어의 순서에 맞게 int값을 준비해줍니다.
        switch (locale){
            case "ko":{
                locale_number = 0;
                break;
            }

            case "en":{
                locale_number = 1;
                break;
            }
        }



        // 설정
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
                                option_help();
                                break;
                            case R.id.english_sc:
                                Toast.makeText(getApplication(),"영어",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.korea_sc:
                                Toast.makeText(getApplication(),"한국어",Toast.LENGTH_SHORT).show();
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


        // 권한 허가 요청
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


    }

//    // 언어 변경
//    private void changeLocale(String localeLang){
//        Locale locale = null;
//
//        switch (localeLang){
//            case "ko":
//                locale = new Locale("ko");
//                break;
//            case "en":
//                locale = new Locale("en");
//                break;
//        }
//        Configuration config = context.getResources().getConfiguration();
//
//
//    }


    // 설정 -> 도움말
    public void option_help(){
        Intent intent = new Intent(this, option_help.class);
        startActivity(intent);
    }


    //프레그먼트 변환
    public void onFragmentChanged(int index){

        if(index == 0){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, home).addToBackStack(null).commit();
        }
        else if(index == 1){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, random_menu).addToBackStack(null).commit();
        }
    }


    // 위치 검색  startLocationService()
    public void startLocationService(){
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Bundle bundle = new Bundle();
        try{
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null){
                double latitude = location.getLatitude();
                double longtitude = location.getLongitude();
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

    // 위치 검색  GPSListener
    class GPSListener implements LocationListener{
        @Override
        public void onLocationChanged(@NonNull Location location) {
            latitude = location.getLatitude();
            longtitude = location.getLongitude();

            Log.d("MM","위경main"+latitude+" / "+longtitude);

            reverseCoding();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }

        @Override
        public void onProviderEnabled(@NonNull String provider) { }

        @Override
        public void onProviderDisabled(@NonNull String provider) { }
    }

    // GPS 위도 경도 -> 주소 url로 변환
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

            } else {
                String cut[] = list.get(0).toString().split(" ");
                for (int i = 0; i < cut.length; i++) {
                    System.out.println("cut[" + i + "] : " + cut[i]);
                }

                result_address = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query="+cut[1]+"+"+cut[2]+"+"+cut[3]+"+"+random_menu.menu_res;

                urlOpen(result_address);

            }

        }
    }

    // WebView 띄우기
    public void urlOpen(String url){
        Log.d("menu", url);
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(myIntent);

    }

}