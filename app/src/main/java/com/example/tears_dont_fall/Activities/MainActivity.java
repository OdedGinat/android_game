package com.example.tears_dont_fall.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.example.tears_dont_fall.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {
    private MaterialButton newGameBtn;
    private MaterialButton scoreboardBtn;
    private SwitchMaterial sensorBtn;
    private SwitchMaterial hardmodeBtn;
    private SwitchMaterial nightmareBtn;

    private int difficulty ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getUIElements();
        switchListener();
        buttonListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void buttonListener(){
        newGameBtn.setOnClickListener(view -> {
            openGameScreen();
        });
    }

    private void switchListener(){
        hardmodeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean hardSw) {
                int visibility = (hardSw) ? View.VISIBLE : View.INVISIBLE;
                nightmareBtn.setVisibility(visibility);
                difficulty = (hardSw) ? 1 : 0;
            }
        });
        nightmareBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean nightmareSw) {
                difficulty = (nightmareSw) ? 2 : 1;
            }
        });
    }

    private void getUIElements(){
        newGameBtn = findViewById(R.id.new_game_btn);
        scoreboardBtn = findViewById(R.id.scoreboard_btn);
        sensorBtn = findViewById(R.id.sensor_sw);
        hardmodeBtn = findViewById(R.id.hard_mode_btn);
        nightmareBtn = findViewById(R.id.very_hard_mode_btn);
        nightmareBtn.setVisibility(View.INVISIBLE);
    }

    private void openGameScreen() {
        Intent gameIntent = new Intent(this, GameActivity.class);
        gameIntent.putExtra(GameActivity.KEY_DIFFICULTY, difficulty);
        startActivity(gameIntent);
        finish();
    }
}