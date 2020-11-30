package com.example.allears;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.media.AudioManager;
import android.media.SoundPool;

// TODO: 11/28/20
// - figure out backstack better, when you answer a question or close just nuke it from the stack??
// - figure out playing all the sounds, integers -> sounds
// - make the screen look not absolutely horrible
//   - find way to have variable number of buttons? currently 4 per every screen
// - have some sort of delay after you correctly guess before opening new activity??

public class IntervalQuestionActivity extends AppCompatActivity {

    // placeholder text, shows numbers that question generation obtained
    // and shows what difficulty was obtained through the intent
    private TextView testText;
    private TextView testText2;

    // the button on top used to repeat the sound
    private Button playAgain;

    // the grid of buttons for use in answers
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button button10;
    private Button button11;

    // string obtained through intent from IntervalLanding activity, drives question generation
    private String difficulty;

    // a random to use in creation of questions
    private Random rand;

    // determined on question generation. Determines functionality of buttons.
    private int answer;

    private IntervalPlayer intervalPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_question);

        // TEMPORARY, these views are only to show what data is being transferred
        testText = (TextView)findViewById(R.id.text_interval_question_test);
        testText2 = (TextView)findViewById(R.id.text_interval_question_test2);

        // testing interval player (this should be created when question is made)
        intervalPlayer = new IntervalPlayer(this, 0, 1);

        // create a random to be used in this
        rand = new Random();

        // get the difficulty from the intent, if not null set it
        Bundle bundle = getIntent().getExtras();
        String diff = bundle.getString("difficulty");
        if (diff != null ) {
            difficulty = diff;
        }

        // find the play again button, style it a tiny bit
        playAgain = (Button)findViewById(R.id.button_interval_question_repeat);

        // find all of the buttons in the grid for use
        button0 = (Button)findViewById(R.id.button_interval_question_b0);
        button1 = (Button)findViewById(R.id.button_interval_question_b1);
        button2 = (Button)findViewById(R.id.button_interval_question_b2);
        button3 = (Button)findViewById(R.id.button_interval_question_b3);
        button4 = (Button)findViewById(R.id.button_interval_question_b4);
        button5 = (Button)findViewById(R.id.button_interval_question_b5);
        button6 = (Button)findViewById(R.id.button_interval_question_b6);
        button7 = (Button)findViewById(R.id.button_interval_question_b7);
        button8 = (Button)findViewById(R.id.button_interval_question_b8);
        button9 = (Button)findViewById(R.id.button_interval_question_b9);
        button10 = (Button)findViewById(R.id.button_interval_question_b10);
        button11 = (Button)findViewById(R.id.button_interval_question_b11);

        // call a helper to grey out certain buttons and assign one as the correct answer
        rigButtons();
        List<Integer> question = getQuestionNotes();

        // TEMPORARY: set the views to show what data has been transferred
        testText.setText( "Difficulty selected was:\n" + difficulty );
        testText2.setText( question.toString() );


    }


    // a private helper method to grey out certain buttons based on difficulty, and to generate
    //   a question and assign one button as the correct answer
    private void rigButtons() {

        // switch for difficulty
        switch( difficulty ) {
            case "easy":
                greyMediumButtons();
                greyHardButtons();
                break;

            case "medium":
                greyHardButtons();
                break;

            case "hard":
                break;
        }
    }



    // call helper on the buttons related to "medium" difficulty
    private void greyMediumButtons() {
        greyOut( button2 );
        greyOut( button5 );
        greyOut( button8 );
        greyOut( button10 );
        greyOut( button11 );
    }

    // call helper on the buttons related to "hard" difficulty
    private void greyHardButtons() {
        greyOut( button0 );
        greyOut( button7 );
        greyOut( button9 );
    }

    // change background color and color of text
    private void greyOut( Button button ) {
        // button.setBackgroundColor(Color.argb(100, 255, 255, 255));
        button.setTextColor(getResources().getColor( R.color.colorButtonTestTwo ));
        button.setBackgroundColor( getResources().getColor( R.color.colorButtonTestTwo) );
        button.setHighlightColor( getResources().getColor( R.color.colorButtonTestTwo) );
        // button.setVisibility( View.INVISIBLE );
    }



    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button_interval_question_back:
                finish();
                break;

            case R.id.button_interval_question_repeat:
                Toast.makeText( IntervalQuestionActivity.this, "Pressed play again", Toast.LENGTH_SHORT).show();
                //intervalPlayer.playInterval(1000);
                break;

            case R.id.button_interval_question_new:
                openNewQuestionSameDifficulty();
                break;


                // pass to helper to check if answered correctly or not??
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
            case R.id.button_interval_question_b4:
                answerCorrectHuh( 4 );
                break;
            case R.id.button_interval_question_b5:
                answerCorrectHuh( 5 );
                break;
            case R.id.button_interval_question_b6:
                answerCorrectHuh( 6 );
                break;
            case R.id.button_interval_question_b7:
                answerCorrectHuh( 7 );
                break;
            case R.id.button_interval_question_b8:
                answerCorrectHuh( 8 );
                break;
            case R.id.button_interval_question_b9:
                answerCorrectHuh( 9 );
                break;
            case R.id.button_interval_question_b10:
                answerCorrectHuh( 10 );
                break;
            case R.id.button_interval_question_b11:
                answerCorrectHuh( 11 );
                break;
        }
    }


    // check if the button is correct, perform proper action
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


    // a helper to run when the player answers correctly, or starts a new question
    private void openNewQuestionSameDifficulty() {
        Intent intent = new Intent(this, IntervalQuestionActivity.class );
        intent.putExtra( "difficulty", difficulty );
        startActivity( intent );
    }


    // runs a switch
    private List<Integer> getQuestionNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();

        // so here we are going to determine the root, choose a random note in the middle two octaves
        // using 25 currently, to be pretty much 2 octaves
        int root = rand.nextInt(25) + 24;
        notes.add( root );

        switch ( difficulty ) {
            case "easy":
                generateQuestionGivenBound( notes, 4 );
                break;
            case "medium":
                generateQuestionGivenBound( notes, 9 );
                break;
            case "hard":
                generateQuestionGivenBound( notes, 12 );
                break;
        }
        return notes;
    }



    // generates an interval by placing possibilities in an array and indexing a random one
    //   based on difficulty
    private void generateQuestionGivenBound( ArrayList<Integer> notes, int bound ) {

        // all of the possible values in order of difficulty, by index:
        //   easy   -> (0, 4) ; medium -> (0, 9) ; hard -> (0, 12)
        ArrayList<Integer> possibilities = new ArrayList<>(
                Arrays.asList( 2, 4, 5, 7, 3, 6, 9, 11, 12, 1, 8, 10 ));

        int root = notes.get( 0 );
        boolean ascendHuh = rand.nextBoolean();

        int boundedVal = possibilities.get( rand.nextInt( bound ));
        int toAdd = ( ascendHuh ) ? root + boundedVal : root - boundedVal ;
        notes.add( toAdd );
        answer = boundedVal - 1;

    }

}