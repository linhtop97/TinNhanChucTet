package com.example.linhnb.project1zergitas.screen.wallpaper;

import com.example.linhnb.project1zergitas.screen.base.BasePresenter;
import com.example.linhnb.project1zergitas.screen.base.BaseView;

import java.util.List;

interface WallpaperContract {
    interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();

        void showWallpaperList(List<String> wallpaperList);
    }

    interface Presenter extends BasePresenter {

        void loadingWallpapers();
    }
}
