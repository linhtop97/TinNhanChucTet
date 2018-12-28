package com.tinnhantet.nhantin.hengio.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tinnhantet.nhantin.hengio.ui.fragments.PendingFragment;
import com.tinnhantet.nhantin.hengio.ui.fragments.SentFragment;
import com.tinnhantet.nhantin.hengio.utils.TabType;


public class MainPagerAdapter extends FragmentPagerAdapter {

    public static final int TAB_COUNT = 2;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TabType.PENDING:
                return PendingFragment.newInstance();
            case TabType.SENT:
                return SentFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}
