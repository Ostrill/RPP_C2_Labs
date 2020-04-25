package com.vladimir.rpp_lab_8.api.geocoding.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeocodingResponse {

    @SerializedName("results")
    public List<Address> addresses;

    public String status;

    @SerializedName("error_message")
    public String errorMessage;

}
