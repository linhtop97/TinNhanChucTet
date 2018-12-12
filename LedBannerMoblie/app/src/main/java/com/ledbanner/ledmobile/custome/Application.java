package com.ledbanner.ledmobile.custome;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.zer.android.newsdk.ZAndroidSDK;

/**
 * Created by Trung Tran Thanh on 7/26/2017.
 */

public class Application extends android.app.Application {

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
