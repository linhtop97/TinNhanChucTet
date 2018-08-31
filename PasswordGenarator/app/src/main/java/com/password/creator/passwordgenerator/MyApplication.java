package com.password.creator.passwordgenerator;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.zer.android.newsdk.ZAndroidSDK;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ZAndroidSDK.initApplication(this, getApplicationContext().getPackageName());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
