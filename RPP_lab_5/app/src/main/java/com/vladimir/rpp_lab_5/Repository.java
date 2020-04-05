package com.vladimir.rpp_lab_5;

import android.util.Log;

import com.vladimir.rpp_lab_5.api.CatApi;
import com.vladimir.rpp_lab_5.models.Breed;
import com.vladimir.rpp_lab_5.models.Picture;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {

    private static CatApi api = null;
    private static Callback<ArrayList<Picture>> apiCallback = null;
    private static ArrayList<Picture> lastPictures = null;

    private static void initRepository() {
        api = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CatApi.class);

        apiCallback = new Callback<ArrayList<Picture>>() {
            @Override
            public void onResponse(
                    Call<ArrayList<Picture>> call,
                    Response<ArrayList<Picture>> response
            ) {
                Log.wtf("Repository", "#onResponse");
                lastPictures = response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<Picture>> call, Throwable t) {
                Log.wtf("Repository", "#onFailture");
            }
        };
    }

    public static ArrayList<Picture> getImages(
            int limit,
            int page,
            String breedID
    ) throws IOException {
        if (api == null) initRepository();
        return api.getImages(limit, page, breedID).execute().body();
    }

    public static Breed getBreed(String query) throws IOException {
        if (api == null) initRepository();
        return api.getBreeds(query).execute().body().get(0);
    }

    public static Call<ArrayList<Breed>> getBreedsCall(String query) {
        if (api == null) initRepository();
        return api.getBreeds(query);
    }

    public static Call<ArrayList<Breed>> getAllBreedsCall() {
        if (api == null) initRepository();
        return api.getAllBreeds();
    }

}
