package com.example.allears;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private Query query;
    private Set<String> users;
    private EditText usernameText;
    private EditText passwordText;
    private DBHelper dbHelper;
    private static final String TAG = LoginActivity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameText = findViewById(R.id.login_screen_username_text);
        passwordText = findViewById(R.id.login_screen_password_text);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
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
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.login_screen_sign_up_button:
                openSignUpActivity();
                break;
            case R.id.login_screen_login_button:
                login();
                break;
        }
    }

    private void openSignUpActivity(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void login(){
        final String username = checkNullString(usernameText.getText().toString());
        final String password = checkNullString(passwordText.getText().toString());
        if (username == null || password == null){
            Toast.makeText(this, "Entries may not be null, and may not have spaces!", Toast.LENGTH_LONG).show();
            return;
        }

        query = mDatabase.child(username);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        User userObj = dataSnapshot.getValue(User.class);
                        if(userObj.getUsername().equals(username)){
                            if(!password.equals(userObj.getPassword())){
                                Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_LONG).show();
                            }
                            else{
                                // TODO: save username to localDB
                                dbHelper.inserttoDB(username);
                                finish();
                            }
                            return;
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Username/Password doesn't match", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error fetching data", Toast.LENGTH_LONG).show();
                return;
            }
        });

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
