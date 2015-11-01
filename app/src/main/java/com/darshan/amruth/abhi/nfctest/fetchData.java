package com.darshan.amruth.abhi.nfctest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by darshan on 31/10/15.
 */
public class fetchData extends AsyncTask<String, Void, Boolean> {
    Context context;
    public static ArrayList<DataClass> dataList = new ArrayList<>();

    fetchData(Context con) {
        context = con;
    }
    ProgressDialog progressDialog;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        MainActivity.progressLayout.setVisibility(View.VISIBLE);

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Loading....");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        HousesActivity.recyclerViewAdapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    public static ArrayList<DataClass> list = new ArrayList<>();

    @Override
    protected Boolean doInBackground(String... params) {

        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(params[0]);
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


        return false;
    }


    public static String readStream(InputStream in) {
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


    void parseJson(String data) {

        try {
            JSONArray jsonArray = new JSONArray(data);

            for(int i=0;i<jsonArray.length();i++){
                DataClass dataClass = new DataClass();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                dataClass.id=jsonObject.getString("_id");
                dataClass.image=jsonObject.getString("pic");
                dataClass.price=jsonObject.getString("price");
                dataClass.rating=jsonObject.getString("rating");
                dataClass.address=jsonObject.getString("addr");
                dataClass.category=jsonObject.getString("cat");

                dataList.add(dataClass);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
