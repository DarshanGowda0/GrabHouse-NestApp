package com.darshan.amruth.abhi.nfctest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class SplashActivity extends AppCompatActivity {

    public static String LOGGED_IN = "LoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Iconify.with(new FontAwesomeModule());

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("SignIn", Context.MODE_PRIVATE);
                int check = sharedPreferences.getInt(LOGGED_IN, 0);

                if (!haveNetworkConnection()) {
                    callDialog();
                } else {
                    if (check == 0) {
                        Log.d("darshan", "going to auth");
                        Intent i = new Intent(getApplicationContext(), AuthActivity.class);
                        startActivity(i);
                        finish();
                        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    } else {
                        Log.d("darshan", "going to Houses");

                        Intent i = new Intent(getApplicationContext(), HousesActivity.class);
                        startActivity(i);
                        finish();
                        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }

                // close this activity
//                finish();
            }
        }, 2000);
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
        Log.d("darshan", "inside call dialog");

        new AlertDialog.Builder(this).setTitle("Warning!").setMessage("Not connected to Internet")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
//                        intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SplashActivity.this.finish();
                    }
                }).setCancelable(false).show();


    }


}
