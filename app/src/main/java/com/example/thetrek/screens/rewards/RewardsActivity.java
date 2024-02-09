package com.example.thetrek.screens.rewards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.thetrek.R;
import com.example.thetrek.db.RewardDbHelper;

import java.util.ArrayList;
import java.util.List;

public class RewardsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RewardAdapter rewardAdapter;
    private List<Reward> rewardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerView_rewards);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rewardList = new ArrayList<>(); // Ensure proper initialization here
        rewardAdapter = new RewardAdapter(rewardList);

        recyclerView.setAdapter(rewardAdapter);

        loadRewardData();

    }

    private void loadRewardData() {
        // Retrieve the reward data from SQLite and update the reward list
        // For example, you can use your RewardDbHelper class to fetch the data
        RewardDbHelper dbHelper = new RewardDbHelper(this);
        rewardList.clear();
        rewardList.addAll(dbHelper.getAllRewards());
        rewardAdapter.notifyDataSetChanged();
    }
}