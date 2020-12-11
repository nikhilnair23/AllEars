package com.example.allears;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class QuestionFinishActivity extends AppCompatActivity {

    // Android Elements
    private TextView typeText;
    private TextView difficultyText;
    private TextView numCorrectText;

    private Button backButton;
    private Button repeatButton;


    // variables for this activity
    private String type;
    private String difficulty;
    private int numCorrect;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_finish);

        // pull parameters from intents
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString( "type" );
        difficulty = bundle.getString( "difficulty" );
        numCorrect = bundle.getInt( "numCorrect" );

        typeText = findViewById( R.id.text_question_finish_type);
        difficultyText = findViewById( R.id.text_question_finish_difficulty);
        numCorrectText = findViewById( R.id.text_question_finish_number_correct);

        backButton = findViewById( R.id.button_question_finish_back );
        repeatButton = findViewById( R.id.button_question_finish_again );

        // call helper to adjust text shown on the boxes
        setShownText();

    }

    private void setShownText() {
        typeText.setText( String.format( "Type: %s", type ));
        difficultyText.setText( String.format( "Difficulty: %s", difficulty ));
        numCorrectText.setText( String.format( "Correct: %s of 10", numCorrect ));
    }


    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.button_question_finish_back:
                finish();
                break;


            case R.id.button_question_finish_again:
                if (this.type.equals( "Interval" )) {
                    startIntervalQuestionActivity();
                } else if ( this.type.equals( "Chord" )) {
                    startChordQuestionActivity();
                }
                break;
        }

    }


    private void startIntervalQuestionActivity() {
        Intent intent = new Intent(this, IntervalQuestionActivity.class );
        intent.putExtra( "difficulty", difficulty );
        ArrayList<Integer> blankScore = new ArrayList<Integer>();
        intent.putExtra( "record", blankScore );
        intent.addFlags( Intent.FLAG_ACTIVITY_NO_HISTORY );
        startActivity( intent );
    }

    private void startChordQuestionActivity() {
        Intent intent = new Intent(this, ChordQuestionActivity.class );
        intent.putExtra( "difficulty", difficulty );
        ArrayList<Integer> blankScore = new ArrayList<Integer>();
        intent.putExtra( "record", blankScore );
        intent.addFlags( Intent.FLAG_ACTIVITY_NO_HISTORY );
        startActivity( intent );
    }
}