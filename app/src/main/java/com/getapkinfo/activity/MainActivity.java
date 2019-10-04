package com.getapkinfo.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.base.base.adapter.BaseViewPagerAdapter;
import com.getapkinfo.fragment.ApkListFragment;
import com.getapkinfo.R;
import com.getapkinfo.base.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = findViewById(R.id.tabLayout);
        mTabLayout.addTab(mTabLayout.newTab().setText("应用"));
        mTabLayout.addTab(mTabLayout.newTab().setText("系统应用"));

        mViewPager = findViewById(R.id.viewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ApkListFragment.newInstance(false));
        fragments.add(ApkListFragment.newInstance(true));
        mViewPager.setAdapter(new BaseViewPagerAdapter<Fragment>(getSupportFragmentManager(),fragments));
    }


}
