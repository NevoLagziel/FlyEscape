package com.example.flyescape.logic;

import android.widget.Toast;

import com.example.flyescape.R;
import com.example.flyescape.utilities.SignalGenerator;

import java.util.Random;

public class GameManager {

    private final double MAX_SPEED = 2;

    private final double MIN_SPEED = 0.5;
    private int life;

    private int hits;

    private int[][] board;

    private int flyPos;

    private int score;

    private double speed;


    public GameManager(int life , double speed)
    {
        this.life = life;
        this.hits = 0;
        this.score = 0;
        this.board = new int[11][5];
        this.flyPos = (board[0].length/2);
        board[board.length-1][flyPos] = 1;
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public void increaseSpeed(){
        if(speed < MAX_SPEED)
            speed = speed*(1.25);
    }

    public void decreaseSpeed(){
        if(speed > MIN_SPEED)
            speed = speed/(1.25);
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

    public int getScore(){
        return score;
    }
    public boolean checkForHit(){
            if(board[board.length-2][flyPos] == 1) {
                hits++;
                SignalGenerator.getInstance().sound(R.raw.squish);
                SignalGenerator.getInstance().toast(" 🤕 Ouch!",Toast.LENGTH_SHORT);
                SignalGenerator.getInstance().vibrate(500);
                return true;
            }
            return false;
    }

    public void checkForBonus(){
        if(board[board.length-2][flyPos] == 2) {
            score += 10;
            SignalGenerator.getInstance().toast("🥳 +10 points",Toast.LENGTH_SHORT);
            SignalGenerator.getInstance().sound(R.raw.slurp);
        }
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
        int whatToAdd = random.nextInt(20);
        int randomNumber = random.nextInt(board[0].length);
        if(whatToAdd < 17) {
            board[0][randomNumber] = 1;
        }else
            board[0][randomNumber] = 2;
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
                if(board[i][j] == 2)
                {
                    if(i+1 < (board.length - 1)) {
                        board[i + 1][j] = 2;
                    }
                    board[i][j] = 0;
                }
            }
        }
        score++;
    }
}
