package com.example.allears;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChordQuestionActivity extends AppCompatActivity {

    // placeholder text, shows numbers that question generation obtained
    // and shows what difficulty was obtained through the intent
    private TextView difficultySelected;
    private TextView score;

    // the button on top used to repeat the sound
    private Button playAgain;

    // the grid of buttons for use in answers
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;

    // string obtained through intent from IntervalLanding activity, drives question generation
    private String difficulty;

    // a random to use in creation of questions
    private Random rand;

    // determined on question generation. Determines functionality of buttons.
    private int answer;

    // TODO should be a chord player instead
    private ChordPlayer chordPlayer;

    private boolean guessedWrong;
    private ArrayList<Integer> record;


    // TODO temp for debugging and making sure functional
    private String chordAsText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chord_question);

        // TEMPORARY, these views are only to show what data is being transferred
        difficultySelected = (TextView)findViewById(R.id.text_chord_question_selected_difficulty);
        score = (TextView)findViewById(R.id.text_chord_question_score);

        // create a random to be used in this
        rand = new Random();

        // get the difficulty from the intent, if not null set it
        Bundle bundle = getIntent().getExtras();
        String diff = bundle.getString("difficulty");
        if (diff != null ) {
            difficulty = diff;
        }
        record = bundle.getIntegerArrayList("record");

        // find the play again button, style it a tiny bit
        playAgain = (Button)findViewById(R.id.button_chord_question_repeat);

        // get the buttons in the grid
        button0 = (Button)findViewById(R.id.button_chord_question_b0);
        button1 = (Button)findViewById(R.id.button_chord_question_b1);
        button2 = (Button)findViewById(R.id.button_chord_question_b2);
        button3 = (Button)findViewById(R.id.button_chord_question_b3);
        button4 = (Button)findViewById(R.id.button_chord_question_b4);
        button5 = (Button)findViewById(R.id.button_chord_question_b5);

        // set guessed wrong to false intially
        guessedWrong = false;

        // call a helper to grey out certain buttons and assign one as the correct answer
        rigButtons();

        // TODO
        // get the question and use it in a chord player
        List<Integer> question = getQuestionNotes();
        Integer[] questionArray = questionAsArray( question );

        chordPlayer = new ChordPlayer( this, questionArray );
        chordPlayer.playChord();

        // TODO use this in a chord player, for now display as text
        chordAsText = question.toString();


        // TEMPORARY see the data
        // TODO remove the seeing difficulty possibly, remove question as string for sure
        difficultySelected.setText( "Difficulty: " + difficulty + "\nQ:" + chordAsText );
        score.setText( record.toString() );

    }

    private Integer[] questionAsArray( List<Integer> question ) {

        //Integer[] questionArray = new Integer[ question.size() - 1 ];
        Integer[] questionArray = new Integer[ question.size() ];


        for ( int i = 0; i < question.size(); i++ ) {
            questionArray[i] = question.get( i );
        }

        return questionArray;
    }

    // TODO
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

    private void greyOut( Button button ) {
        // button.setBackgroundColor(Color.argb(100, 255, 255, 255));
        button.setTextColor(getResources().getColor( R.color.colorButtonTestTwo ));
        button.setBackgroundColor( getResources().getColor( R.color.colorButtonTestTwo) );
        button.setHighlightColor( getResources().getColor( R.color.colorButtonTestTwo) );
        // button.setVisibility( View.INVISIBLE );
    }

    private void greyMediumButtons() {
        greyOut( button2 );
        greyOut( button3 );
    }

    private void greyHardButtons() {
        greyOut( button4 );
        greyOut( button5 );
    }





    // a helper to run when the player answers correctly, or starts a new question
    private void openNewQuestionSameDifficulty( Boolean pressedNextHuh ) {
        Intent intent = new Intent(this, ChordQuestionActivity.class );
        intent.putExtra( "difficulty", difficulty );

        Integer toAdd = (guessedWrong) ? 0 : 1 ;
        if (pressedNextHuh) {
            toAdd = 2;
        }
        record.add( toAdd );
        intent.putExtra( "record", record );

        intent.addFlags( Intent.FLAG_ACTIVITY_NO_HISTORY );
        startActivity( intent );
    }



    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button_chord_question_back:
                finish();
                break;

            case R.id.button_chord_question_repeat:
                Toast.makeText( ChordQuestionActivity.this, "Pressed play again", Toast.LENGTH_SHORT).show();
                chordPlayer.playChord();
                break;

            case R.id.button_chord_question_new:
                openNewQuestionSameDifficulty( true );
                break;

            case R.id.button_chord_question_b0:
                answerCorrectHuh( 0 );
                break;

            case R.id.button_chord_question_b1:
                answerCorrectHuh( 1 );
                break;

            case R.id.button_chord_question_b2:
                answerCorrectHuh( 2 );
                break;

            case R.id.button_chord_question_b3:
                answerCorrectHuh( 3 );
                break;

            case R.id.button_chord_question_b4:
                answerCorrectHuh( 4 );
                break;

            case R.id.button_chord_question_b5:
                answerCorrectHuh( 5 );
                break;

        }
    }


    // check if the button is correct, perform proper action
    private void answerCorrectHuh( int button ) {
        if ( button == answer ) {
            Toast.makeText( ChordQuestionActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
//            try {
//                Thread.sleep( 300 );
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            openNewQuestionSameDifficulty( false );
        } else {
            Toast.makeText( ChordQuestionActivity.this, "Incorrect :((", Toast.LENGTH_SHORT).show();
            this.guessedWrong = true;
        }
    }


    // runs a switch
    private List<Integer> getQuestionNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();

        // so here we are going to determine the root, choose a random note in the middle two octaves
        // using 25 currently, to be pretty much 2 octaves

        //int root = rand.nextInt(25) + 24;
        // TODO hard coding this right now, need to add more notes?
        int root = rand.nextInt(14);
        notes.add( root );

        switch ( difficulty ) {
            case "easy":
                generateQuestionWithType( notes, 0 );
                break;
            case "medium":
                generateQuestionWithType( notes, rand.nextInt( 2 ) );
                break;
            case "hard":
                generateQuestionWithType( notes, rand.nextInt( 3 ) );
                break;
        }
        return notes;
    }

    private List<Integer> generateEasyQuestion( ArrayList<Integer> notes ) {
        int root = notes.get( 0 );
        boolean majHuh = rand.nextBoolean();

        if (majHuh) {
            // add maj3
            notes.add( root + 4 );
            answer = 0;
        } else {
            // add b3
            notes.add( root + 3 );
            answer = 1;
        }
        // add 5th regardless
        notes.add( root + 7 );

        return notes;
    }

    private List<Integer> generateMediumQuestion( ArrayList<Integer> notes ) {
        int root = notes.get( 0 );
        boolean majHuh = rand.nextBoolean();

        if (majHuh) {
            // add maj3, maj7
            notes.add( root + 4 );
            notes.add( root + 11 );
            answer = 2;
        } else {
            // add b3, b7
            notes.add( root + 3 );
            notes.add( root + 10 );
            answer = 3;
        }

        // add the 5th regardless
        notes.add( root + 7 );

        return notes;
    }

    private List<Integer> generateHardQuestion( ArrayList<Integer> notes ) {
        int root = notes.get( 0 );
        boolean domHuh = rand.nextBoolean();

        // add maj3, p5
        if ( domHuh ) {
            notes.add( root + 4 );
            notes.add( root + 7 );
            answer = 4;
        } else {
            // add min3, b5
            notes.add( root + 3 );
            notes.add( root + 6 );
            answer = 5;
        }
        // add the b7 in both cases
        notes.add( root + 10 );


        return notes;
    }

    private List<Integer> generateQuestionWithType( ArrayList<Integer> notes, int type ) {
        switch (type) {
            case 0:
                return generateEasyQuestion(notes);
            case 1:
                return generateMediumQuestion(notes);
            case 2:
                return generateHardQuestion(notes);
        }

        // this will never be run, possibly bad design, sprinting through getting this functional rn
        return null;
    }
}