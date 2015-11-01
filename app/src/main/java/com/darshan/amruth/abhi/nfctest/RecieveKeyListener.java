package com.darshan.amruth.abhi.nfctest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class RecieveKeyListener extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_key_listener);
        tv = (TextView) findViewById(R.id.tv);
        new checkKeys(getApplicationContext(),tv).execute("");
    }
}
