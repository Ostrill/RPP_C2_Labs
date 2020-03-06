package com.vladimir.rpp_lab_2;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class TechnologyRepository {
    private static ArrayList<TechnologyModel> list;

    static void initRepository(final SplashScreenActivity activity) {
        TechnologyApi api = TechnologiesApiFactory.createApi();
        Call<ArrayList<TechnologyModel>> call = api.getTechnologiesList();

        call.enqueue(new Callback<ArrayList<TechnologyModel>>() {
            @Override
            public void onResponse(Call<ArrayList<TechnologyModel>> call, Response<ArrayList<TechnologyModel>> response) {
                assert response.body() != null;
                response.body().remove(0);
                list = response.body();

                activity.closeActivity();
            }

            @Override
            public void onFailure(Call<ArrayList<TechnologyModel>> call, Throwable t) {
                activity.showNoInternet();
            }
        });
    }

    static ArrayList<TechnologyModel> getList() {
        return list;
    }
}
