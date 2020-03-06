package com.vladimir.rpp_lab_2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class RecyclerFragment extends Fragment {

    public RecyclerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(Glide.with(this));

        recyclerAdapter.setOnItemClickListener(onItemClickListener);

        RecyclerView recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.setList();
        recyclerAdapter.notifyDataSetChanged();
    }

    private final RecyclerAdapter.OnItemClickListener onItemClickListener =
            new RecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    viewPagerFragment.setArguments(bundle);
                    ((MainActivity) Objects.requireNonNull(getActivity()))
                            .openFragment(viewPagerFragment);
                }
            };
}
