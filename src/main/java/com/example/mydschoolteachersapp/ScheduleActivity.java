package com.example.mydschoolteachersapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.mydschoolteachersapp.Classes.Config;
import com.example.mydschoolteachersapp.Classes.Singelton;
import com.example.mydschoolteachersapp.Model.HolidaysModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleActivity extends AppCompatActivity {
    private String message;
    private int success;
    private List<HolidaysModel> holidaysModelList;
    com.applandeo.materialcalendarview.CalendarView calendarView;
    String mSchoolId;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    ArrayList<HolidaysModel> holidaysModelArrayList=new ArrayList<>();
    TextView textViewHolidayDescription,textViewDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        android.support.v7.widget.Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Calender");
        calendarView = findViewById(R.id.calendarView);
        holidaysModelList = new ArrayList<>();
        textViewHolidayDescription= findViewById(R.id.holidayDescription);
        textViewDate= findViewById(R.id.holidayDate);
        getHolidaysList();
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar calendar=Calendar.getInstance();
                calendar= eventDay.getCalendar();
                Date date = calendar.getTime();
                DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
                String date1=dateFormat.format(date);
                for(int i=0;i<holidaysModelArrayList.size();i++)
                {
                    HolidaysModel holidaysModel=holidaysModelArrayList.get(i);
                    if(holidaysModel.getHolidayDate().equalsIgnoreCase(date1))
                    {
                        textViewDate.setText(date1);
                        textViewHolidayDescription.setText(holidaysModel.getHolidayDescription());
                    }

                }
            }
        });
    }

    public void getHolidaysList() {
        SharedPreferences sharedPreferences=getSharedPreferences(Config.MY_SHARED_PREFRENCE,MODE_PRIVATE);
        mSchoolId=sharedPreferences.getString(Config.SCHOOL_ID,null);
        StringRequest stringRequestGetHolidays = new StringRequest(Request.Method.POST, Config.URL_GET_HOLIDAYS_LIST, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            String holidaysDate;
            @Override
            public void onResponse(String response) {
                List<EventDay> events = new ArrayList<>();
                try
                {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("hdata");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            holidaysDate=jsonObject1.getString("holiday_date");
                            HolidaysModel holidaysModel=new HolidaysModel(holidaysDate,jsonObject1.getString("holiday_description"));
                            holidaysModelArrayList.add(holidaysModel);

                        try {
                            Date date = format.parse(holidaysDate);
                            Calendar calendar=Calendar.getInstance();
                            calendar.setTime(date);
                            EventDay eventDay =new EventDay(calendar,R.drawable.red_circle);
                            events.add(eventDay);



                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                calendarView.setEvents(events);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("School_id", mSchoolId);
                return params;
            }
        };
        Singelton.getInstance(ScheduleActivity.this).addToRequestQue(stringRequestGetHolidays);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ScheduleActivity.this, NavigationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            // todo: goto back activity from here
//            case R.id.action_settings:
//                final AlertDialog.Builder alertBuilder=new AlertDialog.Builder(this);
//                alertBuilder.setTitle("Logout");
//                alertBuilder.setMessage("Do you Really Want to Exit");
//
//                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        SharedPreferences.Editor editor=getSharedPreferences(Config.MY_SHARED_PREFRENCE, Context.MODE_PRIVATE).edit();
//                        editor.clear();
//                        editor.apply();
//                        Intent intent=new Intent(ScheduleActivity.this,LoginActivity.class);
//                        startActivity(intent);
//                    }
//                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                alertBuilder.create();
//                alertBuilder.show();


            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
