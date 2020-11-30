package com.example.allears;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class IntervalLandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_landing);
    }


    public void onClick(View view){
        switch (view.getId()) {
            case R.id.button_interval_landing_back:
                finish();
                break;

            case R.id.button_interval_landing_easy:
                openQuestionDifficulty( "easy" );
                break;

            case R.id.button_interval_landing_medium:
                openQuestionDifficulty( "medium" );
                break;

            case R.id.button_interval_landing_hard:
                openQuestionDifficulty( "hard" );
                break;
        }
    }


    private void openQuestionDifficulty( String difficulty ) {
        Intent intent = new Intent(this, IntervalQuestionActivity.class );
        intent.putExtra( "difficulty", difficulty );
        ArrayList<Integer> blankScore = new ArrayList<Integer>();
        intent.putExtra( "record", blankScore );
        intent.addFlags( Intent.FLAG_ACTIVITY_NO_HISTORY );
        startActivity( intent );
    }

}
