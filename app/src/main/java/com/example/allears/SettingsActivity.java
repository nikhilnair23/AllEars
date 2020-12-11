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
    String volumeText;
    //final GlobalClass globalClass = (GlobalClass) getApplicationContext();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setVolumeButton = findViewById(R.id.settings_setVolumeButton);
        setGoalButton = findViewById(R.id.settings_setGoalButton);
        volumeSlider = findViewById(R.id.settings_volumeSlider);

        // Get globalClass for getting/storing values
        final GlobalClass globalClass = (GlobalClass) getApplicationContext();

        // shows current values
        //volumeText = findViewById(R.string.set_volume);


        volumeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                globalClass.setVolume((volumeSlider.getProgress()));
                Toast.makeText( SettingsActivity.this, "Output volume set to: " + volumeSlider.getProgress(), Toast.LENGTH_SHORT).show();
                if (setVolumeButton != null) {
                    setVolumeButton.setText(String.format("Volume: %d", globalClass.getVolume()));
                }

            }
        });

        goalSlider = findViewById(R.id.settings_goalSlider);
        goalSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                globalClass.setGoal(goalSlider.getProgress());
                Toast.makeText(SettingsActivity.this, "Daily training goal set to: " + goalSlider.getProgress(), Toast.LENGTH_SHORT).show();
                if (setGoalButton != null) {
                    setGoalButton.setText(String.format("Goal: %d", globalClass.getGoal()));
                }
            }
        });
//        setVolumeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final GlobalClass globalClass = (GlobalClass) getApplicationContext();
//                volumeSlider = findViewById(R.id.settings_volumeSlider);
//                globalClass.setVolume((volumeSlider.getProgress()));
//                Toast.makeText( SettingsActivity.this, "Output volume set to: " + volumeSlider.getProgress(), Toast.LENGTH_SHORT).show();
//            }
//        });

//        setGoalButton = findViewById(R.id.settings_setGoalButton);
//        setGoalButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final GlobalClass globalClass = (GlobalClass) getApplicationContext();
//                goalSlider = findViewById(R.id.settings_goalSlider);
//                globalClass.setGoal(goalSlider.getProgress());
//                Toast.makeText(SettingsActivity.this, "Daily training goal set to: " + goalSlider.getProgress(), Toast.LENGTH_SHORT).show();
//            }
//        });

        backButton = findViewById(R.id.settings_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        final GlobalClass globalClass = (GlobalClass) getApplicationContext();
//        volumeOnPause = globalClass.getVolume();
//        goalOnPause = globalClass.getGoal();
//    }

    @Override
    protected void onResume() {
        super.onResume();
        final GlobalClass globalClass = (GlobalClass) getApplicationContext();
        volumeSlider.setProgress(globalClass.getVolume());
        goalSlider.setProgress(globalClass.getGoal());
        setVolumeButton.setText(String.format("Volume: %d", globalClass.getVolume()));
        setGoalButton.setText(String.format("Goal: %d", globalClass.getGoal()));
    }
}



