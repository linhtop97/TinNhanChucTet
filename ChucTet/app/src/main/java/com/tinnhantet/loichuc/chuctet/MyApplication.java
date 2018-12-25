package com.tinnhantet.loichuc.chuctet;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.tinnhantet.loichuc.chuctet.database.sqlite.DatabaseHelper;
import com.zer.android.newsdk.ZAndroidSDK;

import java.io.File;

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
        ZAndroidSDK.initApplication(this, getApplicationContext().getPackageName());
        initDB();
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/font_app.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }

    private void initDB() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        File database = this.getDatabasePath(DatabaseHelper.DBNAME);
        if (!database.exists()) {
            databaseHelper.getReadableDatabase();
         databaseHelper.close();
            //copyDB
            databaseHelper.copyDatabase(this);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
