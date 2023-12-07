package com.maciejsusala;

public class Player {
    private String name;
    private int score = 0;

    public Player() {
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void changeScore(int points){
        int score = getScore() + points;
        setScore(score);
    }
}
