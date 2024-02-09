package com.example.thetrek.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LocationDAO {
    private SQLiteDatabase database;
    private LocationsDbHelper dbHelper;

    public LocationDAO(Context context) {
        dbHelper = new LocationsDbHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertLocation(String name, double latitude, double longitude, double elevation, boolean isVisited) {
        ContentValues values = new ContentValues();
        values.put(LocationsDbHelper.COLUMN_NAME, name);
        values.put(LocationsDbHelper.COLUMN_LATITUDE, latitude);
        values.put(LocationsDbHelper.COLUMN_LONGITUDE, longitude);
        values.put(LocationsDbHelper.COLUMN_ELEVATION, elevation);
        values.put(LocationsDbHelper.COLUMN_IS_VISITED, isVisited ? 1 : 0);

        return database.insert(LocationsDbHelper.TABLE_NAME, null, values);
    }

    public void updateLocation(String name, int id, double latitude, double longitude, double elevation, boolean isVisited) {
        ContentValues values = new ContentValues();
        values.put(LocationsDbHelper.COLUMN_NAME, name);
        values.put(LocationsDbHelper.COLUMN_LATITUDE, latitude);
        values.put(LocationsDbHelper.COLUMN_LONGITUDE, longitude);
        values.put(LocationsDbHelper.COLUMN_ELEVATION, elevation);
        values.put(LocationsDbHelper.COLUMN_IS_VISITED, isVisited ? 1 : 0);

        String whereClause = LocationsDbHelper.COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};

        database.update(LocationsDbHelper.TABLE_NAME, values, whereClause, whereArgs);
    }

    public void deleteLocation(int id) {
        String whereClause = LocationsDbHelper.COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};

        database.delete(LocationsDbHelper.TABLE_NAME, whereClause, whereArgs);
    }

    public Cursor getAllLocations() {
        String[] projection = {LocationsDbHelper.COLUMN_NAME, LocationsDbHelper.COLUMN_ID, LocationsDbHelper.COLUMN_LATITUDE, LocationsDbHelper.COLUMN_LONGITUDE, LocationsDbHelper.COLUMN_ELEVATION, LocationsDbHelper.COLUMN_IS_VISITED};

        return database.query(LocationsDbHelper.TABLE_NAME, projection, null, null, null, null, null);
    }
}
