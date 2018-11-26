package com.example.linhnb.project1zergitas.screen.ringtone;

import com.example.linhnb.project1zergitas.data.model.Ringtone;
import com.example.linhnb.project1zergitas.screen.base.BasePresenter;
import com.example.linhnb.project1zergitas.screen.base.BaseView;

import java.util.List;

interface RingtoneContract {
    interface View extends BaseView<Presenter> {
        void showRingtoneList(List<Ringtone> ringtoneList);
    }

    interface Presenter extends BasePresenter {
        void loadRingtoneList();
    }
}
