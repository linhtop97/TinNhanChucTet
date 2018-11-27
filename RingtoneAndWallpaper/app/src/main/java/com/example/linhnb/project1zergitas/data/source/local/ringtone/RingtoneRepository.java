package com.example.linhnb.project1zergitas.data.source.local.ringtone;

import com.example.linhnb.project1zergitas.data.model.Ringtone;
import com.example.linhnb.project1zergitas.data.source.DataCallback;

import java.util.List;

public class RingtoneRepository implements RingtoneDataSource.LocalDataSource {

    private static RingtoneRepository sInstance;
    private RingtoneLocalDataSource mRingtoneLocalDataSource;

    private RingtoneRepository(RingtoneLocalDataSource ringtoneLocalDataSource) {
        mRingtoneLocalDataSource = ringtoneLocalDataSource;
    }

    public static RingtoneRepository getInstance(RingtoneLocalDataSource ringtoneLocalDataSource) {
        if (sInstance == null) {
            synchronized (RingtoneRepository.class) {
                if (sInstance == null) {
                    sInstance = new RingtoneRepository(ringtoneLocalDataSource);
                }
            }
        }
        return sInstance;
    }

    @Override
    public void getRingtoneList(DataCallback<List<Ringtone>> callback) {
        mRingtoneLocalDataSource.getRingtoneList(callback);
    }
}
