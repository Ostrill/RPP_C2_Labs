package com.vladimir.rpp_lab_3_1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vladimir.rpp_lab_3_1.R;
import com.vladimir.rpp_lab_3_1.utils.DbSingleton;

public class MainActivity extends AppCompatActivity {

    private Button showInfoBtn;
    private Button addRecordBtn;
    private Button changeLastBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showInfoBtn   = findViewById(R.id.show_info_btn);
        addRecordBtn  = findViewById(R.id.plus_record_btn);
        changeLastBtn = findViewById(R.id.change_name_btn);

        DbSingleton.getInstance(getApplicationContext()).refreshDB();

        addListeners();
    }

    private void addListeners() {
        showInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);
            }
        });

        addRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbSingleton.getInstance(getApplicationContext()).insertRandomStudent();
            }
        });

        changeLastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbSingleton.getInstance(getApplicationContext()).changeLast();
            }
        });

    }

}
