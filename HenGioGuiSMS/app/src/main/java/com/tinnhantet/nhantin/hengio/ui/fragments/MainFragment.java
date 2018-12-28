package com.tinnhantet.nhantin.hengio.ui.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.adapters.MainPagerAdapter;
import com.tinnhantet.nhantin.hengio.databinding.FragmentMainBinding;
import com.tinnhantet.nhantin.hengio.utils.TabType;

public class MainFragment extends Fragment implements TabLayout.BaseOnTabSelectedListener {
    private FragmentMainBinding mBinding;


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        initUI();
        return mBinding.getRoot();
    }

    private void initUI() {
        MainPagerAdapter adapter = new MainPagerAdapter(getChildFragmentManager());
        mBinding.viewPager.setAdapter(adapter);
        mBinding.viewPager.setCurrentItem(TabType.PENDING);
        mBinding.tablayout.addOnTabSelectedListener(this);
        mBinding.tablayout.setupWithViewPager(mBinding.viewPager);
        mBinding.tablayout.getTabAt(TabType.PENDING).setText(R.string.pending);
        mBinding.tablayout.getTabAt(TabType.SENT).setText(R.string.sent);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int pos = tab.getPosition();
        switch (pos) {
            case TabType.PENDING:
                break;
            case TabType.SENT:
                break;
        }
        mBinding.viewPager.setCurrentItem(pos);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
