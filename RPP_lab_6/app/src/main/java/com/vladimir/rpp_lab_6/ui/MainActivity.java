package com.vladimir.rpp_lab_6.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vladimir.rpp_lab_6.R;
import com.vladimir.rpp_lab_6.ui.fragments.BackFragment;
import com.vladimir.rpp_lab_6.ui.fragments.FrontFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private BackFragment backFragment;
    private FrontFragment frontFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomMenu = findViewById(R.id.bottom_menu);
        bottomMenu.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_back:
                                openFragment(getBackFragment());
                                break;
                            case R.id.menu_front:
                                openFragment(getFrontFragment());
                                break;
                        }
                        return true;
                    }
                });

        if (savedInstanceState == null) {
            openFragment(getFrontFragment());
        }
    }

    public BackFragment getBackFragment() {
        if (backFragment == null) {
            backFragment = new BackFragment();
        }
        return backFragment;
    }

    public FrontFragment getFrontFragment() {
        if (frontFragment == null) {
            frontFragment = new FrontFragment();
        }
        return frontFragment;
    }

    public void openFragment(Fragment fragment) {
        if (fragment.isVisible()) return;

        if (fragment == backFragment) {
            setTitle("Backend");
        } else {
            setTitle("Frontend");
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
