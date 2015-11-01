package com.darshan.amruth.abhi.nfctest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HouseDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView price, category, address, address2;
    ImageView houseImage;
    CardView cardView;
    int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUp();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapSmall);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new requestKey(HouseDetailsActivity.this, fetchData.dataList.get(pos).id, view).execute();

            }
        });
    }

    private void setUp() {

        price = (TextView) findViewById(R.id.money);
        category = (TextView) findViewById(R.id.category);
        address = (TextView) findViewById(R.id.addr);
        address2 = (TextView) findViewById(R.id.addressTv2);
        houseImage = (ImageView) findViewById(R.id.houseImageBig);
        Intent in = getIntent();
        pos = in.getIntExtra("position", 0);
        cardView = (CardView) findViewById(R.id.card_view6);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:9742934099");
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });

        String rentCost = "\u20B9" + fetchData.dataList.get(pos).price;
        price.setText(rentCost);
        category.setText(fetchData.dataList.get(pos).category);
        address.setText(fetchData.dataList.get(pos).address);
        address2.setText(fetchData.dataList.get(pos).address);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage("http://84.200.84.218/grab/imagesa/" + fetchData.dataList.get(pos).image, houseImage, RecyclerViewAdapter.defaultOptions);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(12.927569, 77.620082);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Kormangala Bengaluru"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_house_details, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent in = new Intent(HouseDetailsActivity.this, RecieveKeyListener.class);
            startActivity(in);
        }
        if (id == R.id.open) {
            Intent in = new Intent(HouseDetailsActivity.this, KeySenderActivity.class);
            startActivity(in);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
