package com.tinnhantet.loichuc.chuctet.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.tinnhantet.loichuc.chuctet.Ads;
import com.tinnhantet.loichuc.chuctet.R;
import com.tinnhantet.loichuc.chuctet.database.sharedprf.SharedPrefsImpl;
import com.tinnhantet.loichuc.chuctet.database.sharedprf.SharedPrefsKey;
import com.tinnhantet.loichuc.chuctet.databinding.ActivityMainBinding;
import com.tinnhantet.loichuc.chuctet.ui.fragments.EditMessageFragment;
import com.tinnhantet.loichuc.chuctet.ui.fragments.MainFragment;
import com.tinnhantet.loichuc.chuctet.ui.fragments.MessageFragment;
import com.tinnhantet.loichuc.chuctet.ui.fragments.SplashFragment;
import com.tinnhantet.loichuc.chuctet.utils.Navigator;
import com.zer.android.newsdk.ZAndroidSDK;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private Navigator mNavigator;
    private SharedPrefsImpl mSharedPrefs;
    private static final String[] PERMISSION_WRITE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ActivityMainBinding mainBinding;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(PERMISSION_WRITE, MainActivity.this) != PackageManager.PERMISSION_GRANTED) {
                MainActivity.this.requestPermissions(PERMISSION_WRITE, 12);
            } else {
                initUI();
            }
        } else {
            initUI();
        }
    }

    private void initUI() {
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mNavigator = new Navigator(this);
        mSharedPrefs = new SharedPrefsImpl(this);
        addSplashFragment();
        ads();
    }

    public void ads() {
        ZAndroidSDK.init(MainActivity.this);
        Ads.f(MainActivity.this);
        Ads.b(MainActivity.this, mainBinding.layoutAds, new Ads.OnAdsListener() {
            @Override
            public void onError() {
                mainBinding.layoutAds.setVisibility(View.GONE);
            }

            @Override
            public void onAdLoaded() {
                mainBinding.layoutAds.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdOpened() {
                mainBinding.layoutAds.setVisibility(View.VISIBLE);
            }
        });
    }

    public static int checkPermission(String[] permissions, Context context) {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : permissions) {
            permissionCheck += ContextCompat.checkSelfPermission(context, permission);
        }
        return permissionCheck;
    }

    private void addSplashFragment() {
        SplashFragment splashFragment = SplashFragment.newInstance();
        mNavigator.addFragment(R.id.main_container, splashFragment, false, Navigator.NavigateAnim.NONE, SplashFragment.class.getSimpleName());
    }

    public void onSwipeRight() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(MessageFragment.class.getSimpleName());
        if (fragment != null) {
            MessageFragment messageFragment = (MessageFragment) fragment;
            messageFragment.onSwipeLeft();
        }

    }

    public void onSwipeLeft() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(MessageFragment.class.getSimpleName());
        if (fragment != null) {
            MessageFragment messageFragment = (MessageFragment) fragment;
            messageFragment.onSwipeRight();
        }
    }

    public void onSwipeUp() {

    }

    public void onSwipeDown() {
    }

    public void onSingleTab() {
    }

    public void onDoubleTab() {
    }

    public void gotoHomeFragment() {
        hideSoftKeyboard();
        FragmentManager fm = getSupportFragmentManager();
        for (int j = 0; j < fm.getBackStackEntryCount(); ++j) {
            fm.popBackStack();
        }
        MainFragment mainFragment = MainFragment.newInstance();
        mNavigator.addFragment(R.id.main_container, mainFragment, false, Navigator.NavigateAnim.NONE, MainFragment.class.getSimpleName());
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(EditMessageFragment.class.getSimpleName());
        if (fragmentManager.getBackStackEntryCount() >= 1) {
            String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            if (fragment != null && EditMessageFragment.class.getSimpleName().equals(fragmentTag)) {
                EditMessageFragment editMessageFragment = (EditMessageFragment) fragment;
                hideSoftKeyboard();
                editMessageFragment.confirmLeave(mSharedPrefs.get(SharedPrefsKey.KEY_IS_ADD_NEW, Boolean.class), false);
                return;
            }
        }
        super.onBackPressed();
    }

    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        try {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception ex) {

        }
    }
}
