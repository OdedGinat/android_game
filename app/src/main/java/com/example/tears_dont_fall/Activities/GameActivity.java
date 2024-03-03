package com.example.tears_dont_fall.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.tears_dont_fall.Activities.Interfaces.MovementCallback;
import com.example.tears_dont_fall.DataManager;
import com.example.tears_dont_fall.Model.MySP;
import com.example.tears_dont_fall.Model.Record;
import com.example.tears_dont_fall.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    public static final String KEY_DIFFICULTY = "KEY_DIFFICULTY";
    public static final String KEY_SENSOR = "KEY_SENSOR";
    public static final String KEY_LONGITUDE = "KEY_LONGITUDE";
    public static final String KEY_LATITUDE = "KEY_LATITUDE";

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

    private SpeedManager speedManager;

    private final int LANE_NUM = 5;

    private final int SEP = 100;
    private boolean isSensor;
    private Record record;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        record = new Record().setScore(0);
        setContentView(R.layout.activity_game);
        SignalGenerator.init(this);
        MySP.init(this);
        DataManager.init();
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

        checkSensorMode();
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
        if (isSensor)
            speedManager.stop();
    }

    public void updateScore(int score) {
        record.setScore(record.getScore() + score);
    }

    public Record getRecord() {
        return record;
    }

    private void getValuesPreviousIntent() {
        Intent previousIntent = getIntent();
        difficulty = previousIntent.getIntExtra(KEY_DIFFICULTY, 0);
        isSensor = previousIntent.getBooleanExtra(KEY_SENSOR, false);
        delay = (3-difficulty ) * 300;
        getRecord().setLatitude(previousIntent.getDoubleExtra(KEY_LATITUDE,31.0));
        getRecord().setLongitude(previousIntent.getDoubleExtra(KEY_LONGITUDE,31.0));

    }

    private void checkSensorMode() {
        if (isSensor) { // sensor mode
            speedManager = new SpeedManager(this, new MovementCallback() {
                @Override
                public void playerMovement(int direction) {
                    clicked(direction);
                }

                @Override
                public void playerSpeed(int y) {
                }
            });
            game_FAB_leftArrow.setVisibility(View.INVISIBLE);
            game_FAB_rightArrow.setVisibility(View.INVISIBLE);
            speedManager.start();
        } else {
            game_FAB_leftArrow.setVisibility(View.VISIBLE);
            game_FAB_rightArrow.setVisibility(View.VISIBLE);
        }
    }

    private void refreshUI() {
        int pos = player.getPos();
        player.setView(pos);
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
                findViewById(R.id.game_IMG_player3),
                findViewById(R.id.game_IMG_player4),
                findViewById(R.id.game_IMG_player5)};

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
        if (isSensor)
            speedManager.stop();
        updateScore(gameManger.getObjCount());
        SignalGenerator.getInstance().toast("game over pleb");
        initNamePopup();
    }

    private void initNamePopup() {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_name, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupWindow.showAtLocation(findViewById(R.id.activity_game), Gravity.CENTER, 0, 0);

        MaterialButton popup_BTN_submit = popupView.findViewById(R.id.popup_BTN_submit);
        MaterialTextView popup_LBL_score = popupView.findViewById(R.id.popup_LBL_score);
        TextInputEditText popup_ET_name = popupView.findViewById(R.id.popup_ET_name);

        popup_LBL_score.setText("Score "+gameManger.getObjCount());

        popup_BTN_submit.setOnClickListener(view -> popupClicked(popup_ET_name,popupWindow));

    }

    private void popupClicked(TextInputEditText popup_ET_name, PopupWindow popupWindow){
        if(popup_ET_name.length() == 0){
            SignalGenerator.getInstance().toast("Please enter a name");
        }
        else{
            getRecord().setName(popup_ET_name.getText().toString());
            popupWindow.dismiss();
            openGradesScreen();
        }
    }

    private void openGradesScreen() {
        DataManager.getInstance().addRecord(getRecord());
        Intent scoreIntent = new Intent(this, ScoreBoardActivity.class);
        startActivity(scoreIntent);
        finish();
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