package com.example.flyescape;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flyescape.logic.GameManager;
import com.example.flyescape.utilities.SignalGenerator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {

    public final int DEF_SPEED = 100;

    public static final String SENSOR_TYPE = "SENSOR_TYPE";

    public static final String SPEED_TYPE = "SPEED_TYPE";

    public static final String PLAYER_NAME = "PLAYER_NAME";

    public static final String SCORE = "SCORE";

    private boolean sensorType;

    private boolean speedType;
    private String playerName;



    private final int DOWNDELAY = 500;
    private final int ADDDELAY = 1000;
    private ShapeableImageView[] main_IMG_hearts;

    private FloatingActionButton main_FAB_left;

    private FloatingActionButton main_FAB_right;

    private ShapeableImageView[][] main_IMG_board;

    private MaterialTextView main_LBL_score;
    private MaterialTextView main_LBL_speed;

    private GameManager gameManager;

    private SensorManager sensorManager;

    private Sensor sensor;

    private SensorEventListener sensorEventListener;

    private long moveTimestamp = 0;

    private long changeSpeedTimestamp = 0;

    private MediaPlayer mediaPlayer;

    private final Handler downHandler = new Handler();

    private final Handler addHandler = new Handler();
    private Runnable downRunnable = new Runnable() {
        @Override
        public void run() {
            downHandler.postDelayed(this, (long) (DOWNDELAY/gameManager.getSpeed())); //Do it again in a second
            moveDown();
        }
    };

    private Runnable addRunnable = new Runnable() {
        @Override
        public void run() {
            addHandler.postDelayed(this,(long) (ADDDELAY/gameManager.getSpeed())); //Do it again in a second
            addObs();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getBundleExtra(MenuActivity.BUNDLE);
        sensorType = bundle.getBoolean(SENSOR_TYPE);
        speedType = bundle.getBoolean(SPEED_TYPE);
        playerName = bundle.getString(PLAYER_NAME);

        findViews();

        if(speedType) {
            gameManager = new GameManager(main_IMG_hearts.length, 2);
        }else
            gameManager = new GameManager(main_IMG_hearts.length,1);

        if(sensorType){
            main_FAB_left.setVisibility(View.INVISIBLE);
            main_FAB_right.setVisibility(View.INVISIBLE);
            initSensors();
            initSensorEventListener();
        }else{
            setClickListeners();
        }
        changeSpeedView();

        mediaPlayer = SignalGenerator.getInstance().backgroundSound(R.raw.main_activity_background);
    }

    private void initSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private void initSensorEventListener(){
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];

                calculateStep(x, y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                //not needed
            }
        };
    }

    private void calculateStep(float x, float y) {
        if (System.currentTimeMillis() - moveTimestamp > 300) {
            moveTimestamp = System.currentTimeMillis();
            if (x > 3.0) {
                clicked(-1);
            } else if (x < -3.0) {
                clicked(1);
            }
        }
        if (System.currentTimeMillis() - changeSpeedTimestamp > 600) {
            changeSpeedTimestamp = System.currentTimeMillis();
            if (y > 3.0) {
                changeSpeed(-1);
            } else if (y < -3.0)
                changeSpeed(1);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRunnable();
        if(sensorType){
            sensorManager.unregisterListener(sensorEventListener,sensor);
        }
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activeRunnable();
        if(sensorType){
            sensorManager.registerListener(sensorEventListener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
        mediaPlayer.start();
    }

    private void setRightClickListener() {
        main_FAB_right.setOnClickListener(v -> clicked(1));
    }

    private void setLeftClickListener() {
        main_FAB_left.setOnClickListener(v -> clicked(-1));
    }

    private void setClickListeners() {
        setRightClickListener();
        setLeftClickListener();
    }

    private void clicked(int side) {
        int oldPos = gameManager.getFlyPos();
        gameManager.moveFly(oldPos + side);
        changeFlyPosition(oldPos);
    }

    private void changeSpeed(int op){
        if(op == 1){
            gameManager.increaseSpeed();
        }else {
            gameManager.decreaseSpeed();
        }
        changeSpeedView();
    }

    private void changeFlyPosition(int oldFlyPos) {
        main_IMG_board[main_IMG_board.length - 1][oldFlyPos].setVisibility(View.INVISIBLE);
        main_IMG_board[main_IMG_board.length - 1][gameManager.getFlyPos()].setVisibility(View.VISIBLE);
    }

    private void decreaseHeart() {
        if (gameManager.isLost()) {
            openLooserScreen();
        } else {
            main_IMG_hearts[main_IMG_hearts.length - gameManager.getHits()].setVisibility(View.INVISIBLE);
        }
    }

    private void openLooserScreen() {
        stopRunnable();
        if(sensorType) {
            sensorManager.unregisterListener(sensorEventListener,sensor);
        }
        Intent intent;
        Bundle bund = new Bundle();
        bund.putString(MainActivity.PLAYER_NAME,playerName);
        bund.putInt(SCORE,gameManager.getScore());
        bund.putBoolean(SENSOR_TYPE,sensorType);
        bund.putBoolean(SPEED_TYPE,speedType);
        intent = new Intent(this, LostActivity.class);
        intent.putExtra(MenuActivity.BUNDLE, bund);
        startActivity(intent);
        MainActivity.this.finish();
    }

    private void addObs() {
        int add = gameManager.addObs();
        if (gameManager.getBoard()[0][add] == 2) {
            main_IMG_board[0][add].setImageResource(R.drawable.poop);
        } else if (gameManager.getBoard()[0][add] == 1)
            main_IMG_board[0][add].setImageResource(R.drawable.flip_flops);
        main_IMG_board[0][add].setVisibility(View.VISIBLE);
    }

    private void moveDown() {
        gameManager.checkForBonus();
        if (gameManager.checkForHit()) {
            decreaseHeart();
            int deadFlyPos = gameManager.getFlyPos();
            main_IMG_board[main_IMG_board.length - 1][deadFlyPos].setImageResource(R.drawable.blood);
            stopShowingBlood(deadFlyPos);
        }
        gameManager.moveDown();
        updateBoard();
    }

    private void stopShowingBlood(int deadFlyPos) {
        new CountDownTimer(500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                //nothing
            }

            @Override
            public void onFinish() {
                main_IMG_board[main_IMG_board.length - 1][deadFlyPos].setImageResource(R.drawable.fruit_fly);
            }
        }.start();
    }

    private void updateBoard() {
        for (int i = 0; i < main_IMG_board.length - 1; i++) {
            for (int j = 0; j < main_IMG_board[0].length; j++) {
                if (gameManager.getBoard()[i][j] == 2) {
                    main_IMG_board[i][j].setImageResource(R.drawable.poop);
                    main_IMG_board[i][j].setVisibility(View.VISIBLE);
                } else if (gameManager.getBoard()[i][j] == 1) {
                    main_IMG_board[i][j].setImageResource(R.drawable.flip_flops);
                    main_IMG_board[i][j].setVisibility(View.VISIBLE);
                } else
                    main_IMG_board[i][j].setVisibility(View.INVISIBLE);
            }
        }
        main_LBL_score.setText("" + gameManager.getScore());
    }


    private void activeRunnable() {
        boolean check = false;
        for (int i = 0; i < gameManager.getBoard()[0].length; i++) {
            if (gameManager.getBoard()[0][i] == 1)
                check = true;
        }
        if (check) {
            addHandler.postDelayed(addRunnable, 1100);
            downHandler.postDelayed(downRunnable, 0);
        } else {
            addHandler.postDelayed(addRunnable, 100);
            downHandler.postDelayed(downRunnable, 0);
        }
    }

    private void stopRunnable() {
        downHandler.removeCallbacks(downRunnable);
        addHandler.removeCallbacks(addRunnable);
    }

    private void changeSpeedView(){
        int s = (int)(DEF_SPEED*gameManager.getSpeed());
        main_LBL_speed.setText(s + " Km/h");
    }

    private void findViews() {
        main_FAB_left = findViewById(R.id.main_FAB_left);
        main_FAB_right = findViewById(R.id.main_FAB_right);
        main_LBL_score = findViewById(R.id.main_LBL_score);
        main_LBL_speed = findViewById(R.id.main_LBL_speed);

        main_IMG_board = new ShapeableImageView[][]{
                {findViewById(R.id.main_IMG_flipflop01),
                        findViewById(R.id.main_IMG_flipflop02),
                        findViewById(R.id.main_IMG_flipflop03),
                        findViewById(R.id.main_IMG_flipflop04),
                        findViewById(R.id.main_IMG_flipflop05)},
                {findViewById(R.id.main_IMG_flipflop11),
                        findViewById(R.id.main_IMG_flipflop12),
                        findViewById(R.id.main_IMG_flipflop13),
                        findViewById(R.id.main_IMG_flipflop14),
                        findViewById(R.id.main_IMG_flipflop15)},
                {findViewById(R.id.main_IMG_flipflop21),
                        findViewById(R.id.main_IMG_flipflop22),
                        findViewById(R.id.main_IMG_flipflop23),
                        findViewById(R.id.main_IMG_flipflop24),
                        findViewById(R.id.main_IMG_flipflop25)},
                {findViewById(R.id.main_IMG_flipflop31),
                        findViewById(R.id.main_IMG_flipflop32),
                        findViewById(R.id.main_IMG_flipflop33),
                        findViewById(R.id.main_IMG_flipflop34),
                        findViewById(R.id.main_IMG_flipflop35)},
                {findViewById(R.id.main_IMG_flipflop41),
                        findViewById(R.id.main_IMG_flipflop42),
                        findViewById(R.id.main_IMG_flipflop43),
                        findViewById(R.id.main_IMG_flipflop44),
                        findViewById(R.id.main_IMG_flipflop45)},
                {findViewById(R.id.main_IMG_flipflop51),
                        findViewById(R.id.main_IMG_flipflop52),
                        findViewById(R.id.main_IMG_flipflop53),
                        findViewById(R.id.main_IMG_flipflop54),
                        findViewById(R.id.main_IMG_flipflop55)},
                {findViewById(R.id.main_IMG_flipflop61),
                        findViewById(R.id.main_IMG_flipflop62),
                        findViewById(R.id.main_IMG_flipflop63),
                        findViewById(R.id.main_IMG_flipflop64),
                        findViewById(R.id.main_IMG_flipflop65)},
                {findViewById(R.id.main_IMG_flipflop71),
                        findViewById(R.id.main_IMG_flipflop72),
                        findViewById(R.id.main_IMG_flipflop73),
                        findViewById(R.id.main_IMG_flipflop74),
                        findViewById(R.id.main_IMG_flipflop75)},
                {findViewById(R.id.main_IMG_flipflop81),
                        findViewById(R.id.main_IMG_flipflop82),
                        findViewById(R.id.main_IMG_flipflop83),
                        findViewById(R.id.main_IMG_flipflop84),
                        findViewById(R.id.main_IMG_flipflop85)},
                {findViewById(R.id.main_IMG_flipflop91),
                        findViewById(R.id.main_IMG_flipflop92),
                        findViewById(R.id.main_IMG_flipflop93),
                        findViewById(R.id.main_IMG_flipflop94),
                        findViewById(R.id.main_IMG_flipflop95)},
                {findViewById(R.id.main_IMG_fly1),
                        findViewById(R.id.main_IMG_fly2),
                        findViewById(R.id.main_IMG_fly3),
                        findViewById(R.id.main_IMG_fly4),
                        findViewById(R.id.main_IMG_fly5)}};

        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)};
    }

}