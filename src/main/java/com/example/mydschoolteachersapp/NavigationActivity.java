package com.example.mydschoolteachersapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mydschoolteachersapp.Classes.Config;
import com.example.mydschoolteachersapp.Classes.SharedPrefrenceManager;
import com.example.mydschoolteachersapp.Fragments.AssignmentFragment;
import com.example.mydschoolteachersapp.Fragments.AttendanceFragment;
import com.example.mydschoolteachersapp.Fragments.CalendarFragment;
import com.example.mydschoolteachersapp.Fragments.HomeFragment;
import com.example.mydschoolteachersapp.Fragments.NotificationsFragment;
import com.example.mydschoolteachersapp.Model.ClassModel;
import com.example.mydschoolteachersapp.Model.SectionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    int fragmentId;
    String fragmentName;
    BottomNavigationView bottomNavigationView;
    FragmentTransaction fragmentTransaction;
    private static final String TAG = "NavigationActivity";
    Context mContext;
    Spinner spinnerClassName;
    Spinner spinnerSectionName;
    ArrayList listClass=new ArrayList<ClassModel>();
    ArrayList listSection=new ArrayList<SectionModel>();
    ArrayList listClassName=new ArrayList<String>();
    ArrayList listSectionName=new ArrayList<String>();
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=NavigationActivity.this;
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("DASHBOARD");
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        bottomNavigationView =findViewById(R.id.bottomNavigation);
        //getClassSectionList();
        //    getUserNames();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_navigation, new AttendanceFragment())
                .addToBackStack(null)
                .commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_attendance:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_navigation, new AttendanceFragment()).commit();
                        fragmentTransaction.addToBackStack(null);
                        return true;
                    case R.id.nav_assingment:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_navigation,new AssignmentFragment()).commit();
                        fragmentTransaction.addToBackStack(null);

                        return true;
                    case R.id.nav_calender:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_navigation,new CalendarFragment()).commit();
                        fragmentTransaction.addToBackStack(null);
                        return true;
                    case R.id.nav_notifications:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_navigation,new NotificationsFragment()).commit();
                        fragmentTransaction.addToBackStack(null);
                        return true;

                }
                return false;
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        SharedPreferences sharedPreferences=getSharedPreferences(Config.MY_SHARED_PREFRENCE, Context.MODE_PRIVATE);
        String user_name= SharedPrefrenceManager.getPrefVal(this,Config.USERNAME);
        NavigationView navigationView = findViewById(R.id.nav_view);
        TextView textViewUserName=navigationView.getHeaderView(0).findViewById(R.id.textViewUsername);
        textViewUserName.setText(user_name);
        navigationView.setNavigationItemSelectedListener(this);


    }
    int doubleBackToExitPressed = 1;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //Toast.makeText(this, "Press again to exit the app", Toast.LENGTH_SHORT).show();
            if (doubleBackToExitPressed == 2) {
                finishAffinity();
                System.exit(0);
            }
            else {
                doubleBackToExitPressed++;
                Toast.makeText(this, "Please press Back again to exit", Toast.LENGTH_SHORT).show();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressed=1;
                }
            }, 2000);

        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        int id = item.getItemId();
        if (id == R.id.nav_assingment) {
            // Handle the camera action
            AssignmentFragment assingmentFragment=new AssignmentFragment();
            fragmentTransaction.replace(R.id.content_navigation,assingmentFragment).commit();
            fragmentTransaction.addToBackStack("Assingment Fragment");
        }else if (id == R.id.nav_attendance) {
            AttendanceFragment attendanceFragment =new AttendanceFragment();
            fragmentTransaction.replace(R.id.content_navigation, attendanceFragment).commit();
            fragmentTransaction.addToBackStack("Class Fragment");
        }else if (id == R.id.nav_calender) {
            CalendarFragment calendarFragment=new CalendarFragment();
            fragmentTransaction.replace(R.id.content_navigation,calendarFragment).commit();
            fragmentTransaction.addToBackStack(null);
        }else if (id == R.id.nav_notifications) {
            NotificationsFragment notificationsFragment=new NotificationsFragment();
            fragmentTransaction.replace(R.id.content_navigation,notificationsFragment).commit();
            fragmentTransaction.addToBackStack(null);
        }
        else if (id == R.id.nav_logout) {
            final AlertDialog.Builder alertBuilder=new AlertDialog.Builder(this);
            alertBuilder.setTitle("Logout");
            alertBuilder.setMessage("Do you Really Want to Exit");

            alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPrefrenceManager.setPrefVal(mContext,Config.USERNAME,"");
                    SharedPrefrenceManager.setPrefVal(mContext,Config.PASSWORD,"");
                    SharedPrefrenceManager.setPrefVal(mContext,Config.SCHOOL_ID,"");
                    SharedPrefrenceManager.setPrefVal(mContext,Config.USER_ID,"");
                    SharedPrefrenceManager.setPrefVal(mContext,"SECTION_ID","");
                    SharedPrefrenceManager.setPrefVal(mContext,"CLASS_ID","");
                    Intent intent=new Intent(NavigationActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertBuilder.create();
            alertBuilder.show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
    private void getClassSectionList(){
        String staff_id = SharedPrefrenceManager.getPrefVal(mContext,Config.USER_ID);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Config.URL_GET_CLASS_SECTION_LIST+staff_id
                , new JSONObject()
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status=response.getString("status");
                    if(status.equalsIgnoreCase("1")){
                        JSONObject jsonObject=response.getJSONObject("data");
                        JSONArray jsonArray=jsonObject.getJSONArray("Class_List");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String classId=jsonObject1.getString("class_id");
                            SharedPrefrenceManager.setPrefVal(mContext,"CLASS_ID",classId);
                            // Toast.makeText(mContext, classId, Toast.LENGTH_SHORT).show();
                        }
                        JSONArray jsonArray1=jsonObject.getJSONArray("Section_List");
                        for(int i=0;i<jsonArray1.length();i++){
                            JSONObject jsonObject1=jsonArray1.getJSONObject(i);
                            String sectionId=jsonObject1.getString("section_id");
                            SharedPrefrenceManager.setPrefVal(mContext,"SECTION_ID",sectionId);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_navigation, new AttendanceFragment())
                                    .addToBackStack(null)
                                    .commit();
                            // Toast.makeText(mContext, sectionId, Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(NavigationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
    public void getUserNames(){
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Config.URL_GET_USERNAME, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArrayClass=response.getJSONObject("data").getJSONArray("Class_List");
                    JSONArray jsonArraySection=response.getJSONObject("data").getJSONArray("Class_List");
                    for (int i=0;i<jsonArrayClass.length();i++){
                        String class_id=jsonArrayClass.getJSONObject(i).getString("class_id");
                        String class_name=jsonArrayClass.getJSONObject(i).getString("class_name");
                        ClassModel classModel=new ClassModel(class_name,class_id);
                        listClass.add(classModel);
                        listClassName.add(class_name);
                        String section_id=jsonArrayClass.getJSONObject(i).getString("section_id ");
                        String section_name=jsonArrayClass.getJSONObject(i).getString("section_name");
                        SectionModel sectionModel=new SectionModel(section_id,section_name);
                        listSection.add(sectionModel);
                        listSectionName.add(section_name);
                    }
                    spinnerClassName.setAdapter(new ArrayAdapter<String>(NavigationActivity.this,android.R.layout.simple_list_item_1,listClassName));
                    spinnerSectionName.setAdapter(new ArrayAdapter<String>(NavigationActivity.this,android.R.layout.simple_list_item_1,listSectionName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NavigationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
