package com.vladimir.rpp_lab_5.api;

import com.vladimir.rpp_lab_5.Constants;
import com.vladimir.rpp_lab_5.models.Breed;
import com.vladimir.rpp_lab_5.models.Picture;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface CatApi {

    @Headers("x-api-key: " + Constants.API_KEY)
    @GET("images/search")
    Call<ArrayList<Picture>> getImages(
            @Query("limit") int limit,
            @Query("page")  int page,
            @Query("breed_id") String breed_id
    );

    @Headers("x-api-key: " + Constants.API_KEY)
    @GET("breeds/search")
    Call<ArrayList<Breed>> getBreeds(
            @Query("q") String query
    );

    @Headers("x-api-key: " + Constants.API_KEY)
    @GET("breeds")
    Call<ArrayList<Breed>> getAllBreeds();

}
