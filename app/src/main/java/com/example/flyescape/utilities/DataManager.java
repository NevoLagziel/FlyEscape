package com.example.flyescape.utilities;

import com.example.flyescape.model.Score;

import java.util.ArrayList;
import java.util.Collections;


public class DataManager {

    private  ArrayList<Score> scores;

    public DataManager(){
        scores = new ArrayList<>();
    }

    public  ArrayList<Score> getScores() {
        return scores;
    }

    public DataManager setScores(ArrayList<Score> scores) {
        this.scores = scores;
        return this;
    }

    public void addScore(Score score) {
        this.scores.add(score);
        Collections.sort(scores);
        if(this.scores.size() > 10)
            this.scores.remove(scores.size()-1);
        fixPlacement(scores);
    }

    public static void fixPlacement(ArrayList<Score> scores){
        for(int i = 0; i < scores.size();i++){
            scores.get(i).setPlace(i+1);
        }
    }

    @Override
    public String toString() {
        return "DataManager{" +
                "scores=" + scores +
                '}';
    }
}
