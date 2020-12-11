package com.example.allears;

import android.app.Application;

public class GlobalClass extends Application{

    private int volume = 80;
    private int goal = 3;


    public int getVolume() {

        return volume;
    }

    public void setVolume(int value) {

        volume = value;

    }

    public int getGoal() {

        return goal;
    }

    public void setGoal(int value) {

        goal = value;
    }

}