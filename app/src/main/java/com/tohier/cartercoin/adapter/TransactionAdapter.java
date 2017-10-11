package com.tohier.cartercoin.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/11.
 */

public class TransactionAdapter extends FragmentPagerAdapter {

    private List<Fragment> vp_data = new ArrayList<Fragment>();

    public TransactionAdapter(FragmentManager fm, List<Fragment> vp_data) {
        super(fm);
        this.vp_data = vp_data;
    }



    @Override
    public Fragment getItem(int arg0) {
        return vp_data.get(arg0);
    }

    @Override
    public int getCount() {
        return vp_data.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
