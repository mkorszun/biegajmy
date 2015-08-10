package com.biegajmy.general;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private CharSequence titles[];
    private List<Fragment> fragments;

    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], List<Fragment> fragments) {
        super(fm);
        this.titles = mTitles;
        this.fragments = fragments;
    }

    @Override public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override public int getCount() {
        return fragments.size();
    }
}
