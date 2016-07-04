package com.example.administrator.testguid.view.parallax;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Liyeyu on 2016/7/4.
 */
public class ParallaxAdapter extends FragmentPagerAdapter {
    private List<ParallaxFragment> mFragments;
    public ParallaxAdapter(FragmentManager fm,List<ParallaxFragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

}
