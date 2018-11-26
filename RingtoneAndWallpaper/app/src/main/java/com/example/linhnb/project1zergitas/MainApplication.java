package com.example.linhnb.project1zergitas;

import android.app.Application;

public class MainApplication extends Application {

    private static MainApplication sMainApplication;

    public static MainApplication getInstance() {
        return sMainApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sMainApplication = this;
    }
}
