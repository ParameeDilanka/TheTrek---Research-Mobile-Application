package com.example.thetrek.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thetrek.R;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
//        ImageView yogi = findViewById(R.id.yogi);
//        Button start = findViewById(R.id.get_started);
//        start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                System.out.println("Before calling list intent");
//
//                Intent intentlist = new Intent(LaunchActivity.this, ShowPeopleActivity.class);
//                startActivity(intentlist);
//            }
//        });
//    }
//}
        findViewById(R.id.outdoor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this, OutdoorActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.dance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this, DanceActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.sports).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this, SportsActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.workouts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this, WorkoutActivity.class);
                startActivity(intent);
            }
        });
    }
}


