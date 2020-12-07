package com.example.allears;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
    private DatabaseReference usersRef;
    private Query query;
    private Set<String> users;
    private static final String TAG = LoginActivity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

    }
}
