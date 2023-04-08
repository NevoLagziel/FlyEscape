package com.example.flyescape.logic;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.sql.Array;
import java.util.Random;

/**
 * Created by  on ${16},,${2022}
 */
public class GameManager {
    private int life;

    private int hits;

    private int[][] board;

    private int flyPos;

    public GameManager(int life)
    {
        this.life = life;
        this.hits = 0;
        this.flyPos = 1;
        this.board = new int[8][3];
        board[board.length-1][(board[0].length/2)] = 1;
    }

    public int getHits(){
        return hits;
    }


    public boolean isLost() {
        return life == hits;
    }

    public int[][] getBoard(){
        return board;
    }
    public int getFlyPos(){
        return flyPos;
    }
    public boolean checkForHit(Context context , Vibrator v){
            if(board[board.length-2][flyPos] == 1) {
                hits++;
                Toast.makeText(context, " 🤕 Ouch!", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(500);
                }
                return true;
            }
            return false;
    }
    public void moveFly(int flyPos){
        if(flyPos <= board[0].length-1 && flyPos >= 0) {
            this.board[board.length-1][this.flyPos] = 0;
            this.flyPos = flyPos;
            this.board[board.length-1][flyPos] = 1;
        }
    }

    public int addObs(){
        Random random = new Random();
        int randomNumber = random.nextInt(board[0].length);
        board[0][randomNumber] = 1;
        return randomNumber;
    }

    public void moveDown(){
        for(int i = board.length - 2;i >= 0;i--)
        {
            for(int j = 0;j < board[0].length;j++)
            {
                if(board[i][j] == 1)
                {
                    if(i+1 < (board.length - 1)) {
                        board[i + 1][j] = 1;
                    }
                    board[i][j] = 0;
                }
            }
        }
    }
}
