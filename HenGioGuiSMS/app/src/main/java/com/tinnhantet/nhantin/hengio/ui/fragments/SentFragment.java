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
import com.tinnhantet.nhantin.hengio.databinding.FragmentSentBinding;

public class SentFragment extends Fragment {
    private FragmentSentBinding mBinding;


    public static SentFragment newInstance() {
        SentFragment fragment = new SentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sent, container, false);
        initUI();
        return mBinding.getRoot();
    }

    private void initUI() {
    }
}
