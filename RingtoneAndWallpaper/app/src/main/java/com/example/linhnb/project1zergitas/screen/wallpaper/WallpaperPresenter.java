package com.example.linhnb.project1zergitas.screen.wallpaper;

import android.content.Context;

import com.example.linhnb.project1zergitas.data.source.DataCallback;
import com.example.linhnb.project1zergitas.data.source.local.sharedprf.SharedPrefsImpl;
import com.example.linhnb.project1zergitas.data.source.local.sharedprf.SharedPrefsKey;

public class WallpaperPresenter implements WallpaperContract.Presenter, DataCallback<String> {

    private final WallpaperContract.View mView;
    private WallpaperHelper mHelper;
    private Context mContext;

    public WallpaperPresenter(Context context, WallpaperContract.View view) {
        mView = view;
        mHelper = new WallpaperHelper(context);
    }

    @Override
    public void loadingWallpapers() {

    }

    @Override
    public void onStart() {
        if (new SharedPrefsImpl(mContext).get(SharedPrefsKey.WALLPAPER_COPIED, Boolean.class)) {
            return;
        }
        mHelper.copyWallpaper(this);
        mView.showLoading();
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onGetDataSuccess(String data) {
        mView.hideLoading();
    }

    @Override
    public void onGetDataFailed(String msg) {

    }
}
