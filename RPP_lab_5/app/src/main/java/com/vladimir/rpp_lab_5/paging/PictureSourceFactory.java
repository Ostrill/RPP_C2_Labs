package com.vladimir.rpp_lab_5.paging;

import com.vladimir.rpp_lab_5.models.Breed;
import com.vladimir.rpp_lab_5.models.Picture;

import androidx.paging.DataSource;

public class PictureSourceFactory extends DataSource.Factory<Integer, Picture> {

    private final String breedID;

    public PictureSourceFactory(Breed breed) {
        if (breed == null) {
            breedID = null;
        } else {
            breedID = breed.id;
        }
    }

    @Override
    public DataSource<Integer, Picture> create() {
        return new PictureDataSource(breedID);
    }

}
