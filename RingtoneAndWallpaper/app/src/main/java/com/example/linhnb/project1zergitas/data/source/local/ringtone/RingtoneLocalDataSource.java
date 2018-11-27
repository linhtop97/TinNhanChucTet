package com.example.linhnb.project1zergitas.data.source.local.ringtone;

import com.example.linhnb.project1zergitas.MainApplication;
import com.example.linhnb.project1zergitas.R;
import com.example.linhnb.project1zergitas.data.model.Ringtone;
import com.example.linhnb.project1zergitas.data.source.DataCallback;

import java.util.ArrayList;
import java.util.List;

public class RingtoneLocalDataSource implements RingtoneDataSource.LocalDataSource {

    private static RingtoneLocalDataSource sInstance;

    private RingtoneLocalDataSource() {
    }

    public static RingtoneLocalDataSource getInstance() {
        if (sInstance == null) {
            synchronized (RingtoneLocalDataSource.class) {
                if (sInstance == null) {
                    sInstance = new RingtoneLocalDataSource();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void getRingtoneList(DataCallback<List<Ringtone>> callback) {
        List<Ringtone> ringtoneList = new ArrayList<>();
        String[] arrRingtone = MainApplication.getInstance().getResources().getStringArray(R.array.ringtone_array);
        for (String anArrRingtone : arrRingtone) {
            ringtoneList.add(new Ringtone(anArrRingtone));
        }
        callback.onGetDataSuccess(ringtoneList);
    }
}