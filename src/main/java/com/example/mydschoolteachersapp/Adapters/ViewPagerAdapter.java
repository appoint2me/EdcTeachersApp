package com.example.mydschoolteachersapp.Adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList titleListt=new ArrayList();
    ArrayList fraagmentsList=new ArrayList();
    public ViewPagerAdapter(FragmentManager fm, ArrayList fraagmentsList, ArrayList titleListt) {
        super(fm);
        this.titleListt=titleListt;
        this.fraagmentsList=fraagmentsList;
    }

    @Override
    public Fragment getItem(int i) {
       return (Fragment) fraagmentsList.get(i);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
      return (CharSequence) titleListt.get(position);

    }
}
