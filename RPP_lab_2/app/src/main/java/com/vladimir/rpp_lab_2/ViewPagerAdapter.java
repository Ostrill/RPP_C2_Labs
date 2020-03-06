package com.vladimir.rpp_lab_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    private ArrayList<TechnologyModel> list = new ArrayList<>();
    private RequestManager glide;

    public void setList() {
        list = TechnologyRepository.getList();
    }

    public ViewPagerAdapter(RequestManager glide) {
        this.glide = glide;
    }

    @NonNull
    @Override
    public ViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.view_item_pager, parent, false);
        ViewPagerAdapter.ViewHolder viewHolder = new ViewPagerAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewHolder holder, int position) {
        holder.textName.setText(list.get(position).name);
        holder.textDescription.setText(list.get(position).helptext);

        glide
                .load(Constants.IMG_BASE_URL + list.get(position).graphic)
                .placeholder(R.drawable.ic_image_placeholder_64dp)
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textName;
        private final TextView textDescription;
        private final ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.pagerName);
            textDescription = itemView.findViewById(R.id.pagerDescription);
            image = itemView.findViewById(R.id.pagerImage);

        }
    }

}
