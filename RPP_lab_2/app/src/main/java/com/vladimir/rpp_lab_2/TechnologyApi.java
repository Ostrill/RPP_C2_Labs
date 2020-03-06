package com.vladimir.rpp_lab_2;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TechnologyApi {

    @GET("techs.ruleset.json")
    Call<ArrayList<TechnologyModel>> getTechnologiesList();

}
