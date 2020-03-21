package com.vladimir.rpp_lab_3_2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.vladimir.rpp_lab_3_2.R;
import com.vladimir.rpp_lab_3_2.utils.DbSingleton;
import com.vladimir.rpp_lab_3_2.utils.ListAdapter;

public class ListActivity extends AppCompatActivity {

    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                listAdapter = new ListAdapter(DbSingleton.getInstance(getApplicationContext()).getStudents());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();
            }
        });
        thread.start();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                this,
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

    }
}
