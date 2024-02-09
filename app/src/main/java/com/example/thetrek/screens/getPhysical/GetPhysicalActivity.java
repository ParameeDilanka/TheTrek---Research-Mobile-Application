package com.example.thetrek.screens.getPhysical;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.thetrek.R;

public class GetPhysicalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_physical);
        getSupportActionBar().hide();
    }
}