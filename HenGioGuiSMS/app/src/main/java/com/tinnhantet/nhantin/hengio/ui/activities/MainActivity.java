package com.tinnhantet.nhantin.hengio.ui.activities;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tinnhantet.nhantin.hengio.Ads;
import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.adapters.MainPagerAdapter;
import com.tinnhantet.nhantin.hengio.databinding.ActivityMainBinding;
import com.tinnhantet.nhantin.hengio.utils.Navigator;
import com.tinnhantet.nhantin.hengio.utils.TabType;
import com.zer.android.newsdk.ZAndroidSDK;

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
        int[] tabIcon = new int[]{R.drawable.cb_unchecked, R.drawable.cb_checked};
        int[] tabTitle = new int[]{R.string.pending, R.string.sent};
        for (int i = 0; i < 2; i++) {
            LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_tab, null);
            TextView tabContent = (TextView) tabLinearLayout.findViewById(R.id.tabContent);
            tabContent.setText("  " + getApplicationContext().getResources().getString(tabTitle[i]));
            tabContent.setCompoundDrawablesWithIntrinsicBounds(tabIcon[i], 0, 0, 0);
            mBinding.tablayout.getTabAt(i).setCustomView(tabContent);
        }


        sInstance = this;
        //ads();
    }

    public void ads() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
            ZAndroidSDK.init(MainActivity.this);
        }
        Ads.f(MainActivity.this);
        Ads.b(MainActivity.this, mBinding.layoutAds, new Ads.OnAdsListener() {
            @Override
            public void onError() {
                mBinding.layoutAds.setVisibility(View.GONE);
            }

            @Override
            public void onAdLoaded() {
                mBinding.layoutAds.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdOpened() {
                mBinding.layoutAds.setVisibility(View.VISIBLE);
            }
        });
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