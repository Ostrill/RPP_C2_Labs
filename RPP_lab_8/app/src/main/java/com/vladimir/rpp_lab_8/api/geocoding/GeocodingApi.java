package com.vladimir.rpp_lab_8.api.geocoding;

import com.vladimir.rpp_lab_8.api.geocoding.models.GeocodingResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeocodingApi {

    @GET("geocode/json")
    Call<GeocodingResponse> getAddress(
            @Query("address") String address,
            @Query("key") String apiKey
    );

}
