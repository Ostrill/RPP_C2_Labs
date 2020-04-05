package com.vladimir.rpp_lab_5.viewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.vladimir.rpp_lab_5.Constants;
import com.vladimir.rpp_lab_5.Repository;
import com.vladimir.rpp_lab_5.adapters.PictureAdapter;
import com.vladimir.rpp_lab_5.models.Breed;
import com.vladimir.rpp_lab_5.models.Picture;
import com.vladimir.rpp_lab_5.paging.PictureDiffUtilCallback;
import com.vladimir.rpp_lab_5.paging.PictureSourceFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PictureViewModel extends AndroidViewModel {

    private static String TAG = "PictureViewModel";

    private static PictureAdapter pictureAdapter = null;
    private static Parcelable recyclerViewState;

    private MutableLiveData<Breed> currentBreed;
    private MutableLiveData<ArrayList<String>> allBreeds;
    private MutableLiveData<Boolean> loadingState;

    public PictureViewModel(@NonNull Application application) {
        super(application);
        loadingState = new MutableLiveData<>();
    }

    public void setPictureAdapter(Breed breed) {
        PictureSourceFactory pictureSourceFactory = new PictureSourceFactory(breed);
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .build();

        LiveData<PagedList<Picture>> pagedListLiveData =
                new LivePagedListBuilder<>(pictureSourceFactory, config)
                        .build();

        pictureAdapter = new PictureAdapter(
                new PictureDiffUtilCallback(),
                Glide.with(getApplication()),
                loadVotesFromSP(Constants.SAVED_LIKES),
                loadVotesFromSP(Constants.SAVED_DISLIKES)
        );

        loadingState.postValue(true);
        pagedListLiveData.observeForever(new Observer<PagedList<Picture>>() {
            @Override
            public void onChanged(@Nullable PagedList<Picture> pictures) {
                loadingState.postValue(false);
                pictureAdapter.submitList(pictures);
            }
        });
    }

    public LiveData<Boolean> getLoadingState() {
        return loadingState;
    }

    public PictureAdapter getPictureAdapter() {
        if (pictureAdapter == null) {
            setPictureAdapter(loadBreedFromSP());
        }

        return pictureAdapter;
    }

    public LiveData<ArrayList<String>> getListOfAllBreeds() {
        if (allBreeds == null) {
            allBreeds = new MutableLiveData<>();
            Repository.getAllBreedsCall().enqueue(new Callback<ArrayList<Breed>>() {
                @Override
                public void onResponse(
                        Call<ArrayList<Breed>> call,
                        Response<ArrayList<Breed>> response
                ) {
                    ArrayList<String> list = new ArrayList<>();
                    for (Breed current : response.body()) {
                        list.add(current.name);
                    }
                    allBreeds.postValue(list);
                }

                @Override
                public void onFailure(Call<ArrayList<Breed>> call, Throwable t) {
                    Log.d(TAG, "cant load breeds");
                }
            });
        }

        return allBreeds;
    }

    public void setCurrentBreed(String query) {
        if (currentBreed == null) {
            currentBreed = new MutableLiveData<>();
        }

        if (query.equals("all")) {
            Breed allBreeds = new Breed(null, "All");
            currentBreed.postValue(allBreeds);
            return;
        }

        Repository.getBreedsCall(query).enqueue(new Callback<ArrayList<Breed>>() {
            @Override
            public void onResponse(
                    Call<ArrayList<Breed>> call,
                    Response<ArrayList<Breed>> response
            ) {
                if (response.body().size() == 0) {
                    currentBreed.postValue(null);
                } else {
                    currentBreed.postValue(response.body().get(0));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Breed>> call, Throwable t) {
                Log.d(TAG, "cant load breed");
            }
        });
    }

    public Breed loadBreedFromSP() {
        SharedPreferences sp = getApplication()
                .getSharedPreferences(Constants.BREED_SP_NAME, Context.MODE_PRIVATE);

        Breed breed = new Breed(
                sp.getString(Constants.SAVED_BREED_ID, null),
                sp.getString(Constants.SAVED_BREED_NAME, "All")
        );

        return breed;
    }

    public void saveBreedToSP(Breed breed) {
        SharedPreferences.Editor editor = getApplication()
                .getSharedPreferences(Constants.BREED_SP_NAME, Context.MODE_PRIVATE)
                .edit();
        editor.putString(Constants.SAVED_BREED_ID, breed.id);
        editor.putString(Constants.SAVED_BREED_NAME, breed.name);
        editor.apply();
    }

    public LinkedList<String> loadVotesFromSP(String type) {
        SharedPreferences sp = getApplication()
                .getSharedPreferences(Constants.VOTES_SP_NAME, Context.MODE_PRIVATE);

        StringTokenizer st = new StringTokenizer(sp.getString(type, ""), " ");
        LinkedList<String> list = new LinkedList<>();
        while (st.hasMoreTokens()) {
            list.add(st.nextToken());
        }

        return list;
    }

    public void saveVotesToSP(LinkedList<String> votes, String type) {
        SharedPreferences.Editor editor = getApplication()
                .getSharedPreferences(Constants.VOTES_SP_NAME, Context.MODE_PRIVATE)
                .edit();

        StringBuilder sb = new StringBuilder();
        int counter = 0;
        for (String current : votes) {
            if (counter == 10) break;
            counter++;
            sb.append(current).append(" ");
        }

        editor.putString(type, sb.toString()).apply();
    }

    public LiveData<Breed> getCurrentBreed() {
        if (currentBreed == null) {
            currentBreed = new MutableLiveData<>();
        }

        return currentBreed;
    }

    public Parcelable getRecyclerViewState() {
        return recyclerViewState;
    }

    public void setRecyclerViewState(Parcelable recyclerViewState) {
        PictureViewModel.recyclerViewState = recyclerViewState;
    }
}
