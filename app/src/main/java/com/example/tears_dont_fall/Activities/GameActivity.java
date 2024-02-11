package com.example.tears_dont_fall.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.tears_dont_fall.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    public static final String KEY_DIFFICULTY = "KEY_DIFFICULTY";

    private Timer timer;
    private BoardManager gameManger;

    private int delay;
    private ExtendedFloatingActionButton game_FAB_leftArrow;
    private ExtendedFloatingActionButton game_FAB_rightArrow;
    private LinearLayout[] game_LL_obstacles;
    private LinearLayout game_LL_player;

    private View[] player_images;
    private ShapeableImageView[] game_IMG_hearts;
    private MaterialTextView game_LBL_score;
    private GameObject player;
    private int health = 3;
    private int difficulty;

    private final int LANE_NUM = 3;

    private final int SEP = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        SignalGenerator.init(this);
        findViews();

        player = new GameObject(LANE_NUM, SEP, player_images);
        this.gameManger = new BoardManager(5, LANE_NUM, game_LL_obstacles);
        getValuesPreviousIntent();
        game_FAB_leftArrow.setOnClickListener(view -> {
            clicked(-1);
        });
        game_FAB_rightArrow.setOnClickListener(view -> {
            clicked(1);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(health > 0)
            startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    private void getValuesPreviousIntent() {
        Intent previousIntent = getIntent();
        difficulty = previousIntent.getIntExtra(KEY_DIFFICULTY, 0);
        delay = (3-difficulty ) * 300;
    }

    private void findViews() {
        this.game_FAB_leftArrow = findViewById(R.id.game_FAB_leftArrow);
        this.game_FAB_rightArrow = findViewById(R.id.game_FAB_rightArrow);
        this.game_LL_player = findViewById(R.id.game_LL_player_space);

        this.game_LBL_score = findViewById(R.id.game_LBL_score);

        this.game_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3)
        };

        this.player_images = new View[]{
                findViewById(R.id.game_IMG_player1),
                findViewById(R.id.game_IMG_player2),
                findViewById(R.id.game_IMG_player3)};

        this.game_LL_obstacles = new LinearLayout[]{
                findViewById(R.id.obj_space6),
                findViewById(R.id.obj_space5),
                findViewById(R.id.obj_space3),
                findViewById(R.id.obj_space2),
                findViewById(R.id.obj_space)
        };

        game_FAB_leftArrow.setVisibility(View.VISIBLE);
        game_FAB_rightArrow.setVisibility(View.VISIBLE);
    }

    private void clicked(int direction) {
        player.modPos(direction);
    }


    public void updateObstacles() {
        gameManger.moveDown();
        gameManger.addObjCount();
        hitCheck();
    }


    private void hitCheck() {
        if (gameManger.check(player.getPos())) {
            removeHeart();
        }
    }

    public void newObstacle() {
        GameObject obj = new GameObject(LANE_NUM, SEP, null);
        gameManger.add(obj);
    }

    private void removeHeart() {
        game_IMG_hearts[health - 1].setVisibility(View.INVISIBLE);
        health--;
        SignalGenerator.getInstance().vibrate(500);
        SignalGenerator.getInstance().toast("hit");
        if (health == 0)
            gameOver();
    }

    private void gameOver() {
        stopTimer();
        SignalGenerator.getInstance().toast("game over pleb");
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    updateObstacles();
                    if (gameManger.getObjCount() % ((5-difficulty)) == 0)
                        newObstacle();
                    if(health > 0)
                        game_LBL_score.setText("" + gameManger.getObjCount());
                });
            }
        }, delay, delay);
    }

    private void stopTimer() {
        timer.cancel();
    }


}