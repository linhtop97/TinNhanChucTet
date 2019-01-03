package com.tinnhantet.nhantin.hengio.ui.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.databinding.FragmentPendingBinding;
import com.tinnhantet.nhantin.hengio.ui.activities.AddMsgActivity;
import com.tinnhantet.nhantin.hengio.ui.activities.MainActivity;
import com.tinnhantet.nhantin.hengio.utils.Navigator;

public class PendingFragment extends Fragment implements View.OnClickListener {
    private FragmentPendingBinding mBinding;
    private MainActivity mMainActivity;
    private Navigator mNavigator;


    public static PendingFragment newInstance() {
        PendingFragment fragment = new PendingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pending, container, false);
        initUI();
        initAction();
        return mBinding.getRoot();
    }

    private void initAction() {
        mBinding.btnAdd.setOnClickListener(this);
    }

    private void initUI() {
        mNavigator = new Navigator(mMainActivity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                mNavigator.startActivity(AddMsgActivity.class, Navigator.NavigateAnim.BOTTOM_UP);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        //get All msg Schedule
    }
}
