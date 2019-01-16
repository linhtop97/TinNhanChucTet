package com.tinnhanchuctet.loichuchay.chuctet.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tinnhanchuctet.loichuchay.chuctet.models.Message;
import com.tinnhanchuctet.loichuchay.chuctet.ui.fragments.VPFragment;

import java.util.List;

public class MyTVAdapter extends FragmentPagerAdapter {

    private List<Message> mMessageList;

    public MyTVAdapter(FragmentManager fm, List<Message> messages) {
        super(fm);
        mMessageList = messages;
    }

    @Override
    public Fragment getItem(int i) {
        return VPFragment.newInstance(mMessageList.get(i));
    }

    @Override
    public int getCount() {
        return mMessageList.size();
    }
}

