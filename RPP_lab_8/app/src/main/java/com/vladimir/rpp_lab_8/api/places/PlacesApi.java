package com.vladimir.rpp_lab_8.api.places;

import com.vladimir.rpp_lab_8.api.geocoding.models.GeocodingResponse;
import com.vladimir.rpp_lab_8.api.places.models.PlacesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApi {

    @GET("place/autocomplete/json")
    Call<PlacesResponse> getSearchResults(
            @Query("input") String input,
            @Query("key") String apiKey
    );

}
