package com.example.allears;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class ChordLandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chord_landing);
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.button_chord_landing_back:
                finish();
                break;

            case R.id.button_chord_landing_easy:
                openChordQuestionDifficulty( "easy" );
                break;

            case R.id.button_chord_landing_medium:
                openChordQuestionDifficulty( "medium" );
                break;

            case R.id.button_chord_landing_hard:
                openChordQuestionDifficulty( "hard" );
                break;
        }
    }



    private void openChordQuestionDifficulty(String difficulty ) {
        Intent intent = new Intent(this, ChordQuestionActivity.class );
        intent.putExtra( "difficulty", difficulty );
        ArrayList<Integer> blankScore = new ArrayList<Integer>();
        intent.putExtra( "record", blankScore );
        intent.addFlags( Intent.FLAG_ACTIVITY_NO_HISTORY );
        startActivity( intent );
    }
}