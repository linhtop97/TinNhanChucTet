package com.example.linhnb.project1zergitas.screen.ringtone;

public class RingtonePresenter implements RingtoneContract.Presenter {

    private final RingtoneContract.View mView;

    public RingtonePresenter(RingtoneContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void loadRingtoneList() {

    }
}
