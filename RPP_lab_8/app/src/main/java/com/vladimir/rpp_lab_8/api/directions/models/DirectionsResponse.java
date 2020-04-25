package com.vladimir.rpp_lab_8.api.directions.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DirectionsResponse {

    public List<Route> routes;

    public String status;

    @SerializedName("error_message")
    public String errorMessage;

}
