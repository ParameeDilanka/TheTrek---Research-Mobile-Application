package com.example.thetrek.screens.locationsHandler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thetrek.PredictReward;
import com.example.thetrek.R;
import com.example.thetrek.db.LocationDAO;
import com.example.thetrek.screens.findPlaces.FindPlacesActivity;

public class AddLocations extends AppCompatActivity {
    private EditText editTextLatitude;
    private EditText editTextLongitude;
    private EditText editTextLocationName;
    private EditText editTextElevation;
    private Button buttonAddLocation;
    private LocationDAO locationDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_locations);
        getSupportActionBar().hide();

        editTextLatitude = findViewById(R.id.txt_addLocation_latitude);
        editTextLongitude = findViewById(R.id.txt_addLocation_longitude);
        editTextLocationName = findViewById(R.id.txt_addLocation_name);
        editTextElevation = findViewById(R.id.txt_addLocation_elevation);
        buttonAddLocation = findViewById(R.id.btn_addLocationActivity_addLocation);

        locationDAO = new LocationDAO(this);
        locationDAO.open();

        buttonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextLocationName.getText().toString();
                double latitude = Double.parseDouble(editTextLatitude.getText().toString());
                double longitude = Double.parseDouble(editTextLongitude.getText().toString());
                double elevation = Double.parseDouble(editTextElevation.getText().toString());
                boolean isVisited = false; // Set the initial value for isVisited as needed

                long insertedId = locationDAO.insertLocation(name, latitude, longitude, elevation, isVisited);

                if (insertedId != -1) {
                    Toast.makeText(AddLocations.this, "Location added to the database", Toast.LENGTH_SHORT).show();
                    editTextLatitude.setText("");
                    editTextLongitude.setText("");
                    editTextLocationName.setText("");
                    editTextElevation.setText("");
                } else {
                    Toast.makeText(AddLocations.this, "Failed to add location to the database", Toast.LENGTH_SHORT).show();
                }
                finish();
                Intent intent =  new Intent(v.getContext(), LocationsCrud.class);
                v.getContext().startActivity(intent);
            }
        });
        Button userGuideButton = (Button) findViewById(R.id.btn_MapVedioActivity_userGuide);
        userGuideButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        Intent intent =  new Intent(v.getContext(), MapVedioActivity.class);
        v.getContext().startActivity(intent);
       }
  });
        Button rewardPredictor = (Button) findViewById(R.id.btn_predictor);
        rewardPredictor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(v.getContext(), PredictReward.class);
                v.getContext().startActivity(intent);
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationDAO.close();
    }
}