package com.vladimir.rpp_lab_2;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TechnologiesApiFactory {

    public static TechnologyApi createApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(TechnologyApi.class);
    }

}
