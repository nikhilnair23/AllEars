package com.example.allears;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class IntervalQuestionActivity extends AppCompatActivity {

    private TextView testText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_question);

        testText = (TextView)findViewById(R.id.text_interval_question_test);

        Bundle bundle = getIntent().getExtras();
        String diff = bundle.getString("difficulty");

        if (diff == null) {
            testText.setText( "was null!");
        } else {
            testText.setText( diff );
        }

    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_interval_question_back:
                finish();
                break;
        }
    }
}