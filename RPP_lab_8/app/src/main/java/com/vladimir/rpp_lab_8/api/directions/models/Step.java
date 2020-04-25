package com.vladimir.rpp_lab_8.api.directions.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.vladimir.rpp_lab_8.api.geocoding.models.Coordinate;

import java.util.List;

public class Step {

    @SerializedName("start_address")
    public String startLabel;

    @SerializedName("end_address")
    public String endLabel;

    @SerializedName("start_location")
    public Coordinate startAdress;

    @SerializedName("end_location")
    public Coordinate endAdress;

    public List<Step> steps;

}
