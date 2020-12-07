package com.example.allears;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.chord_training_button:
                openActivityChordLanding();
                break;

            case R.id.interval_training_button:
                openActivityIntervalLanding();
                break;
        }
    }


    private void openActivityIntervalLanding() {
        Intent intent = new Intent(this, IntervalLandingActivity.class );
        startActivity( intent );
    }

    private void openActivityChordLanding() {
        Intent intent = new Intent(this, ChordLandingActivity.class );
        startActivity( intent );
    }



}