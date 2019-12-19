package com.example.mydschoolteachersapp;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mydschoolteachersapp.Fragments.AssignmentFragment;
import com.example.mydschoolteachersapp.Fragments.AttendanceFragment;
import com.example.mydschoolteachersapp.Fragments.CalendarFragment;
import com.riontech.calendar.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    int[] icons = {R.drawable.attendance_icon
            , R.drawable.assignment_image
            , R.drawable.calendar_icon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        addTabs(viewPager);
        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(new AttendanceFragment());
        fragmentList.add(new AssignmentFragment());
        fragmentList.add(new CalendarFragment());
    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new AttendanceFragment());
        viewPagerAdapter.addFragments(new AssignmentFragment());
        viewPagerAdapter.addFragments(new CalendarFragment());

    }

    private void setUpTabs() {
        tabLayout.getTabAt(0).setIcon(icons[0]);
        tabLayout.getTabAt(1).setIcon(icons[1]);
        tabLayout.getTabAt(2).setIcon(icons[2]);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> mListFragments = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mListFragments.get(i);
        }

        @Override
        public int getCount() {
            return mListFragments.size();
        }

        private void addFragments(Fragment fragment) {
            mListFragments.add(fragment);

        }
    }

}
