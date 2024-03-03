package com.example.tears_dont_fall.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.tears_dont_fall.Activities.Fragments.ListFragment;
import com.example.tears_dont_fall.Activities.Fragments.MapFragment;
import com.example.tears_dont_fall.Activities.Interfaces.Callback_List;
import com.example.tears_dont_fall.R;

import com.google.android.material.button.MaterialButton;

public class ScoreBoardActivity extends AppCompatActivity {

    private ListFragment listFragment;
    private MapFragment mapFragment;
    private MaterialButton score_BTN_menu;

    private Callback_List callback_list = new Callback_List() {
        @Override
        public void setMapLocation(double lat, double lon, String name) {
            mapFragment.setMapLocation(lat,lon,name);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        listFragment = new ListFragment();
        mapFragment = new MapFragment();
        listFragment.setCallback_list(callback_list);
        findViews();
        getSupportFragmentManager().beginTransaction().add(R.id.score_FRAME_list,listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.score_FRAME_map,mapFragment).commit();

        score_BTN_menu.setOnClickListener(view -> {
            openMenuScreen();
        });

    }

    private void openMenuScreen() {
        Intent menuIntent = new Intent(this, MainActivity.class);
        startActivity(menuIntent);
        finish();
    }

    private void findViews(){
        this.score_BTN_menu = findViewById(R.id.score_BTN_Menu);
    }
}