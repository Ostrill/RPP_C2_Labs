package com.vladimir.rpp_lab_5.models;

import android.widget.ImageView;

public class PictureVote {

    public ImageView likeButton;
    public ImageView dislikeButton;
    public Picture picture;

    public PictureVote(ImageView likeButton, ImageView dislikeButton, Picture picture) {
        this.likeButton = likeButton;
        this.dislikeButton = dislikeButton;
        this.picture = picture;
    }
}
