package com.tinnhantet.loichuc.chuctet.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tinnhantet.loichuc.chuctet.MyApplication;
import com.tinnhantet.loichuc.chuctet.R;
import com.tinnhantet.loichuc.chuctet.databinding.FragmentSplashBinding;
import com.tinnhantet.loichuc.chuctet.ui.activities.MainActivity;
import com.tinnhantet.loichuc.chuctet.utils.Constant;
import com.tinnhantet.loichuc.chuctet.utils.Navigator;

public class SplashFragment extends Fragment {

    private FragmentSplashBinding mBinding;
    private Navigator mNavigator;
    private MainActivity mMainActivity;

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
        Glide.with(MyApplication.getInstance())
                .load(R.drawable.ss_splash)
                .into(mBinding.imgBg);
        //checkpermision
        checkPermission();

    }

    public void addMainFragment() {
        int SPLASH_DISPLAY_LENGTH = 2500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainFragment mainFragment = MainFragment.newInstance();
                mNavigator.addFragment(R.id.main_container, mainFragment, false, Navigator.NavigateAnim.NONE, MainFragment.class.getSimpleName());
            }
        }, SPLASH_DISPLAY_LENGTH);

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
                if (Settings.System.canWrite(mMainActivity)) {
                    addMainFragment();
                }
            } else {
                //show snack bar require
                showSnackbar();
            }
        } else {
            addMainFragment();
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
            if (!Settings.System.canWrite(mMainActivity)) {
                String message = "bạn cần cho phép ứng dụng truy cập bộ nhớ! ";
                int duration = Integer.MAX_VALUE;
                showSnackbar(mBinding.imgBg, message, duration);
            }
        }
    }

    private void showSnackbar(View view, String message, int duration) {
        final Snackbar snackbar = Snackbar.make(view, message, duration);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri2 = Uri.fromParts("package", mMainActivity.getPackageName(), null);
                intent.setData(uri2);
                startActivityForResult(intent, 13);
                snackbar.dismiss();

            }
        });
        snackbar.show();
    }
}