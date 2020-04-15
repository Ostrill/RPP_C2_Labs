package com.vladimir.rpp_lab_5.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.vladimir.rpp_lab_5.Constants;
import com.vladimir.rpp_lab_5.R;
import com.vladimir.rpp_lab_5.models.PictureVote;
import com.vladimir.rpp_lab_5.viewModels.PictureViewModel;
import com.vladimir.rpp_lab_5.adapters.PictureAdapter;
import com.vladimir.rpp_lab_5.models.Breed;

import java.util.ArrayList;
import java.util.LinkedList;

public class PictureFragment extends Fragment {

    private final static String TAG = "PictureFragment";

    private RecyclerView recyclerView;
    private PictureViewModel pictureViewModel;

    public PictureFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pictures, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pictureViewModel = ViewModelProviders.of(this).get(PictureViewModel.class);

        recyclerView = getView().findViewById(R.id.recyclerview_pictures);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(pictureViewModel.getPictureAdapter());

        LiveData<Breed> breedLiveData = pictureViewModel.getCurrentBreed();
        breedLiveData.observe(this, new Observer<Breed>() {
            @Override
            public void onChanged(Breed breed) {
                if (breed != null) {
                    Log.d(TAG, "breed is "+breed.name+" ["+breed.id+"]");
                    pictureViewModel.setPictureAdapter(breed);
                    pictureViewModel.getPictureAdapter().setOnVoteClickListener(onVoteClickListener);
                    recyclerView.setAdapter(pictureViewModel.getPictureAdapter());
                    getActivity().setTitle(breed.name);
                    pictureViewModel.saveBreedToSP(breed);
                }
            }
        });

        LiveData<Boolean> loadingStateLiveData = pictureViewModel.getLoadingState();
        loadingStateLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                setLoading(aBoolean);
            }
        });

        getActivity().setTitle(pictureViewModel.loadBreedFromSP().name);
    }

    private void setLoading(boolean state) {
        if (state) {
            getView().findViewById(R.id.picture_progress_bar).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.picture_progress_bar).setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        final MenuItem searchItem = menu.findItem(R.id.menu_search_button);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        final SearchView.SearchAutoComplete searchAutoComplete =
                searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        searchAutoComplete.setDropDownAnchor(R.id.menu_search_button);
        searchAutoComplete.setThreshold(1);

        LiveData<ArrayList<String>> allBreedsLiveData = pictureViewModel.getListOfAllBreeds();
        allBreedsLiveData.observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> breeds) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        breeds
                );

                searchAutoComplete.setAdapter(adapter);
            }
        });

        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                pictureViewModel.setCurrentBreed(selected);
                searchItem.collapseActionView();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pictureViewModel.setCurrentBreed(query);
                searchItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPause() {
        super.onPause();
        pictureViewModel.setRecyclerViewState(
                recyclerView.getLayoutManager().onSaveInstanceState()
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.getLayoutManager()
                .onRestoreInstanceState(pictureViewModel.getRecyclerViewState());
        pictureViewModel.getPictureAdapter().setOnVoteClickListener(onVoteClickListener);
    }

    private final PictureAdapter.OnVoteClickListener onVoteClickListener =
            new PictureAdapter.OnVoteClickListener() {
                @Override
                public void onVoteClick(PictureVote vote, boolean isLike) {
                    LinkedList<String> likes = pictureViewModel
                            .loadVotesFromSP(Constants.SAVED_LIKES);
                    LinkedList<String> dislikes = pictureViewModel
                            .loadVotesFromSP(Constants.SAVED_DISLIKES);

                    if (isLike) {
                        if (likes.contains(vote.picture.url)) {
                            vote.likeButton.setImageResource(R.drawable.ic_like);

                            // remove from likes
                            likes.remove(vote.picture.url);
                        } else {
                            vote.likeButton.setImageResource(R.drawable.ic_like_active);
                            vote.dislikeButton.setImageResource(R.drawable.ic_dislike);

                            // remove from dislikes
                            // add to likes
                            dislikes.remove(vote.picture.url);
                            likes.addFirst(vote.picture.url);
                        }
                    } else {
                        if (dislikes.contains(vote.picture.url)) {
                            vote.dislikeButton.setImageResource(R.drawable.ic_dislike);

                            // remove from dislikes
                            dislikes.remove(vote.picture.url);
                        } else {
                            vote.dislikeButton.setImageResource(R.drawable.ic_dislike_active);
                            vote.likeButton.setImageResource(R.drawable.ic_like);

                            // remove from likes
                            // add to dislikes
                            likes.remove(vote.picture.url);
                            dislikes.addFirst(vote.picture.url);
                        }
                    }

                    pictureViewModel.saveVotesToSP(likes,    Constants.SAVED_LIKES   );
                    pictureViewModel.saveVotesToSP(dislikes, Constants.SAVED_DISLIKES);
                    pictureViewModel.getPictureAdapter().reloadVotes(likes, dislikes);
                }
            };
}


