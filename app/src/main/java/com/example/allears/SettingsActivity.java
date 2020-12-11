package com.example.allears;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    Button setVolumeButton;
    Button setGoalButton;
    Button backButton;
    private SeekBar volumeSlider;
    private SeekBar goalSlider;
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
                volumeSlider = findViewById(R.id.settings_volumeSlider);
                globalClass.setVolume((volumeSlider.getProgress()));
                Toast.makeText( SettingsActivity.this, "Output volume set to: " + volumeSlider.getProgress(), Toast.LENGTH_SHORT).show();
            }
        });

        setGoalButton = findViewById(R.id.settings_setGoalButton);
        setGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final GlobalClass globalClass = (GlobalClass) getApplicationContext();
                goalSlider = findViewById(R.id.settings_goalSlider);
                globalClass.setGoal(goalSlider.getProgress());
                Toast.makeText(SettingsActivity.this, "Daily training goal set to: " + goalSlider.getProgress(), Toast.LENGTH_SHORT).show();
            }
        });

        backButton = findViewById(R.id.settings_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}



