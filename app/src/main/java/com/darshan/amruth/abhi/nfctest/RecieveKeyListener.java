package com.darshan.amruth.abhi.nfctest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class RecieveKeyListener extends AppCompatActivity {

    ImageView keyStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_key_listener);

        keyStatus = (ImageView) findViewById(R.id.image_keyStatus);
        new checkKeys(getApplicationContext(), keyStatus).execute("");
    }
}
