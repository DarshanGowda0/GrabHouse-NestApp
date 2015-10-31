package com.darshan.amruth.abhi.nfctest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences("SignIn", Context.MODE_PRIVATE);
        int check = sharedPreferences.getInt("Yes", 0);

        if (!haveNetworkConnection()) {
            callDialog();
        } else {
            if (check == 0) {
                Intent i = new Intent(getApplicationContext(), AuthActivity.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(getApplicationContext(), HousesActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void callDialog() {
        new AlertDialog.Builder(this).setTitle("Warning!").setMessage("Not connected to Internet")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
//                        intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                        startActivity(intent);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SplashActivity.this.finish();
            }
        }).setCancelable(false).show();
    }


}
