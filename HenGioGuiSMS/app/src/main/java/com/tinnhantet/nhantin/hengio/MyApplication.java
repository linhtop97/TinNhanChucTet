package com.tinnhantet.nhantin.hengio;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.tinnhantet.nhantin.hengio.database.sqlite.MessageDatabaseHelper;
import com.zer.android.newsdk.ZAndroidSystems;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApplication extends Application {

    private static MyApplication sInstance;

    public static MyApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        ZAndroidSystems.initApplication(this, getApplicationContext().getPackageName());
        MessageDatabaseHelper messageDatabaseHelper = MessageDatabaseHelper.getInstance(this);
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/text_app.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
