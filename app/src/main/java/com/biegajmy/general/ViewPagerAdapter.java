package com.biegajmy.general;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override public int getCount() {
        return fragments.size();
    }
}
