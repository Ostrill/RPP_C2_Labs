package com.vladimir.rpp_lab_8.api.geocoding.models;

import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("formatted_address")
    public String addressName;

    @SerializedName("geometry")
    public LocationGeometry locationGeometry;

    @SerializedName("place_id")
    public String placeId;

}
