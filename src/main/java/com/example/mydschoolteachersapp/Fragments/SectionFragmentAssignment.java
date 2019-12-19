package com.example.mydschoolteachersapp.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.mydschoolteachersapp.Adapters.AdapterRecyclerViewSectionAssignment;
import com.example.mydschoolteachersapp.Classes.CheckInternetConnection;
import com.example.mydschoolteachersapp.Classes.Config;
import com.example.mydschoolteachersapp.Classes.Singelton;
import com.example.mydschoolteachersapp.Model.SectionModel;
import com.example.mydschoolteachersapp.NavigationActivity;
import com.example.mydschoolteachersapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SectionFragmentAssignment extends Fragment {
    RecyclerView mRecyclerViewSection;
    ArrayList<SectionModel> mListSection;
    Context mContext;
    String mSchoolId="";
    ProgressBar progressBar;
    String mClassId=null;

    public SectionFragmentAssignment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_section_fragment_assignment, container, false);
        mContext=view.getContext();
        mRecyclerViewSection= view.findViewById(R.id.recyclerViewSection);
        mListSection=new ArrayList<>();
        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle("Section List");
        progressBar= view.findViewById(R.id.progressBar);
        mRecyclerViewSection.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Bundle bundle=getArguments();
        mClassId= bundle.getString("CLASS_ID");
      //  Toast.makeText(getContext(), "Section :"+mClassId, Toast.LENGTH_SHORT).show();
        if (CheckInternetConnection.checkConnection(view.getContext())) {
//            Snackbar snackbar = Snackbar.make(view.findViewById(R.id.container),
//                    " Connected to Internet", Snackbar.LENGTH_SHORT);
//            snackbar.show();
            //Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
            getSectionList();
        } else {
            Snackbar snackbar = Snackbar.make(view.findViewById(R.id.container),
                    "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Go to Settings", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(
                            Settings.ACTION_WIFI_SETTINGS));

                }
            });
            snackbar.show();
            // Toast.makeText(view.getContext(), "Not Connected", Toast.LENGTH_SHORT).show();

        }

        return view;
    }
    public void getSection()
    {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Config.MY_SHARED_PREFRENCE, Context.MODE_PRIVATE);
        mSchoolId = sharedPreferences.getString(Config.SCHOOL_ID, "");
        StringRequest stringRequestGetSection=new StringRequest(Request.Method.POST, Config.URL_GET_SECTION_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    mRecyclerViewSection.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                  //  System.out.println("SECTION RESPONSE"+response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("secationList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        SectionModel sectionModel = new SectionModel(jsonObject1.getString("section_id"),
                                jsonObject1.getString("section_name"));
                        mListSection.add(sectionModel);

                    }

                    AdapterRecyclerViewSectionAssignment adapterRecyclerViewSectionAssignment = new AdapterRecyclerViewSectionAssignment(mContext,mListSection,mClassId);
                    GridLayoutManager layoutManager = new GridLayoutManager(mContext,2);                    mRecyclerViewSection.setLayoutManager(layoutManager);
                    mRecyclerViewSection.setAdapter(adapterRecyclerViewSectionAssignment);
                    adapterRecyclerViewSectionAssignment.notifyDataSetChanged();

                } catch (Exception e) {

                    e.printStackTrace();
                   // System.out.println("Section Exception:"+e.getMessage());

                    mRecyclerViewSection.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // System.out.println("SECTION ERROR:"+error.getMessage());
                mRecyclerViewSection.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(mContext, "Something Went Wrong", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params=new HashMap<>();
                params.put("school_id",mSchoolId);
              //  System.out.println("SECTION SCHOOL ID:"+mSchoolId);
                return params;
            }
        };
        Singelton.getInstance(getContext()).addToRequestQue(stringRequestGetSection);
    }
    private void getSectionList() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.MY_SHARED_PREFRENCE, Context.MODE_PRIVATE);
        final String school_id = sharedPreferences.getString(Config.SCHOOL_ID, null);
        final String staff_id = sharedPreferences.getString(Config.USER_ID, null);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Config.URL_GET_ASSINGMENT_LIST + staff_id, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //  Toast.makeText(mContext, response.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                mRecyclerViewSection.setVisibility(View.VISIBLE);
                try {
                    String status = response.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONArray jsonArray = jsonObject.getJSONArray("Section_List");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String section_id = jsonObject1.getString("section_id");
                            String section_name = jsonObject1.getString("section_name");

                            SectionModel sectionModel = new SectionModel(section_id,section_name);
                            mListSection.add(sectionModel);
                        }
                        AdapterRecyclerViewSectionAssignment adapterRecyclerViewSectionAssignment = new AdapterRecyclerViewSectionAssignment(mContext, mListSection, mClassId);
                        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
                        mRecyclerViewSection.setLayoutManager(layoutManager);
                        mRecyclerViewSection.setAdapter(adapterRecyclerViewSectionAssignment);
                        adapterRecyclerViewSectionAssignment.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        Singelton.getInstance(mContext).addToRequestQue(jsonObjectRequest);
    }

}
