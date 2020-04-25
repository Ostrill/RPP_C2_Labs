package com.vladimir.rpp_lab_8.api.places.models;

import com.google.gson.annotations.SerializedName;

public class SearchResult {

    public String description;

    @SerializedName("place_id")
    public String placeId;

    @SerializedName("structured_formatting")
    public FormattedLocationName locationName;

}
