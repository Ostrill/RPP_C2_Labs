package com.vladimir.rpp_lab_5.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.vladimir.rpp_lab_5.R;
import com.vladimir.rpp_lab_5.models.Category;
import com.vladimir.rpp_lab_5.models.Picture;
import com.vladimir.rpp_lab_5.models.PictureVote;

import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PictureAdapter extends PagedListAdapter<Picture, PictureAdapter.ViewHolder> {

    private RequestManager requestManager;
    private LinkedList<String> likes;
    private LinkedList<String> dislikes;

    private final String TAG = "PictureAdapter";

    @Nullable
    private OnVoteClickListener onVoteClickListener;

    public PictureAdapter(
            DiffUtil.ItemCallback<Picture> diffUtilCallback,
            RequestManager requestManager,
            LinkedList<String> likes,
            LinkedList<String> dislikes
    ) {
        super(diffUtilCallback);

        if (likes == null) likes = new LinkedList<>();
        if (dislikes == null) dislikes = new LinkedList<>();

        this.requestManager = requestManager;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_picture_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = getItem(position).url;

        requestManager
                .load(url)
                .placeholder(R.drawable.ic_placeholder_picture)
                .into(holder.imageView);

        String categories = getAllCategories(getItem(position));
        holder.textView.setText(categories);

        if (likes.contains(url)) {
            holder.likeButton.setImageResource(R.drawable.ic_like_active);
        } else {
            holder.likeButton.setImageResource(R.drawable.ic_like);
        }

        if (dislikes.contains(url)) {
            holder.dislikeButton.setImageResource(R.drawable.ic_dislike_active);
        } else {
            holder.dislikeButton.setImageResource(R.drawable.ic_dislike);
        }

        PictureVote vote = new PictureVote(
                holder.likeButton,
                holder.dislikeButton,
                getItem(position)
        );

        holder.likeButton.setTag(vote);
        holder.dislikeButton.setTag(vote);
    }

    public void setOnVoteClickListener(@Nullable OnVoteClickListener onVoteClickListener) {
        this.onVoteClickListener = onVoteClickListener;
    }

    public void reloadVotes(
            LinkedList<String> likes,
            LinkedList<String> dislikes
    ) {
        this.likes = likes;
        this.dislikes = dislikes;
    }

    private String getAllCategories(Picture picture) {
        StringBuilder categoriesString = new StringBuilder();
        if (picture.categories != null) {
            for (Category current : picture.categories) {
                categoriesString.append(", ").append(current.name);
            }
        }

        if (picture.url.endsWith(".gif")) {
            categoriesString.append(", gif");
        }

        categoriesString.delete(0, 2);
        return categoriesString.toString();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView textView;
        private final ImageView likeButton;
        private final ImageView dislikeButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView     = itemView.findViewById(R.id.picture_image  );
            textView      = itemView.findViewById(R.id.picture_text   );
            likeButton    = itemView.findViewById(R.id.picture_like   );
            dislikeButton = itemView.findViewById(R.id.picture_dislike);

            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PictureVote vote = (PictureVote) v.getTag();
                    if (onVoteClickListener != null) {
                        onVoteClickListener.onVoteClick(vote, true);
                    }
                }
            });

            dislikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PictureVote vote = (PictureVote) v.getTag();
                    if (onVoteClickListener != null) {
                        onVoteClickListener.onVoteClick(vote, false);
                    }
                }
            });
        }
    }

    public interface OnVoteClickListener {
        void onVoteClick(PictureVote vote, boolean isLike);
    }

}
