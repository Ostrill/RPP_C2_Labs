package com.vladimir.rpp_lab_8.api.places.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacesResponse {

    @SerializedName("predictions")
    public List<SearchResult> searchResults;

    public String status;

    @SerializedName("error_message")
    public String errorMessage;

}
