package com.example.arputha_v.googlemap;

/**
 * Created by arputha_v on 3/7/2018.
 */

public class Locations {

    private String title;
    private String description;
    private String locationImage;
    private double latitude;
    private double longitude;

    public Locations() {

    }

    public Locations(String title, String description, String locationImage, double latitude, double longitude) {
        this.title = title;
        this.description = description;
        this.locationImage = locationImage;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationImage() {
        return locationImage;
    }

    public void setLocationImage(String locationImage) {
        this.locationImage = locationImage;
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
}
