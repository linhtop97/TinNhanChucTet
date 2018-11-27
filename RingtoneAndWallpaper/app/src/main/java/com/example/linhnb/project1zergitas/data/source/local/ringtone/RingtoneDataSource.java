package com.example.linhnb.project1zergitas.data.source.local.ringtone;

import com.example.linhnb.project1zergitas.data.model.Ringtone;
import com.example.linhnb.project1zergitas.data.source.DataCallback;

import java.util.List;

public interface RingtoneDataSource {

    interface LocalDataSource extends RingtoneDataSource {

        void getRingtoneList(DataCallback<List<Ringtone>> callback);
    }
}
