package com.example.allears;

import android.app.Application;

public class GlobalClass extends Application{

    private int volume;
    private String email;


    public int getVolume() {

        return volume;
    }

    public void setVolume(int value) {

        volume = value;

    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String aEmail) {

        email = aEmail;
    }

}