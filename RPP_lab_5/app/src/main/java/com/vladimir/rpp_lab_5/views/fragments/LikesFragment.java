package com.vladimir.rpp_lab_5.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vladimir.rpp_lab_5.Constants;
import com.vladimir.rpp_lab_5.R;
import com.vladimir.rpp_lab_5.adapters.LikesAdapter;
import com.vladimir.rpp_lab_5.viewModels.PictureViewModel;

import java.util.LinkedList;

public class LikesFragment extends Fragment {

    private static final String TAG = "LikesFragment";

    public LikesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_likes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        PictureViewModel pictureViewModel = ViewModelProviders
                .of(this)
                .get(PictureViewModel.class);

        LinkedList<String> likes = pictureViewModel.loadVotesFromSP(Constants.SAVED_LIKES);

        getActivity().setTitle("Likes");

        TextView textView = getView().findViewById(R.id.likes_text);
        if (likes.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }

        RecyclerView recyclerView = getView().findViewById(R.id.recyclerview_like);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        LikesAdapter adapter = new LikesAdapter(Glide.with(this), likes);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
