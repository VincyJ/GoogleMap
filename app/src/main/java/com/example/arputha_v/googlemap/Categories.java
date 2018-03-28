package com.example.arputha_v.googlemap;

import java.util.List;

/**
 * Created by arputha_v on 3/7/2018.
 */

public class Categories {

    private String categoryName;
    private int color;
    private List<Locations> locations;

    public Categories() {

    }

    public Categories(String categoryName, int color, List<Locations> locations) {
        this.categoryName = categoryName;
        this.color = color;
        this.locations = locations;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<Locations> getLocations() {
        return locations;
    }

    public void setLocations(List<Locations> locations) {
        this.locations = locations;
    }
}
