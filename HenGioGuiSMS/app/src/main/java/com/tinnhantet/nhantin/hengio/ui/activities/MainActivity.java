package com.tinnhantet.nhantin.hengio.ui.activities;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tinnhantet.nhantin.hengio.Ads;
import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.adapters.MainPagerAdapter;
import com.tinnhantet.nhantin.hengio.databinding.ActivityMainBinding;
import com.tinnhantet.nhantin.hengio.utils.Navigator;
import com.tinnhantet.nhantin.hengio.utils.TabType;
import com.zer.android.newsdk.ZAndroidSystems;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void initUI() {
        mNavigator = new Navigator(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        Glide.with(this)
                .load(R.drawable.bg_main)
                .into(mBinding.imgBackground);
        mBinding.viewPager.setAdapter(adapter);
        mBinding.tablayout.setupWithViewPager(mBinding.viewPager);
        for (int i = 0; i < MainPagerAdapter.TAB_COUNT; i++) {
            TabLayout.Tab tab = mBinding.tablayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
        mBinding.tablayout.addOnTabSelectedListener(this);
        sInstance = this;
        ads();
    }

    public void ads() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
            ZAndroidSystems.init(MainActivity.this);
        }
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
            case TabType.SENT:
                View v = mBinding.tablayout.getTabAt(TabType.SENT).getCustomView();
                ImageView img = v.findViewById(R.id.img_icon_tab);
                TextView txt1 = v.findViewById(R.id.tabContent);
                txt1.setTextColor(getResources().getColor(R.color.white));
                img.setImageDrawable(getResources().getDrawable(R.drawable.ic_send_click));
                break;

            case TabType.PENDING:
                View v4 = mBinding.tablayout.getTabAt(TabType.PENDING).getCustomView();
                ImageView img4 = v4.findViewById(R.id.img_icon_tab);
                TextView txt4 = v4.findViewById(R.id.tabContent);
                txt4.setTextColor(getResources().getColor(R.color.white));
                img4.setImageDrawable(getResources().getDrawable(R.drawable.ic_loading_click));
                break;

        }
        mBinding.viewPager.setCurrentItem(pos);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        int pos = tab.getPosition();
        switch (pos) {
            case TabType.SENT:
                View v = mBinding.tablayout.getTabAt(TabType.SENT).getCustomView();
                ImageView img = v.findViewById(R.id.img_icon_tab);
                TextView txt1 = v.findViewById(R.id.tabContent);
                txt1.setTextColor(getResources().getColor(R.color.colorWitheOpa));
                img.setImageDrawable(getResources().getDrawable(R.drawable.ic_send));

                break;

            case TabType.PENDING:
                View v4 = mBinding.tablayout.getTabAt(TabType.PENDING).getCustomView();
                ImageView img4 = v4.findViewById(R.id.img_icon_tab);
                TextView txt4 = v4.findViewById(R.id.tabContent);
                txt4.setTextColor(getResources().getColor(R.color.colorWitheOpa));
                img4.setImageDrawable(getResources().getDrawable(R.drawable.ic_loading));
                break;

        }
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