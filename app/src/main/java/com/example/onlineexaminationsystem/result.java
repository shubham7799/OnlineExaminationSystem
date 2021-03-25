package com.example.onlineexaminationsystem;

public class result {
    String user;
    int score;
    result(){}
    result(String user,int score){
        this.user=user;
        this.score=score;
    }

    public int getScore() {
        return score;
    }

    public String getUser() {
        return user;
    }
}
