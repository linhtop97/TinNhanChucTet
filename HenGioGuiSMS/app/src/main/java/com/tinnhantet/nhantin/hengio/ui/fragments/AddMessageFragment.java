package com.tinnhantet.nhantin.hengio.ui.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.databinding.FragmentAddMsgBinding;

public class AddMessageFragment extends Fragment {
    private FragmentAddMsgBinding mBinding;


    public static AddMessageFragment newInstance() {
        AddMessageFragment fragment = new AddMessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_msg, container, false);
        initUI();
        return mBinding.getRoot();
    }

    private void initUI() {
    }
}
