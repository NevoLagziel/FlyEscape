package com.example.flyescape;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.flyescape.model.Score;
import com.example.flyescape.utilities.DataManager;
import com.example.flyescape.utilities.MySPv3;
import com.example.flyescape.utilities.SignalGenerator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

public class LostActivity extends AppCompatActivity {

    private String playerName;
    private int score;
    private Location gps_Location;
    private Location final_Location;
    private Location network_Location;

    private DataManager dataManager;
    private LocationManager locationManager;

    private double x;

    private double y;

    private boolean lastSpeedType;

    private boolean lastSensorType;

    private ShapeableImageView lost_IMG_goback;
    private MaterialTextView lost_MTV_gameover;
    private MaterialButton lost_BTN_scoreboard;
    private MaterialButton lost_BTN_tryagain;
    private MaterialTextView lost_MTV_name;
    private MaterialTextView lost_MTV_score;

    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);
        Bundle bundle = getIntent().getBundleExtra(MenuActivity.BUNDLE);
        playerName = bundle.getString(MainActivity.PLAYER_NAME);
        score = bundle.getInt(MainActivity.SCORE);
        lastSensorType = bundle.getBoolean(MainActivity.SENSOR_TYPE);
        lastSpeedType = bundle.getBoolean(MainActivity.SPEED_TYPE);

        findViews();

        lost_MTV_score.setText("Score : "+score);
        lost_MTV_name.setText(playerName);

        initVies();
        getLocation();
        insertNewRecord(playerName,score);
        mediaPlayer = SignalGenerator.getInstance().backgroundSound(R.raw.lost_activity_background);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    private void insertNewRecord(String playerName, int score) {
        getLocation();
        insertRecord(playerName,score,x,y);
    }

    public void initVies(){
        lost_BTN_scoreboard.setOnClickListener(v -> {
            Intent intent = new Intent(LostActivity.this, ScoreBoardActivity.class);
            startActivity(intent);
            finish();
        });

        lost_IMG_goback.setOnClickListener(v -> {
            Intent intent = new Intent(LostActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        });

        lost_BTN_tryagain.setOnClickListener(v -> {
            Intent intent;
            Bundle bund = new Bundle();
            bund.putBoolean(MainActivity.SENSOR_TYPE,lastSensorType);
            bund.putBoolean(MainActivity.SPEED_TYPE,lastSpeedType);
            bund.putString(MainActivity.PLAYER_NAME,playerName);
            intent = new Intent(LostActivity.this, MainActivity.class);
            intent.putExtra(MenuActivity.BUNDLE, bund);
            startActivity(intent);
            finish();
        });
    }


    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        } else {

            try {
                gps_Location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                network_Location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (gps_Location != null) {
                final_Location = gps_Location;
                x = final_Location.getLatitude();
                y = final_Location.getLongitude();
            } else if (network_Location != null) {
                final_Location = network_Location;
                x = final_Location.getLatitude();
                y = final_Location.getLongitude();
            } else {
                x = 0.0;
                y = 0.0;
            }
        }
    }

    private void insertRecord(String name, int score, double x, double y) {
        String json = MySPv3.getInstance().getString(App.KEY,"");
        dataManager = new Gson().fromJson(json,DataManager.class);
        dataManager.addScore(new Score().setName(name).setScore(score).setX(x).setY(y));
        String newJson = new Gson().toJson(dataManager);
        MySPv3.getInstance().putString(App.KEY, newJson);
    }

    private void findViews() {
        lost_IMG_goback = findViewById(R.id.lost_IMG_goback);
        lost_MTV_gameover = findViewById(R.id.lost_MTV_gameover);
        lost_BTN_scoreboard= findViewById(R.id.lost_BTN_scoreboard);
        lost_BTN_tryagain= findViewById(R.id.lost_BTN_tryagain);
        lost_MTV_name= findViewById(R.id.lost_MTV_name);
        lost_MTV_score= findViewById(R.id.lost_MTV_score);
    }

}