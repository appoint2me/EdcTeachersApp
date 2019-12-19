package com.example.mydschoolteachersapp.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mydschoolteachersapp.Adapters.AdapterRecyclerViewStudentData;
import com.example.mydschoolteachersapp.Classes.CheckInternetConnection;
import com.example.mydschoolteachersapp.Classes.Config;
import com.example.mydschoolteachersapp.Classes.SharedPrefrenceManager;
import com.example.mydschoolteachersapp.Classes.Singelton;
import com.example.mydschoolteachersapp.Model.ClassModel;
import com.example.mydschoolteachersapp.Model.SectionModel;
import com.example.mydschoolteachersapp.Model.StudentDataModel;
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
public class AttendanceFragment extends Fragment {
    public ArrayList<ClassModel> mListClass;
    Context mContext;
    ArrayList listClass = new ArrayList<ClassModel>();
    ArrayList listSection = new ArrayList<SectionModel>();
    ArrayList listClassName = new ArrayList<String>();
    ArrayList listSectionName = new ArrayList<String>();
    Button buttonGet;
    Spinner spinnerClassName;
    Spinner spinnerSectionName;
    TextView textViewNothing;
    ArrayList<SectionModel> mListSection=new ArrayList<>();
    ArrayList<ClassModel> classModelList = new ArrayList<>();
    String mClassId,mSectionId;
    String application_id=null;
    String Status=null;
    TextView textViewPresentCount,textViewAbsentCount;
    private static final String TAG = "AttendanceFragment";
    ArrayList<StudentDataModel> mListStudentData=new ArrayList<>();
    RecyclerView recyclerViewStudentData;
    String current_status;
    String Statusofstd = "P";
    String item_status=null;
    Button buttonSubmit;
    AdapterRecyclerViewStudentData adapterRecyclerViewStudentData;

    ProgressDialog progressDialog;
    private JSONObject jsonObject=new JSONObject();
    CheckBox checkBoxSelectAll;
    CardView cardViewHeader;
    private static int firstVisibleInListview;

   static int y;

    public AttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_class, container, false);
        mContext = view.getContext();
        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle("Attendance");
        textViewPresentCount = view.findViewById(R.id.textViewPresentCount);
        textViewAbsentCount = view.findViewById(R.id.textViewAbsentCount);
        buttonSubmit= view.findViewById(R.id.buttonSubmit);
        checkBoxSelectAll= view.findViewById(R.id.checkBoxSelectAll);
        spinnerClassName = view.findViewById(R.id.spinnerClassName1);
        spinnerSectionName = view.findViewById(R.id.spinnerSectionName1);
        buttonGet = (Button) view.findViewById(R.id.buttonGet);
        textViewNothing = (TextView) view.findViewById(R.id.textViewNothing);
        textViewNothing.setVisibility(View.GONE);
        cardViewHeader=(CardView)view.findViewById(R.id.cardView);
        recyclerViewStudentData= view.findViewById(R.id.recyclerViewStudentData);
       // cardViewHeader.setVisibility(View.GONE);
       // recyclerViewStudentData.setVisibility(View.GONE);

        if (CheckInternetConnection.checkConnection(mContext)) {
        // getAttendance();
            getClassAndSectionList();
            buttonGet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (spinnerClassName.getSelectedItem().toString().equals("Select Class")) {
                        Toast.makeText(mContext, "Please select class", Toast.LENGTH_SHORT).show();
                    } else if (spinnerSectionName.getSelectedItem().toString().equals("Select Section")) {
                        Toast.makeText(mContext, "Please select section", Toast.LENGTH_SHORT).show();

                    } else {
                        getAttendance();
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

        }

        checkBoxSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked) {
                        adapterRecyclerViewStudentData.selectAll(true);
                    } else {
                        adapterRecyclerViewStudentData.selectAll(false);
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }
        });

        textViewPresentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              getPresentCount();
            }
        });


        textViewAbsentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAbsentCount();
            }
        });

//        recyclerViewStudentData.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if(recyclerViewStudentData.SCROLL_STATE_DRAGGING==newState){
//                    //fragProductLl.setVisibility(View.GONE);
//                }
//                if(recyclerViewStudentData.SCROLL_STATE_IDLE==newState){
//                    // fragProductLl.setVisibility(View.VISIBLE);
//                    if(y<=0){
//                        cardViewHeader.setVisibility(View.VISIBLE);
//                      //  cardViewHeader.animate().translationY(cardViewHeader.getHeight());
//                        Log.d(TAG, "onScrollStateChanged: "+y);
//                    }
//                    else{
//                        y=0;
//                        cardViewHeader.setVisibility(View.GONE);
//                      //  cardViewHeader.animate().translationY(0);
//                        Log.d(TAG, "onScrollStateChanged: "+y);
//                    }
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                y=dy;
//            }
//        });
        return view;
    }

    //get attendance by classId and sectionId
    public void getClassAndSectionList() {
        listClassName.add("Select Class");
        listSectionName.add("Select Section");
        String userId = SharedPrefrenceManager.getPrefVal(mContext, Config.USER_ID);
        String schoolId = SharedPrefrenceManager.getPrefVal(mContext, Config.SCHOOL_ID);
      //  Toast.makeText(mContext, "staffId="+userId+" schoolId="+schoolId, Toast.LENGTH_SHORT).show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Config.URL_GET_USERNAME + userId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //   Toast.makeText(mContext, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    if(response.equals("[]")){
                        Toast.makeText(mContext, "No class and section found", Toast.LENGTH_SHORT).show();
                    }else {
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
    public void getAttendance() {
//    mClassId=SharedPrefrenceManager.getPrefVal(mContext,"CLASS_ID");
//    mSectionId=SharedPrefrenceManager.getPrefVal(mContext,"SECTION_ID");

        final ProgressDialog progressDialog=new ProgressDialog(mContext);
        progressDialog.setTitle("Students Details");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        StringRequest stringRequestGetClassList=new StringRequest(Request.Method.POST, Config.URL_GET_CLASS_STUDENTS_LIST, new Response.Listener<String>() {
            int presentCount=0;
            int absentCount=0;
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                mListStudentData.clear();
                try{

                    System.out.println("View Class List Response:"+response);
                    JSONObject jsonObject=new JSONObject(response);
                   if( jsonObject.getString("success").equals("0")){
                       textViewNothing.setVisibility(View.VISIBLE);
                   }else {
                       textViewNothing.setVisibility(View.GONE);
                       JSONArray jsonArray = jsonObject.getJSONArray("studentsname");
                       for (int i = 0; i < jsonArray.length(); i++) {
                           JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                           String attendance = jsonObject1.getString("Attidends");
                           String firstName = jsonObject1.getString("first_name");
                           String classId = jsonObject1.getString("class_id");
                           String applicationId = jsonObject1.getString("application_id");
                           String schoolId = jsonObject1.getString("school_id");
                           String rollNo = jsonObject1.getString("roll_no");
                           String sectionId = jsonObject1.getString("section_id");
                           StudentDataModel studentDataModel = new StudentDataModel(attendance, firstName,
                                   classId, sectionId, rollNo, applicationId, schoolId);
                           mListStudentData.add(studentDataModel);
                           // Toast.makeText(ViewStudentsListActivity.this, attendance, Toast.LENGTH_SHORT).show();
                           if (studentDataModel.getAttendance().equalsIgnoreCase("P")) {
                               presentCount = presentCount + 1;
                               textViewPresentCount.setText("Present-" + presentCount + "");
                               //Toast.makeText(ViewStudentsListActivity.this, presentCount, Toast.LENGTH_SHORT).show();
                           } else if (studentDataModel.getAttendance().equalsIgnoreCase("A")) {
                               absentCount = absentCount + 1;
                               textViewAbsentCount.setText("Absent-" + absentCount);
                           }
                       }

                       adapterRecyclerViewStudentData = new AdapterRecyclerViewStudentData(mContext, mListStudentData, checkBoxSelectAll, buttonSubmit);
                       LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                       layoutManager.setReverseLayout(true);
                       layoutManager.setStackFromEnd(true);
                       recyclerViewStudentData.setLayoutManager(layoutManager);
                       recyclerViewStudentData.setAdapter(adapterRecyclerViewStudentData);
                       recyclerViewStudentData.setHasFixedSize(true);
                       recyclerViewStudentData.setItemViewCacheSize(mListStudentData.size());
                       adapterRecyclerViewStudentData.notifyDataSetChanged();
                   }

                }
                catch(Exception e)
                {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    textViewNothing.setVisibility(View.VISIBLE);
                    // System.out.println("View Class List Exception:"+e.getMessage());
                    //  Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                textViewNothing.setVisibility(View.VISIBLE);
                // System.out.println("View Class List Error:"+error.getMessage());
                // Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params=new HashMap<>();
                params.put("class_id",mClassId);
                params.put("section_id",mSectionId);
                return params;
            }
        };
        stringRequestGetClassList
                .setRetryPolicy(new DefaultRetryPolicy(50000
                        ,DefaultRetryPolicy
                        .DEFAULT_MAX_RETRIES
                        ,DefaultRetryPolicy
                        .DEFAULT_BACKOFF_MULT));
        Singelton.getInstance(mContext)
                .addToRequestQue(stringRequestGetClassList);
    }

    private void sendMultipleAttendance(final JSONArray jsonArray){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("jsonArray",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Config.SEND_MULTIPLE_ATTENDANCE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject1=new JSONObject(response);
                    String message=jsonObject1.getString("message");
                    if(message.equalsIgnoreCase("Success")){
                        ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_navigation, new AttendanceFragment())
                                .commit();
                    }else{
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             //   Toast.makeText(mContext, error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("string",jsonArray.toString());
                return params;
            }
        };
                RequestQueue requestQueue= Volley.newRequestQueue(mContext);
                requestQueue.add(stringRequest);
    }

    // get present student count from student attendance object
    private void getPresentCount(){
        final ProgressDialog progressDialog=new ProgressDialog(mContext);
        progressDialog.setTitle("Students Details");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        StringRequest stringRequestGetClassList=new StringRequest(Request.Method.POST, Config.URL_GET_CLASS_STUDENTS_LIST, new Response.Listener<String>() {
            int presentCount=0;
            int absentCount=0;
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                mListStudentData.clear();
                try{
                    System.out.println("View Class List Response:"+response);
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("studentsname");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String attendance=jsonObject1.getString("Attidends");
                        String firstName=jsonObject1.getString("first_name");
                        String classId=jsonObject1.getString("class_id");
                        String applicationId=jsonObject1.getString("application_id");
                        String schoolId=jsonObject1.getString("school_id");
                        String rollNo=jsonObject1.getString("roll_no");
                        String sectionId=jsonObject1.getString("section_id");
                        StudentDataModel studentDataModel=new StudentDataModel(attendance,firstName,
                                classId,sectionId,rollNo,applicationId,schoolId);
                        if(attendance.equalsIgnoreCase("P"))
                        {

                            mListStudentData.add(studentDataModel);
                        }

                        if(studentDataModel.getAttendance().equalsIgnoreCase("P"))
                        {
                            presentCount=presentCount+1;
                            textViewPresentCount.setText("Present-"+presentCount+"");
                            //Toast.makeText(ViewStudentsListActivity.this, presentCount, Toast.LENGTH_SHORT).show();
                        }
                        else if(studentDataModel.getAttendance().equalsIgnoreCase("A"))
                        {
                            absentCount=absentCount+1;
                            textViewAbsentCount.setText("Absent-"+absentCount);
                        }
                    }

                    adapterRecyclerViewStudentData=new AdapterRecyclerViewStudentData(mContext,mListStudentData,checkBoxSelectAll,buttonSubmit);
                    LinearLayoutManager layoutManager=new LinearLayoutManager(mContext);
                    layoutManager.setReverseLayout(true);
                    layoutManager.setStackFromEnd(true);
                    recyclerViewStudentData.setLayoutManager(layoutManager);
                    recyclerViewStudentData.setAdapter(adapterRecyclerViewStudentData);
                    recyclerViewStudentData.setHasFixedSize(true);
                    adapterRecyclerViewStudentData.notifyDataSetChanged();


                }
                catch(Exception e)
                {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    // System.out.println("View Class List Exception:"+e.getMessage());
                   // Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                // System.out.println("View Class List Error:"+error.getMessage());
               // Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params=new HashMap<>();
                params.put("class_id",mClassId);
                params.put("section_id",mSectionId);
                return params;
            }
        };
        stringRequestGetClassList.setRetryPolicy(new DefaultRetryPolicy(50000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singelton.getInstance(mContext).addToRequestQue(stringRequestGetClassList);

    }

    // get absent student count from student attendance object
    private void getAbsentCount(){
        final ProgressDialog progressDialog=new ProgressDialog(mContext);
        progressDialog.setTitle("Students Details");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        StringRequest stringRequestGetClassList=new StringRequest(Request.Method.POST, Config.URL_GET_CLASS_STUDENTS_LIST, new Response.Listener<String>() {
            int presentCount=0;
            int absentCount=0;
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                mListStudentData.clear();
                try{
                    System.out.println("View Class List Response:"+response);
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("studentsname");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String attendance=jsonObject1.getString("Attidends");
                        String firstName=jsonObject1.getString("first_name");
                        String classId=jsonObject1.getString("class_id");
                        String applicationId=jsonObject1.getString("application_id");
                        String schoolId=jsonObject1.getString("school_id");
                        String rollNo=jsonObject1.getString("roll_no");
                        String sectionId=jsonObject1.getString("section_id");
                        StudentDataModel studentDataModel=new StudentDataModel(attendance,firstName,
                                classId,sectionId,rollNo,applicationId,schoolId);
                        if(attendance.equalsIgnoreCase("A"))
                        {

                            mListStudentData.add(studentDataModel);
                        }

                        if(studentDataModel.getAttendance().equalsIgnoreCase("P"))
                        {
                            presentCount=presentCount+1;
                            textViewPresentCount.setText("Present-"+presentCount+"");
                            //Toast.makeText(ViewStudentsListActivity.this, presentCount, Toast.LENGTH_SHORT).show();
                        }
                        else if(studentDataModel.getAttendance().equalsIgnoreCase("A"))
                        {
                            absentCount=absentCount+1;
                            textViewAbsentCount.setText("Absent-"+absentCount);
                        }
                    }

                    adapterRecyclerViewStudentData=new AdapterRecyclerViewStudentData(mContext,mListStudentData,checkBoxSelectAll,buttonSubmit);
                    LinearLayoutManager layoutManager=new LinearLayoutManager(mContext);
                    layoutManager.setReverseLayout(true);
                    layoutManager.setStackFromEnd(true);
                    recyclerViewStudentData.setLayoutManager(layoutManager);
                    recyclerViewStudentData.setAdapter(adapterRecyclerViewStudentData);
                    recyclerViewStudentData.setHasFixedSize(true);
                    adapterRecyclerViewStudentData.notifyDataSetChanged();


                }
                catch(Exception e)
                {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    // System.out.println("View Class List Exception:"+e.getMessage());
                   // Toast.makeText(mContext, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                // System.out.println("View Class List Error:"+error.getMessage());
               // Toast.makeText(mContext, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params=new HashMap<>();
                params.put("class_id",mClassId);
                params.put("section_id",mSectionId);
                return params;
            }
        };
        stringRequestGetClassList.setRetryPolicy(new DefaultRetryPolicy(50000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singelton.getInstance(mContext).addToRequestQue(stringRequestGetClassList);
    }


}