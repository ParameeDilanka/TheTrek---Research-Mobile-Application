package com.example.thetrek.screens.locationsHandler;

public class Location {
    private int id;
    private double latitude;
    private double longitude;
    private boolean isVisited;
    private String locationName;
    private double elevation;

    public Location(int id, double latitude, double longitude, boolean isVisited, String locationName, double elevation) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isVisited = isVisited;
        this.locationName = locationName;
        this.elevation = elevation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }
}
