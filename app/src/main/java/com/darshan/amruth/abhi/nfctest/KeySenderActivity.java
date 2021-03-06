package com.darshan.amruth.abhi.nfctest;

import android.app.Activity;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;


public class KeySenderActivity extends Activity implements NfcAdapter.CreateNdefMessageCallback {

    SharedPreferences sharedPreferences;

    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_sender);
        mEditText = (EditText) findViewById(R.id.edit_text_field);
        sharedPreferences = getSharedPreferences("SignIn",MODE_PRIVATE);
        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            mEditText.setText("Sorry this device does not have NFC.");
            return;
        }

        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
        }

        mAdapter.setNdefPushMessageCallback(this, this);
    }

    /**
     * Ndef Record that will be sent over via NFC
     * @param nfcEvent
     * @return
     */
    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
//        String message = mEditText.getText().toString();

        String msg = sharedPreferences.getString("passcode","");
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", msg.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        Log.d("nandhan",msg+" is going");
        return ndefMessage;
    }
}
