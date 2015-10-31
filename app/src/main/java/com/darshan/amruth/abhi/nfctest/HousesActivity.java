package com.darshan.amruth.abhi.nfctest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class HousesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    public static RecyclerViewAdapter recyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_houses);

        new fetchData(HousesActivity.this).execute("http://84.200.84.218:3000/get_prop");

        //set up recycler view
        setUpRecView();

    }

    private void setUpRecView() {


        recyclerView = (RecyclerView) findViewById(R.id.recView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(HousesActivity.this);
        try {
            recyclerView.setAdapter(recyclerViewAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
