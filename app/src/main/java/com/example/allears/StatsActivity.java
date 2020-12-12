package com.example.allears;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
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
import java.util.Collections;
import java.util.Comparator;
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

    private ProgressBar progressBar;

    // Constants
    private static final String INTERVAL_TRAINING = "Interval";
    private static final String CHORD_TRAINING = "Chord";
    private static final String EASY_DIFFICULTY = "easy";
    private static final String MEDIUM_DIFFICULTY = "medium";
    private static final String HARD_DIFFICULTY = "hard";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        // setting database referencee to the stats table
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Stats");
        createItemList();
        createRecyclerView();

        // default values
        trainingType = INTERVAL_TRAINING;
        difficulty = EASY_DIFFICULTY;

        trainingSpinner = findViewById(R.id.training_spinner);
        difficultySpinner = findViewById(R.id.difficulty_spinner);
        progressBar = findViewById(R.id.progress_spinner);
        // Fetch scores
        getScores(trainingType, difficulty);
        populateSpinners();
        setSpinnerListeners();
    }

    // Getting the scores from firebase for a particular trainingType and difficulty
    private void getScores(String trainingType, String difficulty){
        progressBar.setVisibility(View.VISIBLE);
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
                    progressBar.setVisibility(View.INVISIBLE);
                }
                progressBar.setVisibility(View.INVISIBLE);
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

    // Adds an item to the list and then sorts it
    public void addItem(Score score){
        itemList.add(score);
        Collections.sort(itemList, new CustomComparator());
        rAdapter.notifyDataSetChanged();
    }

    // Custom comparator to sort the itemlist in descending order
    public class CustomComparator implements Comparator<Score> {

        @Override
        public int compare(Score o1, Score o2) {
            return o2.getScore().compareTo(o1.getScore());
        }
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

    // Adding dropdown values for training and difficult spinners
    private void populateSpinners(){
        List<String> trainingList = new ArrayList<>(
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
                    itemList.clear();
                    recyclerView.getAdapter().notifyDataSetChanged();
                    getScores(trainingType, difficulty);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newDifficulty = difficultySpinner.getItemAtPosition(position).toString();
                if (!difficulty.equals(newDifficulty)){
                    difficulty = newDifficulty;
                    itemList.clear();
                    recyclerView.getAdapter().notifyDataSetChanged();
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
