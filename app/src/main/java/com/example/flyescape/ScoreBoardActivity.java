package com.example.flyescape;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flyescape.fragments.MapsFragment;
import com.example.flyescape.fragments.ScoreBoardFragment;
import com.example.flyescape.interfaces.CallBack_SendClick;
import com.google.android.material.imageview.ShapeableImageView;

public class ScoreBoardActivity extends AppCompatActivity {
    private ScoreBoardFragment scoreBoardFragment;

    private MapsFragment mapsFragment;

    private ShapeableImageView scoreboard_IMG_goback;


    private CallBack_SendClick callBack_sendClick = new CallBack_SendClick() {
        @Override
        public void scoreChosen(int rank, double x, double y) {
            mapsFragment.zoomOnRecord(rank, x, y);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        initFragments();

        beginTransactions();

        scoreboard_IMG_goback = findViewById(R.id.scoreboard_IMG_goback);

        setClickListener();

    }

    private void setClickListener() {
        scoreboard_IMG_goback.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreBoardActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void beginTransactions() {
        getSupportFragmentManager().beginTransaction().add(R.id.scoreboard_FRM_scores, scoreBoardFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.scoreboard_FRM_map, mapsFragment).commit();
    }

    private void initFragments() {
        scoreBoardFragment = new ScoreBoardFragment();
        scoreBoardFragment.setCallBack_sendClick(callBack_sendClick);
        mapsFragment = new MapsFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.menuMediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.menuMediaPlayer.pause();
    }
}