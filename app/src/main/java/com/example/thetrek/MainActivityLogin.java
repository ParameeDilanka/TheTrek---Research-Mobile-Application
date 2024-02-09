package com.example.thetrek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.thetrek.screens.mobileUsage.ScreenMonitoringService;

public class MainActivityLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        startService(new Intent(this, ScreenMonitoringService.class));

        Button loginButton = (Button) findViewById(R.id.btnSubmit_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent =  new Intent(v.getContext(), HomeActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }
}