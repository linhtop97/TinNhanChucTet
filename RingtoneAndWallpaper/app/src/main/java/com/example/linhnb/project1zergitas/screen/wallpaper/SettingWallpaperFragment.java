package com.example.linhnb.project1zergitas.screen.wallpaper;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.linhnb.project1zergitas.R;
import com.example.linhnb.project1zergitas.data.source.DataCallback;
import com.example.linhnb.project1zergitas.databinding.FragmentSettingWallpaperBinding;
import com.example.linhnb.project1zergitas.screen.main.MainActivity;
import com.example.linhnb.project1zergitas.utils.Constant;
import com.example.linhnb.project1zergitas.utils.DimensionUtil;

public class SettingWallpaperFragment extends Fragment implements DataCallback<String> {
    private FragmentSettingWallpaperBinding mBinding;
    private MainActivity mMainActivity;
    private ProgressDialog mDialog;
    private static final String PROGRESS_DIALOG = "ProgressDialog";

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
        final String nameImage = getArguments().getString(Constant.ARGUMENT_IMAGE);
        Glide.with(mMainActivity)
                .load(Uri.parse("file:///android_asset/wallpaper/" + nameImage))
                .into(mBinding.imageWallpaper);
        final int height = DimensionUtil.getScreenHeightInPixels(mMainActivity);
        mBinding.buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                Glide.with(mMainActivity)
                        .asBitmap()
                        .load(Uri.parse("file:///android_asset/wallpaper/" + nameImage))
                        .apply(new RequestOptions().override(height, height))
                        .into(new Target<Bitmap>() {
                            @Override
                            public void onLoadStarted(@Nullable Drawable placeholder) {

                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {

                            }

                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                               new WallpaperHelper(mMainActivity).setWallpaper(resource, nameImage, SettingWallpaperFragment.this);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }

                            @Override
                            public void getSize(@NonNull SizeReadyCallback cb) {

                            }

                            @Override
                            public void removeCallback(@NonNull SizeReadyCallback cb) {

                            }

                            @Override
                            public void setRequest(@Nullable Request request) {

                            }

                            @Nullable
                            @Override
                            public Request getRequest() {
                                return null;
                            }

                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onStop() {

                            }

                            @Override
                            public void onDestroy() {

                            }
                        });
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

    public void showProgress() {
        ProgressDialogFragment f = ProgressDialogFragment.getInstance();
        getFragmentManager().beginTransaction().add(f, PROGRESS_DIALOG).commitAllowingStateLoss();
    }

    public void dismissProgress() {
        if (!isResumed()) return;
        android.support.v4.app.FragmentManager manager = getFragmentManager();
        if (manager == null) return;
        ProgressDialogFragment f = (ProgressDialogFragment) manager.findFragmentByTag(PROGRESS_DIALOG);
        if (f != null) {
            getFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
        }
    }

    @Override
    public void onGetDataSuccess(String data) {
        dismissProgress();
        Toast.makeText(mMainActivity, "Wallpaper setting successful", Toast.LENGTH_SHORT).show();
        mMainActivity.getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onGetDataFailed(String msg) {
        Toast.makeText(mMainActivity, "Wallpaper setting Failed", Toast.LENGTH_SHORT).show();
        dismissProgress();
    }
}
