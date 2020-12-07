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
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Set;


public class SignUpActivity extends AppCompatActivity {
    private EditText usernameText;
    private EditText passwordText;
    private DatabaseReference mDatabase;
    private DatabaseReference usersRef;
    private Set<String> users;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        usernameText = findViewById(R.id.username_text);
        passwordText = findViewById(R.id.password_text);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        usersRef = mDatabase.child("Users");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users = new HashSet<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String username = ds.child("username").getValue(String.class);
                    users.add(username);
                }
                System.out.println(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        usersRef.addListenerForSingleValueEvent(valueEventListener);



    }


    // adds the user to the database
    private void addUserToDatabase( DatabaseReference postRef) {

        // check username, password cannot be null and may not have spaces
        final String usernameChecked = checkNullString(usernameText.getText().toString());
        final String password = checkNullString(passwordText.getText().toString());

        if (usernameChecked == null || password == null) {
            Toast.makeText(this, "Entries may not be null, and may not have spaces!", Toast.LENGTH_LONG).show();
            return;
        }

        if (users.contains(usernameChecked)){
            Toast.makeText(this, "Username is already taken", Toast.LENGTH_LONG).show();
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
