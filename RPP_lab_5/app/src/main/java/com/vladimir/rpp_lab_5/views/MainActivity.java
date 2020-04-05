package com.vladimir.rpp_lab_5.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vladimir.rpp_lab_5.R;
import com.vladimir.rpp_lab_5.views.fragments.LikesFragment;
import com.vladimir.rpp_lab_5.views.fragments.PictureFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private PictureFragment pictureFragment;
    private LikesFragment likesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomMenu = findViewById(R.id.bottomMenu);
        bottomMenu.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                   @Override
                   public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                       switch (item.getItemId()) {
                           case R.id.menu_pictures:
                               openFragment(getPictureFragment());
                               break;
                           case R.id.menu_likes:
                               openFragment(getLikesFragment());
                               break;
                       }
                       return true;
                   }
               });

        if (savedInstanceState == null) {
            openFragment(getPictureFragment());
        }
    }

    public PictureFragment getPictureFragment() {
        if (pictureFragment == null) {
            pictureFragment = new PictureFragment();
        }
        return pictureFragment;
    }

    public LikesFragment getLikesFragment() {
        if (likesFragment == null) {
            likesFragment = new LikesFragment();
        }
        return likesFragment;
    }

    public void openFragment(Fragment fragment) {
        if (fragment.isVisible()) return;

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

}
