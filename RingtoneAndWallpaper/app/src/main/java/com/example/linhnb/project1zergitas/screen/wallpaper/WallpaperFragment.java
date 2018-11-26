package com.example.linhnb.project1zergitas.screen.wallpaper;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.linhnb.project1zergitas.R;
import com.example.linhnb.project1zergitas.databinding.FragmentWallpaperBinding;

public class WallpaperFragment extends Fragment {
    private FragmentWallpaperBinding mBinding;

    public static WallpaperFragment newInstance() {
        return new WallpaperFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallpaper, container, false);
        return mBinding.getRoot();
    }
}
