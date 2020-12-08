package com.example.allears;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private TextView loggedInUserText;
    private Button loginButton;
    private Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.main_login_button);
        signOutButton = findViewById(R.id.main_sign_out_button);
        dbHelper = new DBHelper(this);
        loggedInUserText = findViewById(R.id.logged_in_user_text);
        checkUserSignedIn();
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.chord_training_button:
                openActivityChordLanding();
                break;

            case R.id.interval_training_button:
                openActivityIntervalLanding();
                break;

            case R.id.main_login_button:
                openLoginActivity();
                break;

            case R.id.main_sign_out_button:
                signOut();
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

    private void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity( intent );
    }

    /**
     * Function to check if the user is already signed in to display their name at the top as
     * well as replace the login button with the sign out button
     */
    private void checkUserSignedIn(){
        Cursor entries = dbHelper.getAllEntries();
        if (entries.getCount() > 0){
            entries.moveToFirst();
            loggedInUserText.setVisibility(View.VISIBLE);
            loggedInUserText.setText(entries.getString(1));
            loginButton.setVisibility(View.INVISIBLE);
            signOutButton.setVisibility(View.VISIBLE);
        }
    }

    private void signOut(){
        dbHelper.truncateTable();
        loggedInUserText.setVisibility(View.INVISIBLE);
        loginButton.setVisibility(View.VISIBLE);
        signOutButton.setVisibility(View.INVISIBLE);
    }



}