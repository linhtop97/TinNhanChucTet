package com.tinnhantet.loichuc.chuctet.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tinnhantet.loichuc.chuctet.MyApplication;
import com.tinnhantet.loichuc.chuctet.R;
import com.tinnhantet.loichuc.chuctet.database.sharedprf.SharedPrefsImpl;
import com.tinnhantet.loichuc.chuctet.databinding.FragmentSplashBinding;
import com.tinnhantet.loichuc.chuctet.ui.activities.MainActivity;
import com.tinnhantet.loichuc.chuctet.ui.dialogs.GuideRequestFragment;
import com.tinnhantet.loichuc.chuctet.utils.Constant;
import com.tinnhantet.loichuc.chuctet.utils.Navigator;

public class SplashFragment extends Fragment {

    private static final String TAG = "MainActivity";
    private static final String REQUEST_DIALOG = "REQUEST_DIALOG";
    private FragmentSplashBinding mBinding;
    private Navigator mNavigator;
    private SharedPrefsImpl mSharedPrefs;
    private MainActivity mMainActivity;
    public static final int REQUEST_PERMISSION_CODE = 12345;

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false);
        initUI();
        return mBinding.getRoot();
    }

    private void initUI() {
        mNavigator = new Navigator(this);
        mSharedPrefs = new SharedPrefsImpl(mMainActivity);
        Glide.with(MyApplication.getInstance())
                .load(R.drawable.ss_splash)
                .into(mBinding.imgBg);
        //checkpermision
        checkPermission();

    }

    public void addMainFragmentWithDelay() {
        int SPLASH_DISPLAY_LENGTH = 1500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainFragment mainFragment = MainFragment.newInstance();
                mNavigator.addFragment(R.id.main_container, mainFragment, false, Navigator.NavigateAnim.NONE, MainFragment.class.getSimpleName());
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    public void addMainFragment() {
        MainFragment mainFragment = MainFragment.newInstance();
        mNavigator.addFragment(R.id.main_container, mainFragment, false, Navigator.NavigateAnim.NONE, MainFragment.class.getSimpleName());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(MainActivity.PERMISSION_WRITE, mMainActivity) != PackageManager.PERMISSION_GRANTED) {
                mMainActivity.requestPermissions(MainActivity.PERMISSION_WRITE, Constant.REQUEST_CODE);
                //mSharedPrefs.put(SharedPrefsKey.PERMISSION_IS_DECLINE, true);
            } else {
                addMainFragment();
            }
        } else {
            addMainFragmentWithDelay();
        }
    }

    public static int checkPermission(String[] permissions, Context context) {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : permissions) {
            permissionCheck += ContextCompat.checkSelfPermission(context, permission);
        }
        return permissionCheck;
    }


    public void showSnackbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(MainActivity.PERMISSION_WRITE, mMainActivity) == PackageManager.PERMISSION_GRANTED) {
                addMainFragment();
            } else {
                showDialogRequest();
            }
        }
    }

    private void showDialogRequest() {
        GuideRequestFragment f = GuideRequestFragment.getInstance();
        mMainActivity.getSupportFragmentManager().beginTransaction().add(f, REQUEST_DIALOG).commit();
    }
}