package com.tinnhantet.loichuc.chuctet.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tinnhantet.loichuc.chuctet.models.Message;
import com.tinnhantet.loichuc.chuctet.ui.fragments.VPFragment;

import java.util.List;

public class MyTVAdapter extends FragmentPagerAdapter {

    private List<Message> mMessages;

    public MyTVAdapter(FragmentManager fm, List<Message> messages) {
        super(fm);
        mMessages = messages;
    }


    @Override
    public Fragment getItem(int i) {
        return VPFragment.newInstance(mMessages.get(i));
    }

    @Override
    public int getCount() {
        return mMessages.size();
    }
}

