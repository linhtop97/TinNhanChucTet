package com.tinnhantet.loichuc.chuctet.ui.activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.tinnhantet.loichuc.chuctet.Ads;
import com.tinnhantet.loichuc.chuctet.R;
import com.tinnhantet.loichuc.chuctet.database.sharedprf.SharedPrefsImpl;
import com.tinnhantet.loichuc.chuctet.database.sharedprf.SharedPrefsKey;
import com.tinnhantet.loichuc.chuctet.databinding.ActivityMainBinding;
import com.tinnhantet.loichuc.chuctet.ui.fragments.EditMessageFragment;
import com.tinnhantet.loichuc.chuctet.ui.fragments.MainFragment;
import com.tinnhantet.loichuc.chuctet.ui.fragments.SplashFragment;
import com.tinnhantet.loichuc.chuctet.utils.Constant;
import com.tinnhantet.loichuc.chuctet.utils.Navigator;
import com.zer.android.newsdk.ZAndroidSDK;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Navigator mNavigator;
    private SharedPrefsImpl mSharedPrefs;
    public static final String[] PERMISSION_WRITE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ActivityMainBinding mainBinding;

    private BroadcastReceiver mRefreshReceiver;
    IntentFilter filter = new IntentFilter();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filter.addAction("RequestPMS");
        mRefreshReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("RequestPMS")) {
                    Intent intent1 = new Intent();
                    intent1.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri2 = Uri.fromParts("package", getPackageName(), null);
                    intent1.setData(uri2);
                    startActivityForResult(intent1, SplashFragment.REQUEST_PERMISSION_CODE);
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mRefreshReceiver, filter);
        initUI();
    }

    private void initUI() {
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mNavigator = new Navigator(this);
        mSharedPrefs = new SharedPrefsImpl(this);
        addSplashFragment();
    }

    public void ads() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            ZAndroidSDK.init(MainActivity.this);
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(SplashFragment.class.getSimpleName());
        if (requestCode == Constant.REQUEST_CODE) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        boolean showRationale = shouldShowRequestPermissionRationale(permission);
                        if (!showRationale) {
                            if (fragment != null) {
                                SplashFragment splashFragment = (SplashFragment) fragment;
                                splashFragment.showSnackbar();
                            }
                            Log.i(TAG, "onRequestPermissionsResult: 1");
                        } else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
                            if (fragment != null) {
                                SplashFragment splashFragment = (SplashFragment) fragment;
                                splashFragment.showSnackbar();
                            }
                            Log.i(TAG, "onRequestPermissionsResult: 2");
                        }
                    }

                } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "onRequestPermissionsResult: 3");
                    if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
                        if (fragment != null) {
                            SplashFragment splashFragment = (SplashFragment) fragment;
                            splashFragment.addMainFragment();
                        }
                    }
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void addSplashFragment() {
        SplashFragment splashFragment = SplashFragment.newInstance();
        mNavigator.addFragment(R.id.main_container, splashFragment, false, Navigator.NavigateAnim.NONE, SplashFragment.class.getSimpleName());
    }

//    public void onSwipeRight() {
//        Fragment fragment = getSupportFragmentManager().findFragmentByTag(MessageFragment.class.getSimpleName());
//        if (fragment != null) {
//            MessageFragment messageFragment = (MessageFragment) fragment;
//            messageFragment.onSwipeLeft();
//        }
//
//    }
//
//    public void onSwipeLeft() {
//        Fragment fragment = getSupportFragmentManager().findFragmentByTag(MessageFragment.class.getSimpleName());
//        if (fragment != null) {
//            MessageFragment messageFragment = (MessageFragment) fragment;
//            messageFragment.onSwipeRight();
//        }
//    }

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //check permision nếu ok thì cho dùng app, ko thì show snack bar
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(SplashFragment.class.getSimpleName());
        if (fragment != null) {
            SplashFragment splashFragment = (SplashFragment) fragment;
            splashFragment.showSnackbar();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRefreshReceiver);
    }
}
