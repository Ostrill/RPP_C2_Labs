package com.vladimir.rpp_lab_8.api.geocoding.models;

import android.location.Location;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class Viewport {

    @SerializedName("northeast")
    public Coordinate northEast;

    @SerializedName("southwest")
    public Coordinate southWest;
    
    public double getDistance() {
        Location l1 = new Location("");
        l1.setLatitude(northEast.lat);
        l1.setLongitude(northEast.lng);

        Location l2 = new Location("");
        l2.setLatitude(southWest.lat);
        l2.setLongitude(southWest.lng);

        return l1.distanceTo(l2);
    }

}
