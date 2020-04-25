package com.vladimir.rpp_lab_8.api.places.models;

import com.google.gson.annotations.SerializedName;

public class FormattedLocationName {

    @SerializedName("main_text")
    public String mainText;

    @SerializedName("secondary_text")
    public String secondaryText;

}
