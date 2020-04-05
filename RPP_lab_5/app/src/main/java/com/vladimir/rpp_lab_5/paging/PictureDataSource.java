package com.vladimir.rpp_lab_5.paging;

import android.util.Log;

import com.vladimir.rpp_lab_5.Constants;
import com.vladimir.rpp_lab_5.Repository;
import com.vladimir.rpp_lab_5.models.Picture;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

public class PictureDataSource extends PageKeyedDataSource<Integer, Picture> {

    private final String breedID;
    private final String TAG = "PictureDataSource";

    public PictureDataSource(String breedID) {
        this.breedID = breedID;
    }

    @Override
    public void loadInitial(
            @NonNull LoadInitialParams<Integer> params,
            @NonNull LoadInitialCallback<Integer, Picture> callback
    ) {
        try {
            ArrayList<Picture> pictures = Repository.getImages(params.requestedLoadSize, 0, breedID);
            callback.onResult(pictures, null, 1);
            Log.d(TAG, "#loadInitial ("+params.requestedLoadSize+")");
        } catch (IOException e) {}

    }

    @Override
    public void loadBefore(
            @NonNull LoadParams<Integer> params,
            @NonNull LoadCallback<Integer, Picture> callback
    ) {
        try {
            ArrayList<Picture> pictures = Repository.getImages(params.requestedLoadSize, params.key, breedID);
            callback.onResult(pictures, params.key - 1);
            Log.d(TAG, "#loadBefore: "+params.key+" ("+params.requestedLoadSize+")");
        } catch (IOException e) {
            Log.d(TAG, "loadBefore crashed");
        }
    }

    @Override
    public void loadAfter(
            @NonNull LoadParams<Integer> params,
            @NonNull LoadCallback<Integer, Picture> callback
    ) {
        try {
            ArrayList<Picture> pictures = Repository.getImages(params.requestedLoadSize, params.key, breedID);
            callback.onResult(pictures, params.key + 1);
            Log.d(TAG, "#loadAfter: "+params.key+" ("+params.requestedLoadSize+")");
        } catch (IOException e) {
            Log.d(TAG, "loadAfter crashed");
        }
    }
}
