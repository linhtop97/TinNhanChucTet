package com.example.linhnb.project1zergitas.screen.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.linhnb.project1zergitas.R;
import com.example.linhnb.project1zergitas.databinding.FragmentMainBinding;
import com.example.linhnb.project1zergitas.screen.ringtone.RingtoneFragment;
import com.example.linhnb.project1zergitas.screen.wallpaper.WallpaperFragment;
import com.example.linhnb.project1zergitas.utils.navigator.Navigator;

public class MainFragment extends Fragment implements View.OnClickListener {
    private FragmentMainBinding mBinding;
    private MainActivity mMainActivity;
    private Navigator mNavigator;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        mBinding.buttonRingtone.setOnClickListener(this);
        mBinding.buttonWallpaper.setOnClickListener(this);
        mNavigator = new Navigator(mMainActivity);
        return mBinding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_ringtone:
                RingtoneFragment ringtoneFragment = RingtoneFragment.newInstance();
                mNavigator.addFragment(R.id.main_container, ringtoneFragment, true, Navigator.NavigateAnim.NONE, ringtoneFragment.getClass().getSimpleName());
                break;
            case R.id.button_wallpaper:
                WallpaperFragment wallpaperFragment = WallpaperFragment.newInstance();
                mNavigator.addFragment(R.id.main_container, wallpaperFragment, true, Navigator.NavigateAnim.NONE, wallpaperFragment.getClass().getSimpleName());
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }
}
