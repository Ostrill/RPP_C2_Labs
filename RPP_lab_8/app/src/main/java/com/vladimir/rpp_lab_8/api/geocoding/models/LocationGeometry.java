package com.vladimir.rpp_lab_8.api.geocoding.models;

import com.google.gson.annotations.SerializedName;

public class LocationGeometry {

    @SerializedName("location")
    public Coordinate coordinate;

    public Viewport viewport;

}
