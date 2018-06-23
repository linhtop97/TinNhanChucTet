package com.vdt.trieuvu310198.goldtime.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vdt.trieuvu310198.goldtime.fragment.BamGioFragment;
import com.vdt.trieuvu310198.goldtime.fragment.DemNguocFragment;
import com.vdt.trieuvu310198.goldtime.fragment.HenGioFragment;
import com.vdt.trieuvu310198.goldtime.fragment.NhacNhoFragmnet;

public class ViewpagerAdapter extends FragmentPagerAdapter {
    public ViewpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HenGioFragment();
            case 1:
                return new NhacNhoFragmnet();
            case 2:
                return new DemNguocFragment();
            case 3:
                return new BamGioFragment();
                default: return new HenGioFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Hẹn Giờ";
            case 1:
                return "Nhắc Nhở";
            case 2:
                return "Đếm Ngược";
            case 3:
                return "Bấm Giờ";
                default:
                    return "Hẹn Giờ";
        }
    }
}
