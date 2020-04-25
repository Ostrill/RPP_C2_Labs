package com.vladimir.rpp_lab_8.ui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vladimir.rpp_lab_8.R;
import com.vladimir.rpp_lab_8.utils.Repository;
import com.vladimir.rpp_lab_8.api.geocoding.models.Address;
import com.vladimir.rpp_lab_8.api.geocoding.models.GeocodingResponse;
import com.vladimir.rpp_lab_8.api.places.models.PlacesResponse;
import com.vladimir.rpp_lab_8.api.places.models.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class SetPointFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    private static final String TAG = "SetPointFragment";
    static final int PAGE_FROM = 0;
    static final int PAGE_TO = 1;

    private FloatingSearchView searchView;
    private GoogleMap map;

    private String lastSuggestion = null;
    private ArrayList<SearchSuggestion> prevSuggestions;

    private int pageNumber;

    static SetPointFragment newInstance(int page) {
        SetPointFragment setPointFragment = new SetPointFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        setPointFragment.setArguments(arguments);
        return setPointFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.set_point_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setSearchView();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);
    }

    private void setSearchView() {
        searchView = getView().findViewById(R.id.floating_search_view);

        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                Repository
                        .getInstance()
                        .getSearchResultsCall(newQuery)
                        .enqueue(new Callback<PlacesResponse>() {
                            @Override
                            public void onResponse(
                                    Call<PlacesResponse> call,
                                    Response<PlacesResponse> response
                            ) {
                                if (response.body().searchResults.isEmpty()) {
                                    lastSuggestion = null;
                                    searchView.clearSuggestions();
                                    return;
                                }
                                ArrayList<SearchSuggestion> suggestions = new ArrayList<>();
                                for (final SearchResult searchResult : response.body().searchResults) {
                                    suggestions.add(new SearchSuggestion() {
                                                        @Override
                                                        public String getBody() {
                                                            return searchResult.description;
                                                        }

                                                        @Override
                                                        public int describeContents() {
                                                            return 0;
                                                        }

                                                        @Override
                                                        public void writeToParcel(
                                                                Parcel dest,
                                                                int flags
                                                        ) {}
                                                    });

                                }

                                lastSuggestion = suggestions.get(0).getBody();
                                if (!compareSuggestions(suggestions, prevSuggestions)) {
                                    searchView.swapSuggestions(suggestions);
                                }
                                prevSuggestions = new ArrayList<>(suggestions);
                            }

                            @Override
                            public void onFailure(Call<PlacesResponse> call, Throwable t) {

                            }
                        });
            }
        });

        searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.menu_my_location) {
                    LatLng myLocation = Repository.getInstance().getGpsLocation();
                    if (myLocation == null) return;
                    chooseLocation(Repository.latLngToString(myLocation));
                } else {
                    setFinalAddress(null);
                    searchView.clearQuery();
                    showChosenLocation();
                }
            }
        });

        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                searchView.clearSearchFocus();
                chooseLocation(searchSuggestion.getBody());
            }

            @Override
            public void onSearchAction(String currentQuery) {
                if (lastSuggestion != null) {
                    chooseLocation(lastSuggestion);
                }
            }
        });
    }

    private boolean compareSuggestions(ArrayList<SearchSuggestion> s1, ArrayList<SearchSuggestion> s2) {
        if (s1 == null || s2 == null) return false;
        if (s1.size() != s2.size()) return false;
        for (int i = 0; i < s1.size(); ++i) {
            if (!(s1.get(i).getBody().equals(s2.get(i).getBody()))) return false;
        }
        return true;
    }

    private void chooseLocation(final String location) {
        Repository
                .getInstance()
                .getGeocodingCall(location)
                .enqueue(new Callback<GeocodingResponse>() {
                    @Override
                    public void onResponse(
                            Call<GeocodingResponse> call,
                            Response<GeocodingResponse> response
                    ) {
                        if (response.body().addresses.isEmpty()) return;
                        setFinalAddress(response.body().addresses.get(0));
                        showChosenLocation();
                    }

                    @Override
                    public void onFailure(Call<GeocodingResponse> call, Throwable t) {}
                });
    }

    private void showChosenLocation() {
        Address address;

        if (pageNumber == PAGE_FROM) {
            address = Repository.getInstance().getAddressFrom();
        } else {
            address = Repository.getInstance().getAddressTo();
        }

        map.clear();

        if (address == null) {
            searchView.clearQuery();
            return;
        }

        searchView.setSearchBarTitle(address.addressName);

        map.addMarker(new MarkerOptions()
                .position(address.locationGeometry.coordinate.toLatLng())
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                )
                .title(address.addressName)
        );

        Log.d(TAG, "distance is " + address.locationGeometry.viewport.getDistance());

        LatLngBounds bounds = new LatLngBounds
                .Builder()
                .include(address.locationGeometry.viewport.northEast.toLatLng())
                .include(address.locationGeometry.viewport.southWest.toLatLng())
                .build();

        map.animateCamera(CameraUpdateFactory
                .newLatLngBounds(bounds, 0)
        );
    }

    private void setFinalAddress(Address address) {
        if (pageNumber == PAGE_FROM) {
            Repository.getInstance().setAddressFrom(address);
        } else {
            Repository.getInstance().setAddressTo(address);
        }
        ((MainActivity)getActivity()).refreshButton();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        showChosenLocation();
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(false);
    }
}
