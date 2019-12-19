package com.example.mydschoolteachersapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.mydschoolteachersapp.Classes.CheckInternetConnection;
import com.example.mydschoolteachersapp.Classes.Config;
import com.example.mydschoolteachersapp.Classes.SharedPrefrenceManager;
import com.example.mydschoolteachersapp.Classes.Singelton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText editTextUserName,editTextPassword;
    Button buttonLogin;
    Context mContext;
    public final int MY_REQUEST_CODE=112;
    private static final String TAG = "LoginActivity";
    SharedPreferences.Editor sharedPrefrenceEditor;
    ProgressDialog progressDialog;
    private String mUserName,mPassword,mUserId,mSchoolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=LoginActivity.this;
        editTextUserName = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        if(isInternetOn()){
            checkLogin();
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextUserName.getText().toString().equals("")) {
                    editTextUserName.setError("Please Enter Username");
                } else if (editTextPassword.getText().toString().equals("")) {
                    editTextPassword.setError("Please Enter Password");
                } else {
                    if (isInternetOn()) {

                        login();
                    }
                }
            }
        });
    }
    //Check internet connection
    private boolean isInternetOn () {

        if (CheckInternetConnection.checkConnection(this)) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.container),
                    " Connected to Internet", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return true;
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.container),
                    "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Go to Settings", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(
                            Settings.ACTION_WIFI_SETTINGS));

                }
            });
            snackbar.show();
            Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    private void checkLogin(){
        String userName=SharedPrefrenceManager.getPrefVal(mContext,Config.USERNAME);
        String password=SharedPrefrenceManager.getPrefVal(mContext,Config.PASSWORD);
        Log.d(TAG, "checkLogin: Username "+userName);
        Log.d(TAG, "checkLogin: Password "+password);
        if (userName.equalsIgnoreCase("") && password.equalsIgnoreCase("")) {

        }
        else
        {
            startActivity(new Intent(mContext,NavigationActivity.class));
        }
    }

    //Method to login
    public void login() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Authenticating");
        progressDialog.show();

        //Getting values from edit texts
        mUserName = editTextUserName.getText().toString().trim();

        mPassword = editTextPassword.getText().toString().trim();
        String LOGIN_URL = Config.URL_LOGIN + "/" + mUserName + "/" + mPassword;
        //Creating a string request
        // RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequestLogin = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject JSON = new JSONObject(response);

                            JSONArray list_json, list_json1;

                            String message = JSON.getString("success");

                            if (message.equals("1")) {
                                progressDialog.dismiss();
                                list_json = JSON.getJSONArray("data");
                                for (int i = 0; i < list_json.length(); i++) {

                                    JSONObject list = list_json.getJSONObject(i);
                                    mUserId = list.get("staff_id").toString();
                                    mSchoolId = list.get("school_id").toString();
                                    SharedPrefrenceManager.setPrefVal(mContext,Config.USER_ID,mUserId);
                                    SharedPrefrenceManager.setPrefVal(mContext,Config.SCHOOL_ID,mSchoolId);
                                    SharedPrefrenceManager.setPrefVal(mContext,Config.PASSWORD,mPassword);
                                    SharedPrefrenceManager.setPrefVal(mContext,Config.USERNAME,mUserName);
                                    sendFirebaseTokenToServer();
                                    Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                                    startActivity(intent);
                                }
                            } else {


                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });

        //Adding the string request to the queue
        Singelton.getInstance(this).addToRequestQue(stringRequestLogin);

    }
private void sendFirebaseTokenToServer()
{
   String fireBaseToken= SharedPrefrenceManager.getPrefVal(this,Config.FIREBASE_TOKEN);
    Log.d(TAG, "sendFirebaseTokenToServer: "+fireBaseToken);
   // Toast.makeText(mContext, fireBaseToken, Toast.LENGTH_SHORT).show();

    JsonObjectRequest jsonObjectRequestSendToken=new JsonObjectRequest(Request.Method.GET
            , Config.SEND_FIREBASE_TOKEN_TO_SERVER+mUserId+"/"+fireBaseToken
            , new JSONObject(), new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
           // Toast.makeText(mContext, response.toString(), Toast.LENGTH_SHORT).show();
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
           // Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
    Singelton.getInstance(this).addToRequestQue(jsonObjectRequestSendToken);

}
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
    }

}