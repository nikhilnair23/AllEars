package com.example.allears;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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


public class SignUpActivity extends AppCompatActivity {
    private EditText usernameText;
    private EditText passwordText;
    private DatabaseReference mDatabase;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        usernameText = findViewById(R.id.username_text);
        passwordText = findViewById(R.id.password_text);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    // adds the user to the database
    private void addUserToDatabase( DatabaseReference postRef) {

        // check username, password cannot be null and may not have spaces
        final String usernameChecked = checkNullString(usernameText.getText().toString());
        final String password = checkNullString(passwordText.getText().toString());

        if (usernameChecked == null) {
            Toast.makeText(this, "Entries may not be null, and may not have spaces!", Toast.LENGTH_LONG).show();
            return;
        }

        // initialize data for SentCount
        postRef
                .child("Users")
                .child(usernameChecked)
                .runTransaction(new Transaction.Handler() {

                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                        currentData.setValue(password);
                        return Transaction.success(currentData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError error, boolean committed,
                                           @Nullable DataSnapshot currentData) {
                        Log.d(TAG, "postTransaction:onComplete:" + currentData);
                        Toast.makeText(getApplicationContext(), "Sign up successful!", Toast.LENGTH_LONG);
                        finish();
                    }
                });
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.sign_up_button:
                addUserToDatabase(mDatabase);
                break;
        }
    }




    private String checkNullString(String text) {

        // if it contains a space, return null so that the program can throw a toast to user
        if ( text.contains( " " )) {
            return null;
        }

        // otherwise, we're totally good
        return text;
    }

}
