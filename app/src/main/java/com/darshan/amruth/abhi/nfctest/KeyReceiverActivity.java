package com.darshan.amruth.abhi.nfctest;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.ImageView;

public class KeyReceiverActivity extends Activity {

    private ImageView keyStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_receiver);

        keyStatus = (ImageView) findViewById(R.id.image_keyStatus);
        keyStatus.setImageResource(R.drawable.lock);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0];
            keyStatus.setImageResource(R.drawable.lock_open);
        } else
            Log.d("Waiting: ","For NDEF Message");

    }
}
