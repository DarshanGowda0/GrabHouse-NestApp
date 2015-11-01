package com.darshan.amruth.abhi.nfctest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecieveKeyListener extends AppCompatActivity {

    ImageView keyStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_key_listener);

        keyStatus = (ImageView) findViewById(R.id.idGod);

        new checkKeysInner(getApplicationContext()).execute("");
    }

    public class checkKeysInner extends AsyncTask<String, Void, Boolean> {

        Context context;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;

        public checkKeysInner(Context con) {

            context = con;
            sharedPreferences = context.getSharedPreferences("SignIn", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//        MainActivity.progressLayout.setVisibility(View.VISIBLE);

//        progressDialog = new ProgressDialog(context);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.setMessage("Loading....");
//        progressDialog.setIndeterminate(true);
//        progressDialog.show();

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                keyStatus.setImageResource(R.drawable.key_received);
            }else{

            }
        }


        @Override
        protected Boolean doInBackground(String... params) {

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
                    boolean wat = parseJson(responseString);
                    return wat;
                } else {
                    Log.v("CatalogClient", "Response code:" + responseCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }


            return false;
        }

        private boolean parseJson(String responseString) {

            try {
                JSONArray jsonArray = new JSONArray(responseString);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String id = jsonObject.getString("house_id");
                String name = jsonObject.getString("fbname");
                String fbid = jsonObject.getString("fbid");
                String link = jsonObject.getString("link");
                try {
                    int passcode = jsonObject.getInt("passcode");

                    Log.d("passcode", passcode + "");
                    editor.putInt("passcode", passcode);
                    editor.apply();
                    return true;
                } catch (JSONException e) {
                    Log.d("darshan", "no keys are yet received");
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        public String readStream(InputStream in) {
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


    }
}
