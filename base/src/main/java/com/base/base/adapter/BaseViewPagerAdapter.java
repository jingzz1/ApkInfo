package com.base.base.adapter;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 */

public class BaseViewPagerAdapter<T extends Fragment> extends FragmentPagerAdapter {
    private List<T> fragments = new ArrayList<>();
    private String[] titles;

    public BaseViewPagerAdapter(FragmentManager fm) {
        this(fm, new ArrayList<T>(), null);
    }

    public BaseViewPagerAdapter(FragmentManager fm, @Nullable List<T> fragments) {
        this(fm, fragments, null);
    }

    public BaseViewPagerAdapter(FragmentManager fm, @Nullable List<T> fragments, String[] titles) {
        super(fm);
        if(fragments != null)
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (titles == null)
            return super.getPageTitle(position);
        else {
            return titles[position];
        }


    }

    public void notifyDataSetChanged(List<T> fragments){
        if (fragments!= null){
            this.fragments = fragments;
            notifyDataSetChanged();
        }
    }

}
