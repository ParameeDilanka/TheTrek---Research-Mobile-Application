package com.example.thetrek.screens.locationsHandler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.thetrek.R;
import com.example.thetrek.db.LocationDAO;
import com.example.thetrek.db.LocationsDbHelper;

import java.util.ArrayList;
import java.util.List;

public class LocationsCrud extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LocationAdapter locationAdapter;
    private TextView textViewNoLocations;
    private LocationDAO locationDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations_crud);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerView_locationsCrud);
        textViewNoLocations = findViewById(R.id.lbl_locationsCrud_noLocationMessage);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        locationDAO = new LocationDAO(this);
        locationDAO.open();

        List<Location> locationList = getLocationDataFromDatabase();
        if (locationList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            textViewNoLocations.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            textViewNoLocations.setVisibility(View.GONE);
            locationAdapter = new LocationAdapter(locationList, locationDAO);
            recyclerView.setAdapter(locationAdapter);
        }

        Button addLocations = (Button) findViewById(R.id.btn_locationsCrudActivity_addLocations);
        addLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent =  new Intent(v.getContext(), AddLocations.class);
                v.getContext().startActivity(intent);
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