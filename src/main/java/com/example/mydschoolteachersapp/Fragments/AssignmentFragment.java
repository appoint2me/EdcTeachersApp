package com.example.mydschoolteachersapp.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mydschoolteachersapp.Adapters.AdapterRecyclerViewAssignment;
import com.example.mydschoolteachersapp.Adapters.AdapterRecyclerViewGetAssignment;
import com.example.mydschoolteachersapp.Adapters.AdapterRecyclerViewSectionAssignment;
import com.example.mydschoolteachersapp.Classes.CheckInternetConnection;
import com.example.mydschoolteachersapp.Classes.Config;
import com.example.mydschoolteachersapp.Classes.SharedPrefrenceManager;
import com.example.mydschoolteachersapp.Classes.Singelton;
import com.example.mydschoolteachersapp.MainActivity;
import com.example.mydschoolteachersapp.Model.AssignmentModel;
import com.example.mydschoolteachersapp.Model.ClassModel;
import com.example.mydschoolteachersapp.Model.SectionModel;
import com.example.mydschoolteachersapp.NavigationActivity;
import com.example.mydschoolteachersapp.R;
import com.example.mydschoolteachersapp.UploadAssignmentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssignmentFragment extends Fragment {
    RecyclerView recyclerViewAssignment;
    public ArrayList<ClassModel> classModelList;
    Context mContext;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList listClass = new ArrayList<ClassModel>();
    ArrayList listSection = new ArrayList<SectionModel>();
    ArrayList listClassName = new ArrayList<String>();
    ArrayList listSectionName = new ArrayList<String>();
    ArrayList<AssignmentModel> mListAssignment = new ArrayList<>();
    String mClassId, mSectionId;
    FloatingActionButton floatingActionButton;
    Button buttonGet;
    Spinner spinnerClassName;
    Spinner spinnerSectionName;
    TextView textViewNothing;
    private static final String TAG = "AssignmentFragment";

    public AssignmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assingment, container, false);
        mContext = view.getContext();
        classModelList = new ArrayList<>();
        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle("Assignment");
        recyclerViewAssignment = view.findViewById(R.id.recylerViewAssignment);
        progressBar = view.findViewById(R.id.progressBar);
        spinnerClassName = view.findViewById(R.id.spinnerClassName1);
        spinnerSectionName = view.findViewById(R.id.spinnerSectionName1);
        buttonGet = (Button) view.findViewById(R.id.buttonGet);
        textViewNothing = (TextView) view.findViewById(R.id.textViewNothing);

        floatingActionButton = view.findViewById(R.id.fabUpload);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UploadAssignmentActivity.class);
                intent.putExtra("CLASS_ID", mClassId);
                intent.putExtra("SECTION_ID", mSectionId);
                mContext.startActivity(intent);
            }
        });

        if (CheckInternetConnection.checkConnection(view.getContext())) {
            getClassAndSectionList();

            buttonGet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (spinnerClassName.getSelectedItem().toString().equals("Select Class")) {
                        Toast.makeText(mContext, "Please select class", Toast.LENGTH_SHORT).show();
                    } else if (spinnerSectionName.getSelectedItem().toString().equals("Select Section")) {
                        Toast.makeText(mContext, "Please select section", Toast.LENGTH_SHORT).show();

                    } else {
                        getAssignmentList();
                    }
                }
            });

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

    public void getClassAndSectionList() {
        listClassName.add("Select Class");
        listSectionName.add("Select Section");
        String mUserId = SharedPrefrenceManager.getPrefVal(mContext, Config.USER_ID);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Config.URL_GET_USERNAME + mUserId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //   Toast.makeText(mContext, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArrayClass = response.getJSONObject("data").getJSONArray("Class_List");
                    JSONArray jsonArraySection = response.getJSONObject("data").getJSONArray("Section_List");
                    for (int i = 0; i < jsonArrayClass.length(); i++) {
                        String class_id = jsonArrayClass.getJSONObject(i).getString("class_id");
                        String class_name = jsonArrayClass.getJSONObject(i).getString("class_name");

                        String section_id = jsonArraySection.getJSONObject(i).getString("section_id");
                        String section_name = jsonArraySection.getJSONObject(i).getString("section_name");
                        ClassModel classModel = new ClassModel(class_name, class_id);


                        SectionModel sectionModel = new SectionModel(section_id, section_name);
                        listSectionName.add(section_name);
                        listSection.add(sectionModel);

                        listClassName.add(class_name);
                        listClass.add(classModel);
                    }

                    ArrayAdapter arrayAdapterClass = new ArrayAdapter<String>(mContext, R.layout.spinner_item, listClassName);
                    ArrayAdapter arrayAdapterSection = new ArrayAdapter<String>(mContext, R.layout.spinner_item, listSectionName);
                    spinnerClassName.setAdapter(arrayAdapterClass);
                    spinnerSectionName.setAdapter(arrayAdapterSection);
                    spinnerClassName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {

                            } else {
                                ClassModel classModel = (ClassModel) listClass.get(position - 1);
                                mClassId = classModel.getClassId();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    spinnerSectionName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {

                            } else {
                                SectionModel sectionModel = (SectionModel) listSection.get(position - 1);
                                mSectionId = sectionModel.getSectionId();

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
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
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);
    }

    public void getAssignmentList() {

        mListAssignment.clear();
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Getting Previous Assignment List !!");
        progressDialog.show();
        StringRequest stringRequestGetAssignment = new StringRequest(Request.Method.POST, Config.URL_GET_ASSIGNMENT_LIST1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                textViewNothing.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (Integer.parseInt(jsonObject.getString("success")) == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("assigment_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            AssignmentModel assignmentModel = new AssignmentModel(jsonObject1.getString("assignment_id"),
                                    jsonObject1.getString("title"),
                                    jsonObject1.getString("document_path"));
                            progressDialog.dismiss();
                            mListAssignment.add(assignmentModel);
                        }
                        AdapterRecyclerViewGetAssignment adapterRecyclerViewGetAssignment = new AdapterRecyclerViewGetAssignment(mContext, mListAssignment);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                        recyclerViewAssignment.setLayoutManager(layoutManager);
                        recyclerViewAssignment.setHasFixedSize(true);
                        recyclerViewAssignment.setAdapter(adapterRecyclerViewGetAssignment);
                        adapterRecyclerViewGetAssignment.notifyDataSetChanged();
                    } else {
                        textViewNothing.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "No Previous Assignment Found !!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    textViewNothing.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                textViewNothing.setVisibility(View.VISIBLE);
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //send data to server and get respons from its in respons
                params.put("class_id", mClassId);
                params.put("section_id", mSectionId);
                return params;
            }
        };
        Singelton.getInstance(mContext).addToRequestQue(stringRequestGetAssignment);
    }
}