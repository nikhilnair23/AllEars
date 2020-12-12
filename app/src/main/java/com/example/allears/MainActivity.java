package com.example.allears;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static DBHelper dbHelper;
    private TextView loggedInUserText;
    private Button loginButton;
    private Button signOutButton;
    private Button settingsButton;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private final Context context = this;
    private static GlobalClass globalClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.main_login_button);
        signOutButton = findViewById(R.id.main_sign_out_button);
        dbHelper = new DBHelper(this);
        loggedInUserText = findViewById(R.id.logged_in_user_text);
        settingsButton = findViewById(R.id.main_settings_button);
        globalClass = (GlobalClass) getApplicationContext();
        createNotificationChannel();

        // Initializing current number of sets answered as 0
        dbHelper.truncateGoalTable();
        dbHelper.insertToGoalDB(0);

        //Initialize default goal to 1
        globalClass.setGoal(1);
        checkUserSignedIn();
        createAlarm();
    }

    public void onClick(View view) {
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

            case R.id.main_settings_button:
                openSettingsActivity();
                break;

            case R.id.main_stats_button:
                openStatsActivity();
                break;
        }
    }


    private void openActivityIntervalLanding() {
        Intent intent = new Intent(this, IntervalLandingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private void openActivityChordLanding() {
        Intent intent = new Intent(this, ChordLandingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openStatsActivity() {
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }

    /**
     * Function to check if the user is already signed in to display their name at the top as
     * well as replace the login button with the sign out button
     */
    private void checkUserSignedIn() {
        Cursor entries = dbHelper.getAllUserEntries();
        if (entries.getCount() > 0) {
            entries.moveToFirst();
            loggedInUserText.setVisibility(View.VISIBLE);
            loggedInUserText.setText(entries.getString(1));
            loginButton.setVisibility(View.INVISIBLE);
            signOutButton.setVisibility(View.VISIBLE);
        }
    }

    private void signOut() {
        dbHelper.truncateTable();
        loggedInUserText.setVisibility(View.INVISIBLE);
        loginButton.setVisibility(View.VISIBLE);
        signOutButton.setVisibility(View.INVISIBLE);
    }

    private void createAlarm() {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Set the alarm to start at 21:32 PM
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // setRepeating() lets you specify a precise custom interval--in this case,
        // 1 day
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        // Interval used for testing
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                1 * 60 * 1000, alarmIntent);
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        private Intent intent;


        @Override
        public void onReceive(Context context, Intent intent) {


            if (!checkIfReachedGoal()) {
                // Intent to open up on clicking the notification
                intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                // the special pending intent
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);

                //  Builiding out the notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                        .setSmallIcon(R.drawable.ic_baseline_hearing_24)
                        .setContentTitle("Reach your goal today!")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("You haven't met your goal for today. Click here to get back on track"))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            // notificationId is a unique int for each notification that you must define
                notificationManager.notify(1, builder.build());
            }
            // Resetting count of question sets the user has answered
            dbHelper.truncateGoalTable();
            dbHelper.insertToGoalDB(0);
        }

        // Checks if the question number stored in the database is greater than or equal to the goal the user set
        public boolean checkIfReachedGoal() {
            Cursor entries = dbHelper.getGoalEntries();
            if (entries.getCount() > 0) {
                entries.moveToFirst();
                // Getting current questions
                Integer num = entries.getInt(0);
                Integer goal = globalClass.getGoal();
                if (num >= goal) {
                    return true;
                }
            }
            return false;
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NOTIFICATION_CHANNEL";
            String description = "Channel for notifications?";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }




}