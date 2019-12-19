package com.example.mydschoolteachersapp.Fragments;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.mydschoolteachersapp.NavigationActivity;
import com.example.mydschoolteachersapp.R;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

    private String message;
    private int success;
    private List<HolidaysModel> holidaysModelList;
    com.applandeo.materialcalendarview.CalendarView calendarView;
    String mSchoolId;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    ArrayList<HolidaysModel> holidaysModelArrayList=new ArrayList<>();
    TextView textViewHolidayDescription,textViewDay,textViewMonth;
    BottomSheetBehavior bottomSheetBehavior;
    ProgressBar progressBar;
    ListView listViewHolidays;
    private List<String> list_holiday_name=new ArrayList<>();

    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        holidaysModelList = new ArrayList<>();
        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle("Calendar");
        progressBar= view.findViewById(R.id.progressBar2);
      //  textViewHolidayDescription= view.findViewById(R.id.textvie);
        listViewHolidays= view.findViewById(R.id.listViewHolidays);
        textViewMonth= view.findViewById(R.id.textViewMonth);
        textViewDay= view.findViewById(R.id.textViewDay);

        final ConstraintLayout bottomSheetViewgroup =view. findViewById(R.id.bottonSheetsCalendar);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetViewgroup);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setPeekHeight(0);

        getHolidaysList();
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {

                Calendar calendar=Calendar.getInstance();
                calendar= eventDay.getCalendar();
                Date date = calendar.getTime();
                DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
                String date1=dateFormat.format(date);
                DateFormat dateFormatMonth=new SimpleDateFormat("MMMM");
                String month=dateFormatMonth.format(date);
                DateFormat dateFormatDay=new SimpleDateFormat("dd");
                String day=dateFormatDay.format(date);
                list_holiday_name.clear();
                for(int i=0;i<holidaysModelArrayList.size();i++)
                {

                    HolidaysModel holidaysModel=holidaysModelArrayList.get(i);
                    if(holidaysModel.getHolidayDate().equalsIgnoreCase(date1))
                    {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED
                        );
                        textViewDay.setText(day);
                        textViewMonth.setText(month);
                        list_holiday_name.add (holidaysModel.getHolidayDescription());

                    }

                }
                ArrayAdapter arrayAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,list_holiday_name);
                listViewHolidays.setAdapter(arrayAdapter);
                final Handler handler=new Handler();
                final Runnable runnable=new Runnable() {
                    @Override
                    public void run() {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                };
                handler.postDelayed(runnable,7000);
            }
        });

        return view;
    }

    public void getHolidaysList() {
        SharedPreferences sharedPreferences= getContext().getSharedPreferences(Config.MY_SHARED_PREFRENCE, getContext().MODE_PRIVATE);
        mSchoolId=sharedPreferences.getString(Config.SCHOOL_ID,null);
        StringRequest stringRequestGetHolidays = new StringRequest(Request.Method.POST, Config.URL_GET_HOLIDAYS_LIST, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            String holidaysDate;
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
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
                            Toast.makeText(getContext(), "Something  went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                catch (JSONException e)
                {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Something  went wrong", Toast.LENGTH_SHORT).show();

                }

                calendarView.setEvents(events);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Something  weent wrong", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("School_id", mSchoolId);
                return params;
            }
        };
        Singelton.getInstance(getContext()).addToRequestQue(stringRequestGetHolidays);
    }

}
