package com.example.allears;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

// TODO: 11/28/20
// - make the screen look not absolutely horrible
//   - find way to have variable number of buttons? currently 4 per every screen
// - have some sort of delay after you correctly guess before opening new activity??
// - have it keep track of # right and # wrong for the day
//   - at each one, append a 1 for a correct, and 0 for incorrect, 2 for skip
//  -> now have it go into a new drawable type thing, draw blue for correct, red for incorrect
//     etc. also only parse at most the last 10? idk

// - really need to figure out something for the buttons, want a couple custom styles
//  -> because overriding them as grey adds the stupid full rectangle, no padding or anything


/**
 * A class to represent a question for IntervalTraining. supports creation of random
 * interval questions of varying difficulties, and ability to play them through the
 * IntervalPlayer class.
 */
public class IntervalQuestionActivity extends AppCompatActivity {

    // placeholder text, shows numbers that question generation obtained
    // and shows what difficulty was obtained through the intent
    private TextView difficultySelected;
    private TextView score;

    // the button on top used to repeat the sound
    private Button playAgain;

    // USER stuff
    private String loggedInUser;

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

    // the interval player to be used to generate sounds
    private IntervalPlayer intervalPlayer;

    // flags to know if player answered correctly, and a list to keep track of the record
    private boolean guessedWrong;
    private ArrayList<Integer> record;
    private int numRight;


    // field for local database
    private DBHelper dbHelper;

    // fields for firebase
    private DatabaseReference mDatabase;
    private QuestionFirebaseHelper qFBH;
    private static final String TAG = IntervalQuestionActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_question);

        dbHelper = new DBHelper(this);

        // TODO TEMPORARY, these views are only to show what data is being transferred
        difficultySelected = (TextView)findViewById(R.id.text_interval_question_selected_difficulty);
        score = (TextView)findViewById(R.id.text_interval_question_score);

        // TODO how to pull from database
        // get user string to add
        Cursor entries = dbHelper.getAllEntries();
        if ( entries.getCount() > 0 ) {
            entries.moveToFirst();
            loggedInUser = entries.getString( 1 );
        } else {
            loggedInUser = "Guest";
        }
//        loggedInUser = "Guest";

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

        // call a helper to generate a question, rig up the IntervalPlayer, and play the sound
        createNewQuestion();

        // TODO TEMPORARY: set the views to show what data has been transferred
        difficultySelected.setText( "Difficulty: " + difficulty );
        score.setText( record.toString() );

        // firebase stuff
        mDatabase = FirebaseDatabase.getInstance().getReference();
        qFBH = new QuestionFirebaseHelper();
        dbHelper = new DBHelper( this );

    }




    private void createNewQuestion() {

        // set flag back to false
        guessedWrong = false;

        // generate a new question, and use this in the interval player
        List<Integer> question = getQuestionNotes();
        intervalPlayer = new IntervalPlayer( this, question.get( 0 ), question.get( 1 ));

        // call a handler to play the sound on a delay
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                intervalPlayer.playInterval( 1000 );
            }
        }, 500);
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
                    qFBH.postRecordToFirebase( loggedInUser, difficulty, numRight );
                    // postRecordToFirebase();
                }
            }).start();

            // also increment your daily training count, to be able to check it against
            //   your daily goal
            // TODO increment daily training count

            // start a new finish activity
            openQuestionFinishActivity();
        }


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



    // on click method
    public void onClick(View view) {
        switch (view.getId()) {

            // back case, close this activity
            case R.id.button_interval_question_back:
                finish();
                break;


            // repeat case, repeat the question sound
            case R.id.button_interval_question_repeat:

                // push toast to the user
                Toast.makeText( IntervalQuestionActivity.this, "Pressed play again", Toast.LENGTH_SHORT).show();

                // use handler to repeat the question
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        intervalPlayer.playInterval( 1000 );
                    }
                }, 500);
                break;


            // a case for the new question button
            case R.id.button_interval_question_new:

                // creating new question
                Toast.makeText( IntervalQuestionActivity.this, "Creating New!", Toast.LENGTH_SHORT).show();

                // use handler to delay creation of new question
                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Integer toAdd = (guessedWrong) ? 0 : 2 ;
                        addToRecordAndMakeNewQuestion( toAdd );
                    }
                }, 500);

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

        // if the user answered correct
        if ( button == answer ) {

            // send a toast to the user
            Toast.makeText( IntervalQuestionActivity.this, "Correct!", Toast.LENGTH_SHORT).show();

            // use a handler to delay the creation of a new question
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // add integer for correct/incorrect, add to record, and set text on top
                    Integer toAdd = (guessedWrong) ? 0 : 1 ;
                    addToRecordAndMakeNewQuestion( toAdd );

//                    record.add( toAdd );
//                    setScoreText( record.toString() );
//                    createNewQuestion();
                }
            }, 500);

            // if not, push incorrect, and flag that they have guessed wrong
        } else {
            Toast.makeText( IntervalQuestionActivity.this, "Incorrect :((", Toast.LENGTH_SHORT).show();
            this.guessedWrong = true;
        }
    }



    // runs a switch
    private List<Integer> getQuestionNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();

        // so here we are going to determine the root, choose a random note in the middle two octaves
        // using 25 currently, to be pretty much 2 octaves, bound it within 1st octave for root.
        int root = rand.nextInt(14);
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

        // TODO add support for possibly 3 octaves, and up and down
        int root = notes.get( 0 );

        // only going up
        boolean ascendHuh = true;
        // boolean ascendHuh = rand.nextBoolean();

        // bound lets this be contained to this one small method
        int boundedVal = possibilities.get( rand.nextInt( bound ));
        int toAdd = ( ascendHuh ) ? boundedVal : - boundedVal ;
        notes.add( toAdd );
        answer = boundedVal - 1;

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


    private void openQuestionFinishActivity() {
        Intent intent = new Intent( this, QuestionFinishActivity.class );
        intent.putExtra( "type", "Interval" );
        intent.putExtra( "difficulty", difficulty );
        intent.putExtra( "numCorrect", numRight );
        intent.addFlags( Intent.FLAG_ACTIVITY_NO_HISTORY );
        startActivity( intent );
    }

}