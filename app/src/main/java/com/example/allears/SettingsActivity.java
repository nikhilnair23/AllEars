package com.example.allears;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    Button setVolumeButton;
    private EditText volumeValue;
    //final GlobalClass globalClass = (GlobalClass) getApplicationContext();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setVolumeButton = findViewById(R.id.settings_setVolumeButton);
        setVolumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GlobalClass globalClass = (GlobalClass) getApplicationContext();
                volumeValue = findViewById(R.id.settings_volumeNumber);
                globalClass.setVolume(Integer.parseInt(volumeValue.getText().toString()));
                Toast.makeText( SettingsActivity.this, "settings volume:" + volumeValue.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        final GlobalClass globalClass = (GlobalClass) getApplicationContext();

        globalClass.setVolume(80);
    }

//    public void onClick(View view){
//        switch (view.getId()){
//            case R.id.settings_setVolumeButton:
////                final GlobalClass globalClass = (GlobalClass) getApplicationContext();
////                globalClass.setVolume(75);
//                Toast.makeText( SettingsActivity.this, "settings volume", Toast.LENGTH_SHORT).show();
//                break;
////            case R.id.login_screen_login_button:
////                login();
////                break;
//        }
//    }
}





//
//
//public class SettingsActivity extends AppCompatActivity {
//
//    Button setVolumeButton = (Button) findViewById(R.id.setVolumeButton);
//    final GlobalClass globalClass = (GlobalClass) getApplicationContext();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings);
//
//        globalClass.setVolume(80);
//    }
//
//}