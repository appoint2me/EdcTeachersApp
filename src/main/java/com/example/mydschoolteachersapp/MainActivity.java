package com.example.mydschoolteachersapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mydschoolteachersapp.Adapters.AdapterRecyclerViewMain;
import com.example.mydschoolteachersapp.Classes.Config;
import com.example.mydschoolteachersapp.Model.ClassModel;
import com.example.mydschoolteachersapp.Model.SectionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String[] categoryNamesList = {"ATTENDANCE", "ASSIGNMENT"};
    ArrayList listClassName=new ArrayList<ClassModel>();
    ArrayList listSection=new ArrayList<SectionModel>();
    int[] categoryImageList = {R.drawable.ic_menu_camera, R.drawable.assignment_image};
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView= findViewById(R.id.recylerView);
        AdapterRecyclerViewMain adapterRecyclerViewMain=new AdapterRecyclerViewMain(MainActivity.this,categoryNamesList,categoryImageList);
        LinearLayoutManager gridLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapterRecyclerViewMain );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
    public void getUsernames(){
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
                        listClassName.add(classModel);
                        String section_id=jsonArrayClass.getJSONObject(i).getString("section_id ");
                        String section_name=jsonArrayClass.getJSONObject(i).getString("section_name");
                        SectionModel sectionModel=new SectionModel(section_id,section_name);
                        listSection.add(sectionModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
