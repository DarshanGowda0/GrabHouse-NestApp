package com.darshan.amruth.abhi.nfctest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_auth);
        Log.d("darshan", "inside of auth");

        preferences = getSharedPreferences("SignIn", Context.MODE_PRIVATE);
        editor = preferences.edit();

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                String id = loginResult.getAccessToken().getUserId();
                Toast.makeText(getApplicationContext(), "logged in with user id " + id, Toast.LENGTH_SHORT).show();
                //commit to shared preferences
                editor.putInt(SplashActivity.LOGGED_IN, 1);
                editor.apply();
                //start next activity
                Intent in = new Intent(AuthActivity.this, HousesActivity.class);
                startActivity(in);
                callGraph(loginResult.getAccessToken());
                finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void callGraph(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        parseResponse(response);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void parseResponse(GraphResponse response) {
        try {
            Log.d("drahsn",""+response);

            JSONObject graphObject = response.getJSONObject();



            String id, name, link;
            id = graphObject.getString("id");
            name = graphObject.getString("name");
            link = graphObject.getString("link");

            editor.putString("id",id);
            editor.putString("name",name);
            editor.putString("link",link);
            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
