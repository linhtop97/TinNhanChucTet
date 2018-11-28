package com.example.linhnb.project1zergitas.screen.wallpaper;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.linhnb.project1zergitas.R;
import com.example.linhnb.project1zergitas.databinding.FragmentWallpaperBinding;
import com.example.linhnb.project1zergitas.screen.base.adapter.OnItemClickListener;
import com.example.linhnb.project1zergitas.screen.main.MainActivity;
import com.example.linhnb.project1zergitas.utils.navigator.Navigator;

import java.util.Arrays;
import java.util.List;

public class WallpaperFragment extends Fragment implements OnItemClickListener {
    private FragmentWallpaperBinding mBinding;
    private MainActivity mMainActivity;
    private ProgressDialog mDialog;
    private List<String> mListWallpaper;
    private Navigator mNavigator;

    public static WallpaperFragment newInstance() {
        return new WallpaperFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallpaper, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mNavigator = new Navigator(mMainActivity);
        mMainActivity.setSupportActionBar(mBinding.toolbar);
        if (mMainActivity.getSupportActionBar() != null) {
            mMainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mMainActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mDialog = new ProgressDialog(mMainActivity);
        mDialog.setMessage(mMainActivity.getString(R.string.msg_loading));
        String[] arr = getActivity().getResources().getStringArray(R.array.wallpaper_array);
        mListWallpaper = Arrays.asList(arr);
        WallpaperAdapter adapter = new WallpaperAdapter(mMainActivity, mListWallpaper);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(mMainActivity, 2);
        mBinding.recyclerWallpaper.setLayoutManager(linearLayoutManager);
        adapter.setOnItemClickListener(this);
        mBinding.recyclerWallpaper.setAdapter(adapter);

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

    @Override
    public void onItemClick(int position) {
        SettingWallpaperFragment fragment = SettingWallpaperFragment.newInstance(mListWallpaper.get(position));
        mNavigator.addFragment(R.id.main_container, fragment, true, Navigator.NavigateAnim.NONE, fragment.getClass().getSimpleName());
    }
}
