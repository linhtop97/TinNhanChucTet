package com.tinnhantet.nhantin.hengio.ui.activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.adapters.MainPagerAdapter;
import com.tinnhantet.nhantin.hengio.databinding.ActivityMainBinding;
import com.tinnhantet.nhantin.hengio.utils.Navigator;
import com.tinnhantet.nhantin.hengio.utils.TabType;

public class MainActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener {
    private ActivityMainBinding mBinding;
    private Navigator mNavigator;
    public static boolean active = false;
    public static MainActivity sInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        mNavigator = new Navigator(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        mBinding.viewPager.setAdapter(adapter);
        mBinding.viewPager.setCurrentItem(TabType.PENDING);
        mBinding.tablayout.addOnTabSelectedListener(this);
        mBinding.tablayout.setupWithViewPager(mBinding.viewPager);
        mBinding.tablayout.getTabAt(TabType.PENDING).setText(R.string.pending);
        mBinding.tablayout.getTabAt(TabType.SENT).setText(R.string.sent);
        sInstance = this;
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

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }
}