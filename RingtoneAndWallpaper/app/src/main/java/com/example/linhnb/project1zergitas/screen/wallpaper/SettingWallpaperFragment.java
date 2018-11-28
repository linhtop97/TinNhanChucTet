package com.example.linhnb.project1zergitas.screen.wallpaper;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.linhnb.project1zergitas.R;
import com.example.linhnb.project1zergitas.databinding.FragmentSettingWallpaperBinding;
import com.example.linhnb.project1zergitas.screen.main.MainActivity;
import com.example.linhnb.project1zergitas.utils.Constant;
import com.example.linhnb.project1zergitas.utils.DimensionUtil;

public class SettingWallpaperFragment extends Fragment {
    private FragmentSettingWallpaperBinding mBinding;
    private MainActivity mMainActivity;
    private ProgressDialog mDialog;

    public static SettingWallpaperFragment newInstance(String wallpaper) {
        SettingWallpaperFragment fragment = new SettingWallpaperFragment();
        Bundle args = new Bundle();
        args.putString(Constant.ARGUMENT_IMAGE, wallpaper);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_wallpaper, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mMainActivity.setSupportActionBar(mBinding.toolbar);
        if (mMainActivity.getSupportActionBar() != null) {
            mMainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mMainActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mBinding.imageWallpaper.getLayoutParams().height = DimensionUtil.getScreenWidthInPixels(mMainActivity) - (int) DimensionUtil.convertDpToPixel(8);
        Glide.with(mMainActivity)
                .load(Uri.parse("file:///android_asset/wallpaper/" + getArguments().getString(Constant.ARGUMENT_IMAGE)))
                .into(mBinding.imageWallpaper);
        mBinding.buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mMainActivity, "ok saved", Toast.LENGTH_SHORT).show();
            }
        });
        mDialog = new ProgressDialog(mMainActivity);
        mDialog.setMessage(mMainActivity.getString(R.string.msg_loading));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            mMainActivity.getSupportFragmentManager().popBackStackImmediate();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
