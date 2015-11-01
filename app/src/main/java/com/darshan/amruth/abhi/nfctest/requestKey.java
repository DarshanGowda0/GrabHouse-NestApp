package com.darshan.amruth.abhi.nfctest;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by darshan on 01/11/15.
 */
public class requestKey extends AsyncTask<Void, Void, Void> {

    Context context;
    String home_id;
    public static String HOME_ID = "house_id", USER_ID = "fbid", USER_NAME = "fbname", USER_LINK = "fblink";
    View view;


    public requestKey(Context context, String Home_id, View view) {
        this.context = context;
        home_id = Home_id;
        this.view = view;
    }

    BufferedReader mBufferedInputStream;
    String Response = "";
    SharedPreferences sharedPreferences;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Snackbar.make(view, "requesting key", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Snackbar.make(view, "requesting key", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    @Override
    protected Void doInBackground(Void... params) {


        String DEFAULT = "something went wrong";
        sharedPreferences = context.getSharedPreferences("SignIn", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", DEFAULT);
        String name = sharedPreferences.getString("name", DEFAULT);
        String link = sharedPreferences.getString("link", DEFAULT);

        try {
            URL url = new URL("http://84.200.84.218:3000/rqst_key");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter(USER_ID, id)
                    .appendQueryParameter(USER_NAME, name)
                    .appendQueryParameter(USER_LINK, link)
                    .appendQueryParameter(HOME_ID, home_id);
//            Log.d("pageno", "" + page_no);

            String query = builder.build().getEncodedQuery();

            OutputStream os = httpURLConnection.getOutputStream();

            BufferedWriter mBufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            mBufferedWriter.write(query);
            mBufferedWriter.flush();
            mBufferedWriter.close();
            os.close();

            httpURLConnection.connect();


            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                mBufferedInputStream = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inline;
                while ((inline = mBufferedInputStream.readLine()) != null) {
                    Response += inline;
                }
                mBufferedInputStream.close();

//                parseJson(Response);
                Log.d("DARSHAN", Response);
//                parseJSON(Response);

            } else {
                Log.d("darshan", "something wrong");

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
