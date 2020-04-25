package com.vladimir.rpp_lab_8.api.directions;

import com.vladimir.rpp_lab_8.api.directions.models.DirectionsResponse;
import com.vladimir.rpp_lab_8.api.geocoding.models.GeocodingResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DirectionsApi {

    @GET("directions/json")
    Call<DirectionsResponse> getPath(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("key") String apiKey
    );

}
