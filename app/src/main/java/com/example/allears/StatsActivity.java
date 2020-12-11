package com.example.allears;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatsActivity extends AppCompatActivity {
    private ArrayList<Score> itemList;
    private RecyclerView recyclerView;
    private RvAdapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private DatabaseReference mDatabase;
    private DatabaseReference scoreRef;
    private String trainingType;
    private String difficulty;
    private Spinner trainingSpinner;
    private Spinner difficultySpinner;
    private ArrayAdapter<String> trainingSpinnerAdapter;
    private ArrayAdapter<String> difficultySpinnerAdapter;

    private static final String INTERVAL_TRAINING = "Interval";
    private static final String CHORD_TRAINING = "Chord";
    private static final String EASY_DIFFICULTY = "easy";
    private static final String MEDIUM_DIFFICULTY = "medium";
    private static final String HARD_DIFFICULTY = "hard";

    final Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Stats");
        createItemList();
        createRecyclerView();
        trainingType = INTERVAL_TRAINING;
        difficulty = EASY_DIFFICULTY;
        trainingSpinner = findViewById(R.id.training_spinner);
        difficultySpinner = findViewById(R.id.difficulty_spinner);
        getScores(trainingType, difficulty);
        populateSpinners();
        setSpinnerListeners();
    }

    private void getScores(String trainingType, String difficulty){
        scoreRef = mDatabase.child(trainingType).child(difficulty);
        scoreRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String username = dataSnapshot.getKey();
                        Integer score = dataSnapshot.getValue(Integer.class);
                        Score scoreObj = new Score(username, score);
                        addItem(scoreObj);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error fetching data", Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    public void createItemList(){
        itemList = new ArrayList<>();
    }

    //TODO: Sort the list as you add or maybe sort it at the end.
    public void addItem(Score score){
        itemList.add(score);
        rAdapter.notifyDataSetChanged();
    }

    public void createRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        rLayoutManger = new LinearLayoutManager(this);
        rAdapter = new RvAdapter(itemList);
        recyclerView.setAdapter(rAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.stats_back_button:
                goToMainMenu();
                break;
        }
    }

    private void populateSpinners(){
        List<String> trainingList = new ArrayList<String>(
                Arrays.asList(INTERVAL_TRAINING, CHORD_TRAINING));
        List<String> difficultyList = new ArrayList<>(
                Arrays.asList(EASY_DIFFICULTY, MEDIUM_DIFFICULTY, HARD_DIFFICULTY));
        trainingSpinnerAdapter = new ArrayAdapter<>(StatsActivity.this,android.R.layout.simple_spinner_item, trainingList);
        trainingSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        trainingSpinner.setAdapter(trainingSpinnerAdapter);

        difficultySpinnerAdapter = new ArrayAdapter<>(StatsActivity.this,android.R.layout.simple_spinner_item, difficultyList);
        difficultySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        difficultySpinner.setAdapter(difficultySpinnerAdapter);
    }

    private void setSpinnerListeners(){

        trainingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String newTrainingType =  trainingSpinner.getItemAtPosition(position).toString();
                if(!trainingType.equals(newTrainingType)){
                    trainingType = newTrainingType;
                    createItemList();
                    getScores(trainingType, difficulty);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void goToMainMenu(){
        finish();
    }
}
