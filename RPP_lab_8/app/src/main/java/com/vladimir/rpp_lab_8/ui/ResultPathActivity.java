package com.vladimir.rpp_lab_8.ui;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.vladimir.rpp_lab_8.R;
import com.vladimir.rpp_lab_8.utils.Repository;
import com.vladimir.rpp_lab_8.api.directions.models.DirectionsResponse;
import com.vladimir.rpp_lab_8.api.directions.models.Route;
import com.vladimir.rpp_lab_8.api.directions.models.Step;

import java.util.ArrayList;

public class ResultPathActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "ResultPathActivity";

    private LatLng latLngFrom;
    private LatLng latLngTo;
    private LatLng latLngGps;

    private String locationNameFrom;
    private String locationNameTo;

    private LatLngBounds bounds;

    private GoogleMap map;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_path);

        synchronizeData();
        findViewById(R.id.error_layout).setVisibility(View.GONE);

        findViewById(R.id.path_full_size_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bounds != null) moveToBounds();
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.path_map);
        mapFragment.getMapAsync(this);
    }

    private void synchronizeData() {
        latLngFrom = Repository.getInstance().getAddressFrom()
                .locationGeometry.coordinate.toLatLng();
        latLngTo = Repository.getInstance().getAddressTo()
                .locationGeometry.coordinate.toLatLng();
        latLngGps = Repository.getInstance().getGpsLocation();

        locationNameFrom = Repository.getInstance().getAddressFrom().addressName;
        locationNameTo = Repository.getInstance().getAddressTo().addressName;
    }

    private void moveToBounds() {
        map.animateCamera(CameraUpdateFactory
                .newLatLngBounds(bounds, 100)
        );
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;
        googleMap.setBuildingsEnabled(true);

        Repository
                .getInstance()
                .getPathCall(latLngFrom, latLngTo)
                .enqueue(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(
                            Call<DirectionsResponse> call,
                            Response<DirectionsResponse> response
                    ) {
                        if (response.body().routes.isEmpty()) {
                            findViewById(R.id.error_layout).setVisibility(View.VISIBLE);
                            return;
                        }

                        Route route = response.body().routes.get(0);

                        googleMap.addMarker(new MarkerOptions()
                                .position(latLngFrom)
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                                )
                                .title(locationNameFrom)
                        );
                        googleMap.addMarker(new MarkerOptions()
                                .position(latLngTo)
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                                )
                                .title(locationNameTo)
                        );

                        ArrayList<LatLng> dots = new ArrayList<>();
                        dots.add(route.legs.get(0).startAdress.toLatLng());
                        for (Step step : route.legs.get(0).steps) {
                            dots.add(step.endAdress.toLatLng());
                            Log.d(TAG, "step : " + step.endAdress);
                        }

                        googleMap.addPolyline(new PolylineOptions().addAll(dots).width(10));

                        LatLngBounds.Builder boundsBuilder = new LatLngBounds
                                .Builder()
                                .include(route.bounds.northEast.toLatLng())
                                .include(route.bounds.southWest.toLatLng());

                        if (latLngGps != null) {
                            googleMap.addMarker(new MarkerOptions()
                                    .position(latLngGps)
                                    .icon(BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_ROSE)
                                    )
                                    .title("Me")
                            );
                            boundsBuilder.include(latLngGps);
                        }

                        bounds = boundsBuilder.build();

                        moveToBounds();
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                    }
                });
    }

}
