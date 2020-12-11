package com.example.allears;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.Calendar;
import java.util.Date;


public class QuestionFirebaseHelper {

    private DatabaseReference mDatabase;
    private static final String TAG = QuestionFirebaseHelper.class.getSimpleName();


    public QuestionFirebaseHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }



    public void postRecordToFirebase( String user, String difficulty, int num ) {

        // if user is guest, don't post to firebase
        //   otherwise guest would have the godlike stats
        if ( user.equals( "Guest" ) ) {
            return;
        }

        final int numRight = num;
        Date currentTime = Calendar.getInstance().getTime();
        String timeStamp = currentTime.toString();

        mDatabase
                .child( "Users" )
                // TODO make this first string 'user'
                .child( user )
                .child( "Scores" )
                .child( "Interval" )
                .child( difficulty )
                .child( timeStamp )
                .runTransaction( new Transaction.Handler() {

                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                        // if transaction successfully works it goes in :
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
