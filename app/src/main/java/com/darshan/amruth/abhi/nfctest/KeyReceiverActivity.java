package com.darshan.amruth.abhi.nfctest;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class KeyReceiverActivity extends Activity {

    private ImageView keyStatus;
    int passcode;
    String RecievedString ;

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
            RecievedString =message.toString();
            keyStatus.setImageResource(R.drawable.lock_open);
        } else
            Log.d("Waiting: ","For NDEF Message");

    }

    public class checkCorrectness extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL("http://84.200.84.218:3000/get_passcode");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                int responseCode = urlConnection.getResponseCode();

                if (responseCode == 200) {
                    String responseString = readStream(urlConnection.getInputStream());
                    Log.v("CatalogClient", responseString);
                    parseJson(responseString);
                } else {
                    Log.v("CatalogClient", "Response code:" + responseCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }

            return null;
        }

        private void parseJson(String responseString) {

            try {
                JSONArray jsonArray = new JSONArray(responseString);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String id = jsonObject.getString("house_id");
                String name = jsonObject.getString("fbname");
                String fbid = jsonObject.getString("fbid");
                String link = jsonObject.getString("link");
                try {
                    passcode = jsonObject.getInt("passcode");

                } catch (JSONException e) {
                    Log.d("darshan","no keys are yet received");
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public  String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer response = new StringBuffer();
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(RecievedString.equals(""+passcode)){
                keyStatus.setImageResource(R.drawable.lock_open);
                Toast.makeText(getApplicationContext(),"lock opened successfully",Toast.LENGTH_SHORT).show();
            }else{
                keyStatus.setImageResource(R.drawable.lock);
                Toast.makeText(getApplicationContext(),"lock cannot be opened",Toast.LENGTH_SHORT).show();

            }
        }
    }



}
