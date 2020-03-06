package com.vladimir.rpp_lab_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        TechnologyRepository.initRepository(this);
    }

    public void closeActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void showNoInternet() {
        ProgressBar loading = findViewById(R.id.progressBar);
        loading.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "Отсутсвует интернет-соединение", Toast.LENGTH_LONG).show();
    }
}
