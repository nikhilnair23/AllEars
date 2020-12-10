package com.example.allears;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * A class to represent a ChordQuestion. it supports random generation of
 * chord questions of varying difficulties, and references an XML file
 * that has several buttons, allowing users to answer.
 */
public class ChordQuestionActivity extends AppCompatActivity {

    // placeholder text, shows numbers that question generation obtained
    // and shows what difficulty was obtained through the intent
    private TextView difficultySelected;
    private TextView score;

    // USER stuff
    private String loggedInUser;

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

    // the chord player to be used to make noises
    private ChordPlayer chordPlayer;

    // a flag for if the user ever guesses wrong, and an array to hold the users record
    private boolean guessedWrong;
    private ArrayList<Integer> record;
    private int numRight;

    // field for local database
    private DBHelper dbHelper;

    // fields for firebase
    private DatabaseReference mDatabase;
    private QuestionFirebaseHelper qFBH;
    private static final String TAG = ChordQuestionActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chord_question);

        // TODO TEMPORARY, these views are only to show what data is being transferred
        difficultySelected = (TextView)findViewById(R.id.text_chord_question_selected_difficulty);
        score = (TextView)findViewById(R.id.text_chord_question_score);

        // get user string to add
        // TODO how to pull from database
//        Cursor entries = dbHelper.getAllEntries();
//        if ( entries.getCount() > 0 ) {
//            entries.moveToFirst();
//            loggedInUser = entries.getString( 1 );
//        } else {
//            loggedInUser = "Guest";
//        }
        loggedInUser = "Guest";

        // create a random to be used in this
        rand = new Random();

        // get the difficulty from the intent, if not null set it
        Bundle bundle = getIntent().getExtras();
        String diff = bundle.getString("difficulty");
        if (diff != null ) {
            difficulty = diff;
        }
        record = bundle.getIntegerArrayList("record");
        numRight = 0;

        // find the play again button, style it a tiny bit
        playAgain = (Button)findViewById(R.id.button_chord_question_repeat);

        // get the buttons in the grid
        button0 = (Button)findViewById(R.id.button_chord_question_b0);
        button1 = (Button)findViewById(R.id.button_chord_question_b1);
        button2 = (Button)findViewById(R.id.button_chord_question_b2);
        button3 = (Button)findViewById(R.id.button_chord_question_b3);
        button4 = (Button)findViewById(R.id.button_chord_question_b4);
        button5 = (Button)findViewById(R.id.button_chord_question_b5);

        // call a helper to grey out certain buttons and assign one as the correct answer
        rigButtons();

        // call a helper to create a new question
        createNewQuestion();

        // TODO TEMPORARY see the data
        difficultySelected.setText( "Difficulty: " + difficulty );
        score.setText( record.toString() );

        // firebase stuff
        mDatabase = FirebaseDatabase.getInstance().getReference();
        qFBH = new QuestionFirebaseHelper();
        dbHelper = new DBHelper( this );

    }


    /**
     *
     */
    private void createNewQuestion() {

        // reset the guessed wrong flag
        guessedWrong = false;

        // get the question with a helper
        List<Integer> question = getQuestionNotes();
        Integer[] questionArray = questionAsArray( question );

        // create the chord player with the question
        chordPlayer = new ChordPlayer( this, questionArray );

        // call a handler to play the chord on a delay right after creating it
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chordPlayer.playChord();
            }
        }, 500);

    }

    /**
     * Return the question as an array
     * @param question
     * @return
     */
    private Integer[] questionAsArray( List<Integer> question ) {

        // initialize array, add everything from list to it
        Integer[] questionArray = new Integer[ question.size() ];

        for ( int i = 0; i < question.size(); i++ ) {
            questionArray[i] = question.get( i );
        }

        return questionArray;
    }



    private void addToRecordAndMakeNewQuestion( int numForRecord ) {
        record.add( numForRecord );
        if (numForRecord == 1) {
            numRight = numRight + 1;
        }
        score.setText( record.toString() );

        // check if you've compeleted 10
        finishSetIfCompletedTen();

        createNewQuestion();
    }


    private void finishSetIfCompletedTen() {

        // if it made it to 10, run the things
        if ( this.record.size() == 10 ) {
            // otherwise, add users score to firebase
            new Thread( new Runnable() {
                @Override
                public void run() {
                    // TODO get user from local database
                    qFBH.postRecordToFirebase( loggedInUser, difficulty, numRight );
                    // postRecordToFirebase();
                }
            }).start();

            // also increment your daily training count, to be able to check it against
            //   your daily goal
            // TODO increment daily training count
        }


    }


    /**
     * Rig up all the buttons, grey some depending on difficulty
     */
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

    // a helper method to grey out buttons
    private void greyOut( Button button ) {
        button.setTextColor(getResources().getColor( R.color.colorButtonTestTwo ));
        button.setBackgroundColor( getResources().getColor( R.color.colorButtonTestTwo) );
        button.setHighlightColor( getResources().getColor( R.color.colorButtonTestTwo) );
    }

    // grey the buttons associated with the medium difficulty
    private void greyMediumButtons() {
        greyOut( button2 );
        greyOut( button3 );
    }

    // grey the buttons associated with the hard difficulty
    private void greyHardButtons() {
        greyOut( button4 );
        greyOut( button5 );
    }


    // on click
    public void onClick(View view) {
        switch (view.getId()) {

            // back button, finish the activity
            case R.id.button_chord_question_back:
                finish();
                break;

            // repeat button, push a toast to user, use handler to repeat the chord
            case R.id.button_chord_question_repeat:
                Toast.makeText( ChordQuestionActivity.this, "Pressed play again", Toast.LENGTH_SHORT).show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        chordPlayer.playChord();
                    }
                }, 500);

                break;

            // new button, call the createNewQuestion and reset flags, with a handler for delay
            case R.id.button_chord_question_new:

                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Integer toAdd = (guessedWrong) ? 0 : 2 ;
                        addToRecordAndMakeNewQuestion( toAdd );

                    }
                }, 500);

                break;

            // cases for all the buttons
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

    // helper method to set the score from inside handlers
    private void setScoreText( String text ) {
        this.score.setText( text );
    }


    // check if the button is correct, perform proper action
    private void answerCorrectHuh( int button ) {
        if ( button == answer ) {
            Toast.makeText( ChordQuestionActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // add integer for correct/incorrect, add to record, and set text on top
                    Integer toAdd = (guessedWrong) ? 0 : 1 ;
                    addToRecordAndMakeNewQuestion( toAdd );
                }
            }, 500);
        } else {
            Toast.makeText( ChordQuestionActivity.this, "Incorrect :((", Toast.LENGTH_SHORT).show();
            this.guessedWrong = true;
        }
    }


    // runs a switch
    private List<Integer> getQuestionNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();

        // so here we are going to determine the root, choose a random note in the middle two octaves
        // using 25 currently, to be pretty much 2 octaves. Only let root be in first octave

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

    // only possibly generate a major triad, or a minor triad
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

    // only possibly generate a major 7th, or a minor 7th
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

    // only possibly generate a dom7, or a min7b5
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
        //   no default case, and this private method is only called from another private method
        //   so 'type' will always fall into one of these three cases, and this line will never
        //   be run
        return null;
    }

    private void postRecordToFirebase() {

        Date currentTime = Calendar.getInstance().getTime();
        String timeStamp = currentTime.toString();

        // TODO
        // need to get target username from the local database, add as second child
        //

        mDatabase
                .child( "Users" )
                .child( "intervalTest" )
                .child( "Scores" )
                .child( "Interval" )
                .child( difficulty )
                .child( timeStamp )
                .runTransaction( new Transaction.Handler() {

                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                        // if transaction succesfully works it goes in :
                        // Users -> [username] -> Scores -> Interval -> [difficulty]
                        //   and the key-value is [timestamp]-[record]
                        currentData.setValue( numRight );
                        return Transaction.success( currentData );
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError error, boolean committed,
                                           @Nullable DataSnapshot currentData) {

                        Log.d(TAG, "postTransaction:onComplete:" + error);
                        Log.d(TAG, "it's running this thing??");

                    }
                });
    }
}