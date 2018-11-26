package com.example.linhnb.project1zergitas.data.source.local.ringtone;

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
    public void getRingtoneList(LoadTracksCallback callback) {
        //get Ringtone list at here
    }
}