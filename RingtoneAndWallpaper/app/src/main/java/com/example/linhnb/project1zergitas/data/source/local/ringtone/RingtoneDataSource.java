package com.example.linhnb.project1zergitas.data.source.local.ringtone;

import com.example.linhnb.project1zergitas.data.model.Ringtone;

import java.util.List;

public interface RingtoneDataSource {

    interface LoadTracksCallback {

        void onRingtoneListLoaded(List<Ringtone> ringtoneList);

        void onDataNotAvailable(String msg);
    }

    interface LocalDataSource extends RingtoneDataSource {

        void getRingtoneList(LoadTracksCallback callback);
    }
}
