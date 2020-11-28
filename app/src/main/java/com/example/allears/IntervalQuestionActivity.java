package com.example.allears;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IntervalQuestionActivity extends AppCompatActivity {

    private TextView testText;
    private TextView testText2;

    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;

    private String difficulty;
    private Random rand;

    private int answer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_question);

        testText = (TextView)findViewById(R.id.text_interval_question_test);
        testText2 = (TextView)findViewById(R.id.text_interval_question_test2);

        rand = new Random();

        Bundle bundle = getIntent().getExtras();
        String diff = bundle.getString("difficulty");

        if (diff != null ) {
            difficulty = diff;
        }

        button0 = (Button)findViewById(R.id.button_interval_question_b0);
        button1 = (Button)findViewById(R.id.button_interval_question_b1);
        button2 = (Button)findViewById(R.id.button_interval_question_b2);
        button3 = (Button)findViewById(R.id.button_interval_question_b3);

        populateButtonText();

        testText.setText( "Difficulty selected was:\n" + difficulty );

        testText2.setText( getQuestionNotes().toString() );

    }

    private void populateButtonText() {
        switch( difficulty ) {
            case "easy":
                button0.setText( "Octave" );
                button1.setText( "5th" );
                button2.setText( "4th" );
                button3.setText( "Tritone" );
                break;

            case "medium":
                button0.setText( "Maj2" );
                button1.setText( "Maj3" );
                button2.setText( "Maj6" );
                button3.setText( "Maj7" );
                break;

            case "hard":
                button0.setText( "Min2" );
                button1.setText( "Min3" );
                button2.setText( "Min6" );
                button3.setText( "Min7" );
                break;
        }
    }


    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button_interval_question_back:
                finish();
                break;

            case R.id.button_interval_question_repeat:
                Toast.makeText( IntervalQuestionActivity.this, "Pressed play again", Toast.LENGTH_SHORT).show();
                break;

            case R.id.button_interval_question_new:
                openNewQuestionSameDifficulty();
                break;


                // pass to helper to check if answered correctly or not
            case R.id.button_interval_question_b0:
                answerCorrectHuh( 0 );
                break;

            case R.id.button_interval_question_b1:
                answerCorrectHuh( 1 );
                break;

            case R.id.button_interval_question_b2:
                answerCorrectHuh( 2 );
                break;

            case R.id.button_interval_question_b3:
                answerCorrectHuh( 3 );
                break;
        }
    }

    private void answerCorrectHuh( int button ) {
        if ( button == answer ) {
            Toast.makeText( IntervalQuestionActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
//            try {
//                Thread.sleep( 300 );
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            openNewQuestionSameDifficulty();
        } else {
            Toast.makeText( IntervalQuestionActivity.this, "Incorrect :((", Toast.LENGTH_SHORT).show();
        }
    }

    private void openNewQuestionSameDifficulty() {
        Intent intent = new Intent(this, IntervalQuestionActivity.class );
        intent.putExtra( "difficulty", difficulty );
        startActivity( intent );
    }


    private List<Integer> getQuestionNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();

        // so here we are going to determine the root, choose a random note in the middle two octaves
        // using 25 currently, to be pretty much 2 octaves
        int root = rand.nextInt(25) + 24;
        notes.add( root );

        switch ( difficulty ) {
            case "easy":
                formEasyQuestion( notes );
                break;

            case "medium":
                formMediumQuestion( notes );
                break;

            case "hard":
                formHardQuestion( notes );
                break;
        }

        return notes;
    }




    // defining easy as octave, or fifth, or fourth
    private void formEasyQuestion( List<Integer> notes ) {
        int root = notes.get(0);

        boolean ascendHuh = rand.nextBoolean();
        // 0 = octave, 1 = perf 5, 2 = perf 4, 3 = tritone
        int type = rand.nextInt(4);
        int toAdd;

        switch (type) {

            // 0, octave
            case 0:
                toAdd = (ascendHuh) ? root + 12 : root - 12;
                notes.add(toAdd);
                answer = 0;
                break;

            // 1, perfect fifth
            case 1:
                toAdd = (ascendHuh) ? root + 7 : root - 7;
                notes.add(toAdd);
                answer = 1;
                break;

            // 2, perfect fourth
            case 2:
                toAdd = (ascendHuh) ? root + 5 : root - 5;
                notes.add(toAdd);
                answer = 2;
                break;

            // 3, tritone
            case 3:
                toAdd = ( ascendHuh ) ? root + 6  :  root - 6 ;
                notes.add( toAdd );
                answer = 3;
                break;


        }
    }




    private void formMediumQuestion( List<Integer> notes ) {
        int root = notes.get(0);
        boolean ascendHuh = rand.nextBoolean();

        // 0 = maj2, 1 = maj3, 2 = maj6, 3 = maj7
        int type = rand.nextInt(4);

        int toAdd;
        switch (type) {

            // 0, maj2
            case 0:
                toAdd = ( ascendHuh ) ? root + 2  :  root - 2 ;
                notes.add( toAdd );
                answer = 0;
                break;

            // 1, maj3
            case 1:
                toAdd = ( ascendHuh ) ? root + 4  :  root - 4 ;
                notes.add( toAdd );
                answer = 1;
                break;

            // 2, maj6
            case 2:
                toAdd = ( ascendHuh ) ? root + 9  :  root - 9 ;
                notes.add( toAdd );
                answer = 2;
                break;

            // 3, maj7
            case 3:
                toAdd = ( ascendHuh ) ? root + 11  :  root - 11 ;
                notes.add( toAdd );
                answer = 3;
                break;
        }

    }



    private void formHardQuestion( List<Integer> notes ) {
        int root = notes.get(0);
        boolean ascendHuh = rand.nextBoolean();

        // 0 = min2, 1 = min3, 2 = min6, 3 = min7
        int type = rand.nextInt(4);

        int toAdd;
        switch (type) {

            // 0, min2
            case 0:
                toAdd = ( ascendHuh ) ? root + 1  :  root - 1 ;
                notes.add( toAdd );
                answer = 0;
                break;

            // 1, min3
            case 1:
                toAdd = ( ascendHuh ) ? root + 3  :  root - 3 ;
                notes.add( toAdd );
                answer = 1;
                break;

            // 2, min6
            case 2:
                toAdd = ( ascendHuh ) ? root + 8  :  root - 8 ;
                notes.add( toAdd );
                answer = 2;
                break;

            // 3, min7
            case 3:
                toAdd = ( ascendHuh ) ? root + 10  :  root - 10 ;
                notes.add( toAdd );
                answer = 3;
                break;
        }
    }
}