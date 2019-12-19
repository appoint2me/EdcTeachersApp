package com.example.mydschoolteachersapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mydschoolteachersapp.Adapters.AdapterRecyclerViewMain;
import com.example.mydschoolteachersapp.NavigationActivity;
import com.example.mydschoolteachersapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
Button btnClass,btnAssignment;
    String[] categoryNamesList = {"ATTENDANCE", "ASSIGNMENT", "HOLIDAYS"};
    int[] categoryImageList = {R.drawable.attendance_icon, R.drawable.book_icon, R.drawable.calendar_icon};
    RecyclerView recyclerView;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_home, container, false);
        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle("Dashboard");
        recyclerView= view.findViewById(R.id.recylerView);
        AdapterRecyclerViewMain adapterRecyclerViewMain=new AdapterRecyclerViewMain(view.getContext(),categoryNamesList,categoryImageList);
        LinearLayoutManager gridLayoutManager=new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapterRecyclerViewMain );
        return view;
    }

}
