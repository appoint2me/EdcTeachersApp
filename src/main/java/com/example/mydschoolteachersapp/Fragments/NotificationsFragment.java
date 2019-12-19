package com.example.mydschoolteachersapp.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.mydschoolteachersapp.Adapters.NotificationAdapter;
import com.example.mydschoolteachersapp.Classes.Config;
import com.example.mydschoolteachersapp.Classes.SharedPrefrenceManager;
import com.example.mydschoolteachersapp.Classes.Singelton;
import com.example.mydschoolteachersapp.Model.NotificationsModel;
import com.example.mydschoolteachersapp.NavigationActivity;
import com.example.mydschoolteachersapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment {
    RecyclerView mRecyclerViewNotifications;
    List<NotificationsModel> mListNotification = new ArrayList<>();
    Context mContext;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        mContext = view.getContext();
        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle("Notifications");
        mRecyclerViewNotifications = view.findViewById(R.id.recyclerViewNotification);
        String staffId = SharedPrefrenceManager.getPrefVal(mContext, Config.USER_ID);
        getNotifications(staffId);
        return view;
    }

    private void getNotifications(final String staffId) {
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, Config.GET_NOTIFICATIONS+staffId, new JSONObject(), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status=response.getString("status");
                            if(status.equalsIgnoreCase("1")){
                                JSONArray jsonArray=response.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    String notification_id=jsonObject.getString("notification_id");
                                    String notification=jsonObject.getString("notification");
                                    String staff_id=jsonObject.getString("staff_id");
                                    String added_date=jsonObject.getString("added_date");
                                    NotificationsModel notificationsModel=new NotificationsModel(added_date,staff_id,notification,notification_id);
                                    mListNotification.add(notificationsModel);


                                }
                                NotificationAdapter notificationAdapter=new NotificationAdapter(mContext,mListNotification);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(mContext);
                                mRecyclerViewNotifications.setLayoutManager(layoutManager);
                                mRecyclerViewNotifications.setAdapter(notificationAdapter);
                                mRecyclerViewNotifications.setHasFixedSize(true);
                                notificationAdapter.notifyDataSetChanged();

                            }else{
                                Toast.makeText(mContext, "No notifications", Toast.LENGTH_SHORT).show();
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
