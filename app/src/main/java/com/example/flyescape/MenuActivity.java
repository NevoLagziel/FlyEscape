package com.example.flyescape;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textview.MaterialTextView;

public class MenuActivity extends AppCompatActivity {

    public static final String BUNDLE = "BUNDLE";

    private MaterialButton menu_BTN_score_board;

    private MaterialButton menu_BTN_start;

    private SwitchMaterial menu_SWM_speed_switch;

    private SwitchMaterial menu_SWM_mode_switch;

    private EditText menu_TXB_text_box;

    private MaterialTextView menu_MTV_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findVies();
        setOnClick();
    }


    private void setOnClick() {
        menu_BTN_start.setOnClickListener(v -> GameActivity(menu_SWM_mode_switch.isChecked(),menu_SWM_speed_switch.isChecked(),menu_TXB_text_box.getText().toString()));


        menu_BTN_score_board.setOnClickListener(v -> {
            Intent intent;
            intent = new Intent(MenuActivity.this, ScoreBoardActivity.class);
            startActivity(intent);
        });
    }

    private void findVies() {
        menu_BTN_score_board = findViewById(R.id.menu_BTN_score_board);
        menu_BTN_start = findViewById(R.id.menu_BTN_start);
        menu_TXB_text_box = findViewById(R.id.menu_TXB_text_box);
        menu_MTV_title = findViewById(R.id.menu_MTV_title);
        menu_SWM_speed_switch  = findViewById(R.id.menu_SWM_speed_switch);
        menu_SWM_mode_switch = findViewById(R.id.menu_SWM_mode_switch);
    }

    private void GameActivity(boolean sensorFlag,boolean speedFlag,String playerName) {
        //if(TextUtils.isEmpty(playerName))
        //    playerName = "UnNamed";
        Intent myIntent;
        Bundle bundle = new Bundle();
        bundle.putBoolean(MainActivity.SENSOR_TYPE,sensorFlag);
        bundle.putBoolean(MainActivity.SPEED_TYPE,speedFlag);
        bundle.putString(MainActivity.PLAYER_NAME,playerName);
        myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra(BUNDLE, bundle);
        startActivity(myIntent);
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