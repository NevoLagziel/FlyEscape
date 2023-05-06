package com.example.flyescape.model;


public class Score implements Comparable {

    private String name;

    private int place;

    private int score;

    private double x;

    private double y;

    public Score() {}

    public String getName() {
        return name;
    }

    public Score setName(String name) {
        this.name = name;
        return this;
    }

    public int getPlace() {
        return place;
    }

    public Score setPlace(int place) {
        this.place = place;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Score setScore(int score) {
        this.score = score;
        return this;
    }

    public double getX() {
        return x;
    }

    public Score setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public Score setY(double y) {
        this.y = y;
        return this;
    }


    @Override
    public int compareTo(Object o) {
        return ((Score)o).getScore() - this.score;
    }

    @Override
    public String toString() {
        return "Score{" +
                "name='" + name + '\'' +
                ", place=" + place +
                ", score=" + score +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
