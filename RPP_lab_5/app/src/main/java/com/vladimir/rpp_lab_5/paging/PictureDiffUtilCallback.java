package com.vladimir.rpp_lab_5.paging;

import com.vladimir.rpp_lab_5.models.Picture;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class PictureDiffUtilCallback extends DiffUtil.ItemCallback<Picture> {

    @Override
    public boolean areItemsTheSame(@NonNull Picture oldItem, @NonNull Picture newItem) {
        return oldItem.id.equals(newItem.id);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Picture oldItem, @NonNull Picture newItem) {
        return oldItem.url.equals(newItem.url);
    }

}
