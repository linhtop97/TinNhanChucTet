package com.example.linhnb.project1zergitas.screen.ringtone;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.linhnb.project1zergitas.R;
import com.example.linhnb.project1zergitas.data.model.Ringtone;
import com.example.linhnb.project1zergitas.databinding.FragmentRingtoneBinding;

import java.util.List;

public class RingtoneFragment extends Fragment implements RingtoneContract.View {

    private FragmentRingtoneBinding mBinding;

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
    public void showRingtoneList(List<Ringtone> ringtoneList) {

    }
}
