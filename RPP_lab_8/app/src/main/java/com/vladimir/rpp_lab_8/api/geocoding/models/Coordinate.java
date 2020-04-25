package com.vladimir.rpp_lab_8.api.geocoding.models;

import com.google.android.gms.maps.model.LatLng;

public class Coordinate {

    public Double lat;
    public Double lng;

    public LatLng toLatLng() {
        return new LatLng(lat, lng);
    }

}
