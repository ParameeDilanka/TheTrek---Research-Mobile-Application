package com.example.thetrek.geospatial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.thetrek.HomeActivity;
import com.example.thetrek.R;
import com.example.thetrek.db.RewardDbHelper;
import com.example.thetrek.db.RewardStrings;
import com.example.thetrek.screens.findPlaces.FindPlacesActivity;
import com.example.thetrek.screens.rewards.Reward;

public class RewardingScreenActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewarding_screen);
        getSupportActionBar().hide();

        Button findPlacesButton = (Button) findViewById(R.id.btn_claim_reward);
        findPlacesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RewardDbHelper dbHelper = new RewardDbHelper(v.getContext());
                int id = dbHelper.addReward("New Reward");
                dbHelper.updateReward(new Reward(id, RewardStrings.getProgressArray()[id]));
                finish();
                Intent intent =  new Intent(v.getContext(), HomeActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }
}