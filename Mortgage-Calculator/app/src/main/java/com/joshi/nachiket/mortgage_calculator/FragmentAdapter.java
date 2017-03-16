package com.joshi.nachiket.mortgage_calculator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by abc on 14-Mar-17.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {


    private int tabCount;
    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        tabCount = 2;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0) {
            return (new CalculatorFragment());
        } else {
            return (new GoogleMapsFragment());
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0) {
            return "MORTGAGE CALC.";
        } else {
            return "MAP LOCATIONS";
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
