package com.vladimir.rpp_lab_5.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.vladimir.rpp_lab_5.R;

import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.ViewHolder> {

    private RequestManager requestManager;
    private LinkedList<String> likes;

    public LikesAdapter(RequestManager requestManager, LinkedList<String> likes) {
        this.requestManager = requestManager;
        this.likes = likes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_like_item, parent, false);

        return new LikesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        requestManager
                .load(likes.get(position))
                .placeholder(R.drawable.ic_placeholder_picture)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return likes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.like_image);
        }
    }

}
