package com.vladimir.rpp_lab_8.utils;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.vladimir.rpp_lab_8.api.directions.DirectionsApi;
import com.vladimir.rpp_lab_8.api.directions.models.DirectionsResponse;
import com.vladimir.rpp_lab_8.api.geocoding.GeocodingApi;
import com.vladimir.rpp_lab_8.api.geocoding.models.Address;
import com.vladimir.rpp_lab_8.api.geocoding.models.GeocodingResponse;
import com.vladimir.rpp_lab_8.api.places.PlacesApi;
import com.vladimir.rpp_lab_8.api.places.models.PlacesResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository extends Application {

    private static final String TAG = "Repository";
    private static Repository instance;

    private GeocodingApi geocodingApi;
    private PlacesApi placesApi;
    private DirectionsApi directionsApi;

    private Address addressTo = null;
    private Address addressFrom = null;
    private LatLng gpsLocation = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Log.d("hay", "hello");

        geocodingApi = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GeocodingApi.class);

        placesApi = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PlacesApi.class);

        directionsApi = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DirectionsApi.class);

    }

    public static Repository getInstance() {
        return instance;
    }

    public Address getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(Address addressTo) {
        this.addressTo = addressTo;
    }

    public Address getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(Address addressFrom) {
        this.addressFrom = addressFrom;
    }

    public LatLng getGpsLocation() {
        return gpsLocation;
    }

    public void setGpsLocation(LatLng gpsLocation) {
        this.gpsLocation = gpsLocation;
    }

    public static String latLngToString(LatLng latLng) {
        return latLng.latitude + "," + latLng.longitude;
    }

    public Call<PlacesResponse> getSearchResultsCall(String input) {
        return placesApi.getSearchResults(input, Constants.API_KEY);
    }

    public Call<GeocodingResponse> getGeocodingCall(String location) {
        return geocodingApi.getAddress(location, Constants.API_KEY);
    }

    public Call<DirectionsResponse> getPathCall(LatLng from, LatLng to) {
        return directionsApi.getPath(latLngToString(from), latLngToString(to), Constants.API_KEY);
    }

}
