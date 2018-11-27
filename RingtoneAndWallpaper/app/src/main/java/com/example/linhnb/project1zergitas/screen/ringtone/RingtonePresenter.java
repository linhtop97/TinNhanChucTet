package com.example.linhnb.project1zergitas.screen.ringtone;

import com.example.linhnb.project1zergitas.data.model.Ringtone;
import com.example.linhnb.project1zergitas.data.source.DataCallback;
import com.example.linhnb.project1zergitas.data.source.local.ringtone.RingtoneRepository;

import java.util.List;

public class RingtonePresenter implements RingtoneContract.Presenter, DataCallback<List<Ringtone>> {

    private final RingtoneContract.View mView;
    private RingtoneRepository mRepository;

    public RingtonePresenter(RingtoneContract.View view, RingtoneRepository repository) {
        mView = view;
        mView.setPresenter(this);
        mRepository = repository;

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void loadRingtoneList() {
        mRepository.getRingtoneList(this);
    }

    @Override
    public void onGetDataSuccess(List<Ringtone> data) {
        mView.showRingtoneList(data);
    }

    @Override
    public void onGetDataFailed(String msg) {

    }
}
