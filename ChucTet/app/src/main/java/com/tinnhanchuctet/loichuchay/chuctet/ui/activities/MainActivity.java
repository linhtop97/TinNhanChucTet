package com.tinnhanchuctet.loichuchay.chuctet.ui.activities;

import android.Manifest;
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

import com.tinnhanchuctet.loichuchay.chuctet.Ads;
import com.tinnhanchuctet.loichuchay.chuctet.R;
import com.tinnhanchuctet.loichuchay.chuctet.database.sharedprf.SharedPrefsImpl;
import com.tinnhanchuctet.loichuchay.chuctet.database.sharedprf.SharedPrefsKey;
import com.tinnhanchuctet.loichuchay.chuctet.databinding.ActivityMainBinding;
import com.tinnhanchuctet.loichuchay.chuctet.ui.fragments.EditMessageFragment;
import com.tinnhanchuctet.loichuchay.chuctet.ui.fragments.MainFragment;
import com.tinnhanchuctet.loichuchay.chuctet.ui.fragments.SplashFragment;
import com.tinnhanchuctet.loichuchay.chuctet.utils.Constant;
import com.tinnhanchuctet.loichuchay.chuctet.utils.Navigator;
import com.zer.android.newsdk.ZAndroidSystems;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Navigator mNavigators;
    private SharedPrefsImpl mSharedPref;
    public static final String[] PERMISSION_WRITE_PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ActivityMainBinding mBinding;

    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter = new IntentFilter();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIntentFilter.addAction("RequestPMS");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("RequestPMS")) {
                    Intent newIntent = new Intent();
                    newIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri myUri = Uri.fromParts("package", getPackageName(), null);
                    newIntent.setData(myUri);
                    startActivityForResult(newIntent, SplashFragment.RQ_PERMISSION_CODE);
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, mIntentFilter);
        initUI();
    }

    private void initUI() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mNavigators = new Navigator(this);
        mSharedPref = new SharedPrefsImpl(this);
        addSplashFragment();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragmentGet = manager.findFragmentByTag(SplashFragment.class.getSimpleName());
        if (requestCode == Constant.REQUEST_CODE_PER) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        boolean showRationale = shouldShowRequestPermissionRationale(permission);
                        if (!showRationale) {
                            if (fragmentGet != null) {
                                SplashFragment splashFrg = (SplashFragment) fragmentGet;
                                splashFrg.showSnackbar();
                            }
                            Log.i(TAG, "onRequestPermissionsResult: 1");
                        } else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
                            if (fragmentGet != null) {
                                SplashFragment splashFrg = (SplashFragment) fragmentGet;
                                splashFrg.checkPermission();
                            }
                            Log.i(TAG, "onRequestPermissionsResult: 2");
                        }
                    }

                } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "onRequestPermissionsResult: 3");
                    if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
                        if (fragmentGet != null) {
                            SplashFragment splashFragment = (SplashFragment) fragmentGet;
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
        SplashFragment splashFrg = SplashFragment.newInstance();
        mNavigators.addFragment(R.id.main_container, splashFrg, false, Navigator.NavigateAnim.NONE, SplashFragment.class.getSimpleName());
    }

    public void gotoHomeFragment() {
        hideSoftKeyboard();
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int j = 0; j < fragmentManager.getBackStackEntryCount(); ++j) {
            fragmentManager.popBackStack();
        }
        MainFragment fragment = MainFragment.newInstance();
        mNavigators.addFragment(R.id.main_container, fragment, false, Navigator.NavigateAnim.NONE, MainFragment.class.getSimpleName());
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(EditMessageFragment.class.getSimpleName());
        if (manager.getBackStackEntryCount() >= 1) {
            String fragmentTag = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName();
            if (fragment != null && EditMessageFragment.class.getSimpleName().equals(fragmentTag)) {
                EditMessageFragment editMessageFragment = (EditMessageFragment) fragment;
                hideSoftKeyboard();
                editMessageFragment.confirmLeave(mSharedPref.get(SharedPrefsKey.KEY_IS_ADD_NEW, Boolean.class), false);
                return;
            }
        }
        super.onBackPressed();
    }

    public void hideSoftKeyboard() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        try {
            im.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception ex) {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragmentGet = manager.findFragmentByTag(SplashFragment.class.getSimpleName());
        if (fragmentGet != null) {
            SplashFragment splashFragment = (SplashFragment) fragmentGet;
            splashFragment.showSnackbar();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }
}
