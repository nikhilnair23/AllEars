package com.example.allears;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {
    private ArrayList<Score> itemList;
    private RecyclerView recyclerView;
    private RvAdapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    final Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        createItemList();
        createRecyclerView();

        addItem(new Score("test1",2));
        addItem(new Score("nikhil",0));
        addItem(new Score("test1",2));
        addItem(new Score("nikhil",0));
        addItem(new Score("test1",2));
        addItem(new Score("nikhil",0));
        addItem(new Score("test1",2));
        addItem(new Score("nikhil",0));
    }



    public void createItemList(){
        itemList = new ArrayList<>();
    }

    public void addItem(Score score){
        itemList.add(score);
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

    public void goToMainMenu(){
        finish();
    }
}
