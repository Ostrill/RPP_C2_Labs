package com.vladimir.rpp_lab_8.api.directions.models;

import com.vladimir.rpp_lab_8.api.geocoding.models.Viewport;

import java.util.List;

public class Route {

    public Viewport bounds;
    public List<Step> legs;

}
