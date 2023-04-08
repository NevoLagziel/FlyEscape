package com.example.flyescape;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;

import com.google.android.material.button.MaterialButton;

public class LostActivity extends AppCompatActivity {

    private MaterialButton lost_BTN_play_again;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);

        lost_BTN_play_again = findViewById(R.id.lost_BTN_play_again);

        setPlayAgainClickListeners();
    }

    private void setPlayAgainClickListeners() {
        MaterialButton mb = lost_BTN_play_again;
        mb.setOnClickListener(v -> clicked());
    }
    private void clicked() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}