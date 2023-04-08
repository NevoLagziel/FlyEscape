package com.example.flyescape;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;

import com.example.flyescape.logic.GameManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

public class MainActivity extends AppCompatActivity {
    private final int DOWNDELAY = 1000;
    private final int ADDDELAY = 2000;
    private ShapeableImageView[] main_IMG_hearts;

    private FloatingActionButton main_FAB_left;

    private FloatingActionButton main_FAB_right;

    private ShapeableImageView[][] main_IMG_board;

    private GameManager gameManager;

    private final Handler downHandler = new Handler();

    private final Handler addHandler = new Handler();
    private Runnable downRunnable = new Runnable() {
        @Override
        public void run() {
            downHandler.postDelayed(this, DOWNDELAY); //Do it again in a second
            moveDown();
        }
    };

    private Runnable addRunnable = new Runnable() {
        @Override
        public void run() {
            addHandler.postDelayed(this, ADDDELAY); //Do it again in a second
            addObs();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        gameManager = new GameManager(main_IMG_hearts.length);

        setClickListeners();

        activeRunnable();
    }

    private void setRightClickListener() {
        main_FAB_right.setOnClickListener(v -> clicked(1));
    }

    private void setLeftClickListener() {
        main_FAB_left.setOnClickListener(v -> clicked(-1));
    }

    private void setClickListeners(){
        setRightClickListener();
        setLeftClickListener();
    }
    private void clicked(int side) {
        int oldPos = gameManager.getFlyPos();
        gameManager.moveFly(oldPos+side);
        changeFlyPosition(oldPos);
    }

    private void changeFlyPosition(int oldFlyPos) {
        main_IMG_board[main_IMG_board.length-1][oldFlyPos].setVisibility(View.INVISIBLE);
        main_IMG_board[main_IMG_board.length-1][gameManager.getFlyPos()].setVisibility(View.VISIBLE);
    }

    private void decreaseHeart(){
        if(gameManager.isLost()){
            openLooserScreen();
        } else {
                main_IMG_hearts[main_IMG_hearts.length - gameManager.getHits()].setVisibility(View.INVISIBLE);
        }
    }
    private void openLooserScreen() {
        stopRunnable();
        Intent intent = new Intent(this, LostActivity.class);
        startActivity(intent);
        finish();
    }
    private void addObs() {
        int add = gameManager.addObs();
        main_IMG_board[0][add].setVisibility(View.VISIBLE);
    }

    private void moveDown(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if(gameManager.checkForHit(this, v)) {
            decreaseHeart();
            int deadFlyPos = gameManager.getFlyPos();
            main_IMG_board[main_IMG_board.length-1][deadFlyPos].setImageResource(R.drawable.blood);
            stopShowingBlood(deadFlyPos);
        }
        gameManager.moveDown();
        updateBoard();
    }

    private void stopShowingBlood(int deadFlyPos){
        new CountDownTimer(500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                //nothing
            }
            @Override
            public void onFinish() {
                main_IMG_board[main_IMG_board.length-1][deadFlyPos].setImageResource(R.drawable.fruit_fly);
            }
        }.start();
    }
    private void updateBoard(){
        for(int i = 0; i < main_IMG_board.length; i++){
            for(int j = 0; j < main_IMG_board[0].length; j++){
                if(gameManager.getBoard()[i][j] == 1)
                    main_IMG_board[i][j].setVisibility(View.VISIBLE);
                else
                    main_IMG_board[i][j].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void activeRunnable(){
        addHandler.postDelayed(addRunnable,0);
        downHandler.postDelayed(downRunnable,DOWNDELAY);
    }
    private void stopRunnable(){
        downHandler.removeCallbacks(downRunnable);
        addHandler.removeCallbacks(addRunnable);
    }

    private void findViews() {
        main_FAB_left = findViewById(R.id.main_FAB_left);
        main_FAB_right = findViewById(R.id.main_FAB_right);

        main_IMG_board = new ShapeableImageView[][]{
                {findViewById(R.id.main_IMG_flipflop11),
                findViewById(R.id.main_IMG_flipflop12),
                findViewById(R.id.main_IMG_flipflop13)},
                {findViewById(R.id.main_IMG_flipflop21),
                findViewById(R.id.main_IMG_flipflop22),
                findViewById(R.id.main_IMG_flipflop23)},
                {findViewById(R.id.main_IMG_flipflop31),
                findViewById(R.id.main_IMG_flipflop32),
                findViewById(R.id.main_IMG_flipflop33)},
                {findViewById(R.id.main_IMG_flipflop41),
                findViewById(R.id.main_IMG_flipflop42),
                findViewById(R.id.main_IMG_flipflop43)},
                {findViewById(R.id.main_IMG_flipflop51),
                findViewById(R.id.main_IMG_flipflop52),
                findViewById(R.id.main_IMG_flipflop53)},
                {findViewById(R.id.main_IMG_flipflop61),
                findViewById(R.id.main_IMG_flipflop62),
                findViewById(R.id.main_IMG_flipflop63)},
                {findViewById(R.id.main_IMG_flipflop71),
                findViewById(R.id.main_IMG_flipflop72),
                findViewById(R.id.main_IMG_flipflop73)},
                {findViewById(R.id.main_IMG_flyleft),
                findViewById(R.id.main_IMG_flymid),
                findViewById(R.id.main_IMG_flyright)}};

        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)};
    }

}