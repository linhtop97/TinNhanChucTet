package com.example.linhnb.project1zergitas.screen.ringtone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.linhnb.project1zergitas.R;
import com.example.linhnb.project1zergitas.data.model.Ringtone;
import com.example.linhnb.project1zergitas.data.source.local.ringtone.RingtoneLocalDataSource;
import com.example.linhnb.project1zergitas.data.source.local.ringtone.RingtoneRepository;
import com.example.linhnb.project1zergitas.data.source.local.sharedprf.SharedPrefsImpl;
import com.example.linhnb.project1zergitas.data.source.local.sharedprf.SharedPrefsKey;
import com.example.linhnb.project1zergitas.databinding.FragmentRingtoneBinding;
import com.example.linhnb.project1zergitas.screen.base.adapter.OnItemClickListener;
import com.example.linhnb.project1zergitas.screen.main.MainActivity;
import com.example.linhnb.project1zergitas.utils.Constant;

import java.util.List;

public class RingtoneFragment extends Fragment implements RingtoneContract.View, OnItemClickListener {

    private FragmentRingtoneBinding mBinding;
    private RingtoneContract.Presenter mPresenter;
    private RingtoneAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private MainActivity mMainActivity;
    private List<Ringtone> mRingtoneList;
    private RingtoneHelper mRingtoneHelper;
    private int mRingtoneSelected;
    private String mFilePath = "";
    private SharedPrefsImpl mSharedPrefs;
    private String[] listPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static RingtoneFragment newInstance() {
        return new RingtoneFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ringtone, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mRingtoneHelper = new RingtoneHelper(mMainActivity);
        mSharedPrefs = new SharedPrefsImpl(mMainActivity);
        mRingtoneSelected = mSharedPrefs.get(SharedPrefsKey.RINGTONE_SELECTED_INDEX, Integer.class);
        mMainActivity.setSupportActionBar(mBinding.toolbar);
        if (mMainActivity.getSupportActionBar() != null) {
            mMainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mMainActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        RingtoneRepository repository = RingtoneRepository.getInstance(RingtoneLocalDataSource.getInstance());
        mPresenter = new RingtonePresenter(this, repository);
        mPresenter.loadRingtoneList();

    }

    @Override
    public void showRingtoneList(List<Ringtone> ringtoneList) {
        mRingtoneList = ringtoneList;
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mBinding.recyclerRingtone.setLayoutManager(mLinearLayoutManager);
        mAdapter = new RingtoneAdapter(getActivity(), ringtoneList);
        mAdapter.setOnItemClickListener(this);
        mBinding.recyclerRingtone.setAdapter(mAdapter);
        mBinding.buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isGranted = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(mMainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(mMainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            ActivityCompat.requestPermissions(mMainActivity, listPermission, Constant.REQUEST_PERMISSTION);
                        } else {
                            ActivityCompat.requestPermissions(mMainActivity, listPermission, Constant.REQUEST_PERMISSTION);
                        }
                    }
                    if (!Settings.System.canWrite(mMainActivity)) {
                        String message = "bạn cần cho phép ứng dụng cài đặt hệ thống ";
                        int duration = Snackbar.LENGTH_LONG;
                        showSnackbar(view, message, duration);
                        mRingtoneHelper.reset();
                    } else {
                        isGranted = true;
                    }
                } else {
                    isGranted = true;
                }

                if (isGranted) {
                    if (mRingtoneHelper.setRingtone(mFilePath)) {
                        mMainActivity.getSupportFragmentManager().popBackStackImmediate();
                        mSharedPrefs.put(SharedPrefsKey.RINGTONE_SELECTED_INDEX, mRingtoneSelected);
                        Toast.makeText(mMainActivity, "Ringtone is applied", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mMainActivity, "Apply ringtone Failed", Toast.LENGTH_SHORT).show();
                    }
                    mRingtoneHelper.reset();
                }
            }
        });
    }

    private void showSnackbar(View view, String message, int duration) {
        final Snackbar snackbar = Snackbar.make(view, message, duration);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                Uri uri2 = Uri.fromParts("package", mMainActivity.getPackageName(), null);
                intent.setData(uri2);
                startActivityForResult(intent, 13);
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @Override
    public void setPresenter(RingtoneContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onItemClick(int position) {
        mRingtoneSelected = position;
        mFilePath = mRingtoneList.get(position).getName();
        mRingtoneHelper.listent(mMainActivity, mFilePath);
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
    public void onDetach() {
        super.onDetach();
        mRingtoneHelper.reset();
    }
}
