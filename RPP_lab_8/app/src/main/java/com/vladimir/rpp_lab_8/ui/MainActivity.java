package com.vladimir.rpp_lab_8.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.vladimir.rpp_lab_8.R;
import com.vladimir.rpp_lab_8.utils.Repository;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static boolean locationPermissionGranted;

    static final String TAG = "myLogs";
    static final int PAGE_COUNT = 2;

    private ViewPager pager;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = findViewById(R.id.main_view_pager);
        PagerAdapter pagerAdapter = new SetPointViewPagerAdapter(
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        );
        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected, position = " + position);
                refreshButton();
            }

            @Override
            public void onPageScrolled(
                    int position,
                    float positionOffset,
                    int positionOffsetPixels
            ) {}

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        TabLayout tabs = findViewById(R.id.main_tab_layout);
        tabs.setupWithViewPager(pager);

        getLocationPermission();
        setGpsLocation();

        button = findViewById(R.id.main_button);
        refreshButton();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getText() == getString(R.string.button_next_label)) {
                    pager.setCurrentItem((pager.getCurrentItem()+1)%2, true);
                } else {
                    Intent intent = new Intent(getApplicationContext(), ResultPathActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void refreshButton() {
        boolean enabled = true;
        if (pager.getCurrentItem() == SetPointFragment.PAGE_FROM) {
            if (Repository.getInstance().getAddressFrom() == null) {
                enabled = false;
            }
        } else {
            if (Repository.getInstance().getAddressTo() == null) {
                enabled = false;
            }
        }
        button.setEnabled(enabled);
        if (Repository.getInstance().getAddressFrom() != null &&
                Repository.getInstance().getAddressTo() != null) {
            button.setText(getString(R.string.button_finish_label));
        } else {
            button.setText(getString(R.string.button_next_label));
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                setGpsLocation();
            }
        }
    }

    private void setGpsLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = LocationServices
                        .getFusedLocationProviderClient(this)
                        .getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            Repository
                                    .getInstance()
                                    .setGpsLocation(new LatLng(
                                            task.getResult().getLatitude(),
                                            task.getResult().getLongitude()
                                    ));
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: ", e.getMessage());
        }
    }

    private class SetPointViewPagerAdapter extends FragmentPagerAdapter {
        SetPointViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return SetPointFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return getString(R.string.tab_from_label);
            return getString(R.string.tab_to_label);
        }
    }
}
