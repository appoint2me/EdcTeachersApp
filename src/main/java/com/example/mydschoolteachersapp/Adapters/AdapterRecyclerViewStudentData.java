package com.example.mydschoolteachersapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mydschoolteachersapp.Classes.Config;
import com.example.mydschoolteachersapp.Classes.SharedPrefrenceManager;
import com.example.mydschoolteachersapp.Classes.Singelton;
import com.example.mydschoolteachersapp.Fragments.AttendanceFragment;
import com.example.mydschoolteachersapp.Interface.MultipleSubmit;
import com.example.mydschoolteachersapp.Model.StudentDataModel;
import com.example.mydschoolteachersapp.R;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class AdapterRecyclerViewStudentData extends RecyclerView.Adapter<AdapterRecyclerViewStudentData.ViewHolder> {
    Context mContext;
    private final SparseBooleanArray array=new SparseBooleanArray();
    ArrayList<StudentDataModel> mListStudentData;
    private static final String TAG = "AdapterRecyclerViewStud";
    JSONArray jsonArray=new JSONArray();
    CheckBox checkBox;
    Boolean isSelected;
    Button buttonSubmit;

    public AdapterRecyclerViewStudentData(Context context, ArrayList<StudentDataModel> mListStudentData, CheckBox checkBoxSelectAll, Button buttonSubmit) {
        this.mContext=context;
        this.mListStudentData=mListStudentData;
        this.checkBox=checkBoxSelectAll;
        this.buttonSubmit=buttonSubmit;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        final View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_student_data,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
    buttonSubmit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //  Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show();
            sendMultipleAttendance(jsonArray);

        }
    });
        final JSONObject jsonObject=new JSONObject();
        final StudentDataModel studentDataModel=mListStudentData.get(i);
        try {
            if (isSelected) {
                viewHolder.checkBoxChangeAttendance.setChecked(true);

            } else {
                viewHolder.checkBoxChangeAttendance.setChecked(false);
                jsonArray.remove(i);
              //  Toast.makeText(mContext, jsonArray.toString(), Toast.LENGTH_SHORT).show();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }


        viewHolder.textViewFirstName.setText(studentDataModel.getFirstName());
        viewHolder.textViewRollNo.setText(studentDataModel.getRollNo());
        viewHolder.textViewAttendance.setText(studentDataModel.getAttendance());

        if (studentDataModel.getAttendance().equalsIgnoreCase("P"))
        {
            viewHolder.fabAttendance.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.colorGreen)));
        }
        else
        {
            viewHolder.fabAttendance.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.colorRed)));
        }
        viewHolder.fabAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Section_idll = String.valueOf(studentDataModel.getSectionId());
                Log.d(TAG, "onClick: section_id"+Section_idll);
                String Classid = String.valueOf(studentDataModel.getClassId());
                Log.d(TAG, "onClick: Classid"+Classid);

                String Itemstatus = String.valueOf(studentDataModel.getAttendance());
                Log.d(TAG, "onClick: Itemstatus"+Itemstatus);
                String Students_id = String.valueOf(studentDataModel.getApplicationId());
                Log.d(TAG, "onClick:Students_id "+Students_id);
                if(Itemstatus.equalsIgnoreCase("A")){
                    changeAttendanceStatus(Students_id,"1");
                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_navigation, new AttendanceFragment())
                            .commit();

                }else if(Itemstatus.equalsIgnoreCase("P")){
                    changeAttendanceStatus(Students_id,"0");
                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_navigation, new AttendanceFragment())
                            .commit();
                }
            }
        });
        viewHolder.checkBoxChangeAttendance.setFocusable(false);
        viewHolder.checkBoxChangeAttendance.setOnCheckedChangeListener(null);
        viewHolder.checkBoxChangeAttendance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              //  array.put(i,true);
//                if(array.get(i)){
//                    viewHolder.checkBoxChangeAttendance.setChecked(true);
//                }else{
//                    viewHolder.checkBoxChangeAttendance.setChecked(false);
//                }
                if (isChecked) {


                        String Itemstatus = String.valueOf(studentDataModel.getAttendance());


                        if (Itemstatus.equalsIgnoreCase("A")) {
                            try {
                                String student_id = studentDataModel.getApplicationId();
                                String status = "1";
                                jsonObject.put("student_id", student_id);
                                jsonObject.put("status", status);
                                jsonArray.put(jsonObject);
                            //    Toast.makeText(mContext, jsonArray.toString(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onCheckedChanged: JsonObject   A" + jsonObject);
                                Log.d(TAG, "onCheckedChanged: JsonObject    A" + jsonArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else if (Itemstatus.equalsIgnoreCase("P")) {
                            try {
                                String student_id = studentDataModel.getApplicationId();
                                String status = "0";
                                jsonObject.put("student_id", student_id);
                                jsonObject.put("status", status);
                                jsonArray.put(jsonObject);
                              //  Toast.makeText(mContext, jsonArray.toString(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onCheckedChanged: JsonObject   P" + jsonObject);
                                Log.d(TAG, "onCheckedChanged: JsonObject    P" + jsonArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                } else {
                    String id = studentDataModel.getApplicationId();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            String student_id =
                                    jsonArray.getJSONObject(i).getString("student_id");
                            if (id.equalsIgnoreCase(student_id)) {
                                jsonArray.remove(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    /// jsonArray.remove(viewHolder.getAdapterPosition());
                    Log.d(TAG, "onCheckedChanged: adapterPosition:  " + jsonArray);
                  //  Toast.makeText(mContext, jsonArray.toString(), Toast.LENGTH_SHORT).show();
                    // viewHolder.getAdapterPosition();

                }
                SharedPrefrenceManager.setPrefVal(mContext, "ARRAY", jsonArray.toString());
            }

        });
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);

    }

    @Override
    public long getItemId(int position) {
        //  mPosition=position;
        return super.getItemId(position);

    }

    @Override
    public int getItemViewType(int position) {
        //  mPosition=position;
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mListStudentData.size();
    }

    public void selectAll(Boolean status){
        if(status){
            isSelected=true;
            notifyDataSetChanged();
        }else{
            isSelected=false;
            notifyDataSetChanged();
        }

    }
    public void changeAttendanceStatus(final String studentId, final String status) {

        final ProgressDialog progressDialog1=new ProgressDialog(mContext);
        progressDialog1.setTitle("Change Students List");
        progressDialog1.setMessage("Please Wait...");
        progressDialog1.show();
        StringRequest stringRequestChangeAttendanceStatus = new StringRequest(Request.Method.POST, Config.URL_CHANGE_ATTENDANCE_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog1.dismiss();
                try {



                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog1.dismiss();
                    Toast.makeText(mContext, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                Toast.makeText(mContext, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //send data to server and get respons from its in respons
                params.put("student_id", studentId);
                params.put("status", status);
                return params;
            }
        };

        stringRequestChangeAttendanceStatus.setRetryPolicy(new DefaultRetryPolicy(50000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singelton.getInstance(mContext).addToRequestQue(stringRequestChangeAttendanceStatus);

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


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFirstName,textViewRollNo,textViewAttendance;
        FloatingActionButton fabAttendance;
        CheckBox checkBoxChangeAttendance;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewFirstName= itemView.findViewById(R.id.textViewFirstName);
            textViewRollNo= itemView.findViewById(R.id.textViewRollNo);
            textViewAttendance= itemView.findViewById(R.id.textViewAttendance);
            fabAttendance= itemView.findViewById(R.id.fab);
            checkBoxChangeAttendance= itemView.findViewById(R.id.checkBox);
        }

    }

}
