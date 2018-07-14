package com.vdt.trieuvu310198.goldtime.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vdt.trieuvu310198.goldtime.fragment.AlarmFragment;
import com.vdt.trieuvu310198.goldtime.fragment.TimeZoneFragment;
import com.vdt.trieuvu310198.goldtime.fragment.PromptFragmnet;
import com.vdt.trieuvu310198.goldtime.fragment.StopwatchFragment;
import com.vdt.trieuvu310198.goldtime.fragment.TimerFragment;

public class ViewpagerAdapter extends FragmentPagerAdapter {

    public ViewpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AlarmFragment();
            case 1:
                return new StopwatchFragment();
            case 2:
                return new TimerFragment();
            case 3:
                return new PromptFragmnet();
            case 4:
                return new TimeZoneFragment();
                default: return new AlarmFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Hẹn giờ";
            case 1:
                return "Bấm giờ";
            case 2:
                return "Đếm Giờ";
            case 3:
                return "Nhắc nhở";
            case 4:
                return "Giờ quốc tế";
                default:
                    return "Hẹn Giờ";
        }
    }
}
