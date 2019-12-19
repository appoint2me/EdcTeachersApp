package com.example.mydschoolteachersapp;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mydschoolteachersapp.Adapters.ViewPagerAdapter;
import com.example.mydschoolteachersapp.Fragments.AssignmentFragment;
import com.example.mydschoolteachersapp.Fragments.AttendanceFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        viewPager=(ViewPager)findViewById(R.id.viewpager123);
        ArrayList<Fragment> fraagmentsList=new ArrayList<>();
        fraagmentsList.add(new AttendanceFragment());
        fraagmentsList.add(new AssignmentFragment());
        ArrayList<String> titleList=new ArrayList<>();
        titleList.add("Attendance");
        titleList.add("Assignment");
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),fraagmentsList,titleList));
    }
}
