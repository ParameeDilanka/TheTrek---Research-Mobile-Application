package com.example.thetrek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.thetrek.activities.ShowPeopleActivity;
import com.example.thetrek.db.LocationDAO;
import com.example.thetrek.db.LocationsDbHelper;
import com.example.thetrek.db.RewardDbHelper;
import com.example.thetrek.db.RewardStrings;
import com.example.thetrek.geospatial.GeospatialActivity;
import com.example.thetrek.screens.findPlaces.FindPlacesActivity;
import com.example.thetrek.screens.getPhysical.GetPhysicalActivity;
import com.example.thetrek.screens.locationsHandler.AddLocations;
import com.example.thetrek.screens.locationsHandler.Location;
import com.example.thetrek.screens.locationsHandler.LocationsCrud;
import com.example.thetrek.screens.mobileUsage.MobileUsageActivity;
import com.example.thetrek.screens.rewards.Reward;
import com.example.thetrek.screens.rewards.RewardsActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private LocationDAO locationDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        locationDAO = new LocationDAO(this);
        locationDAO.open();

        Button findPlacesButton = (Button) findViewById(R.id.btn_homeActivity_findPlaces);
        findPlacesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(v.getContext(), FindPlacesActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        Button activityRecognitionButton = (Button) findViewById(R.id.btn_activityRecognition);
        activityRecognitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(v.getContext(), ShowPeopleActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        Button recorderButton = (Button) findViewById(R.id.btn_recorder);
        recorderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(v.getContext(), MainActivityRec.class);
                v.getContext().startActivity(intent);
            }
        });

        Button mobileUsageButton = (Button) findViewById(R.id.btn_homeActivity_mobileUsage);
        mobileUsageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(v.getContext(), MobileUsageActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        Button suggestpredictor = (Button) findViewById(R.id.btn_predictor);
        suggestpredictor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(v.getContext(), SuggestActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        Button getPhysicalButton = (Button) findViewById(R.id.btn_homeActivity_getPhysical);
        getPhysicalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(v.getContext(), GeospatialActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        Button healthMonitoringButton = (Button) findViewById(R.id.btn_homeActivity_healthMonitoring);
        healthMonitoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(v.getContext(), WaterActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        Button rewards = (Button) findViewById(R.id.btn_homeActivity_rewards);
        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(v.getContext(), RewardsActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        List<Location> locationList = getLocationDataFromDatabase();

        Button locationsHandler = (Button) findViewById(R.id.btn_homeActivity_locationsHandler);
        locationsHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (locationList.isEmpty()){
//                    Intent intent =  new Intent(v.getContext(), AddLocations.class);
//                    v.getContext().startActivity(intent);
//                }else {
                Intent intent = new Intent(v.getContext(), LocationsCrud.class);
                v.getContext().startActivity(intent);
//            }
            }
        });
    }

    private List<Location> getLocationDataFromDatabase() {
        List<Location> locationList = new ArrayList<>();

        Cursor cursor = locationDAO.getAllLocations();
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(LocationsDbHelper.COLUMN_ID);
            int latitudeIndex = cursor.getColumnIndex(LocationsDbHelper.COLUMN_LATITUDE);
            int longitudeIndex = cursor.getColumnIndex(LocationsDbHelper.COLUMN_LONGITUDE);
            int isVisitedIndex = cursor.getColumnIndex(LocationsDbHelper.COLUMN_IS_VISITED);
            int locationNameIndex = cursor.getColumnIndex(LocationsDbHelper.COLUMN_NAME);
            int elevationIndex = cursor.getColumnIndex(LocationsDbHelper.COLUMN_ELEVATION);

            do {
                int id = cursor.getInt(idIndex);
                double latitude = cursor.getDouble(latitudeIndex);
                double longitude = cursor.getDouble(longitudeIndex);
                boolean isVisited = cursor.getInt(isVisitedIndex) == 1;
                String locationName = cursor.getString(locationNameIndex);
                double elevation = cursor.getDouble(elevationIndex);

                Location location = new Location(id, latitude, longitude, isVisited, locationName, elevation);
                locationList.add(location);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return locationList;
    }
}