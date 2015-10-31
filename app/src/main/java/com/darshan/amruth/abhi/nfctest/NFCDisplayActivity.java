package com.darshan.amruth.abhi.nfctest;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class NFCDisplayActivity extends Activity {

    private ImageView keyStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_display);

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
            keyStatus.setImageResource(R.drawable.lock);

            Toast.makeText(getApplicationContext(),""+new String(message.getRecords()[0].getPayload()),Toast.LENGTH_SHORT).show();
        } else
            Log.d("Waiting: ","For NDEF Message");

    }
}
