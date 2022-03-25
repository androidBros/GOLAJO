package com.example.lucky;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ImageButton option_btn;

    home home;
    random_menu random_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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


        home = (home) getSupportFragmentManager().findFragmentById(R.id.fragmenthome);
        random_menu = new random_menu();
    }

    public void onFragmentChanged(int index){
        if(index == 0){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, home).commit();
        }
        else if(index == 1){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, random_menu).commit();
        }
    }




}