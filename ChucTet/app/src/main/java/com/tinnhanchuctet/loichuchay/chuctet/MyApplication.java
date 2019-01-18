package com.tinnhanchuctet.loichuchay.chuctet;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.tinnhanchuctet.loichuchay.chuctet.database.sqlite.DatabaseHelper;
import com.zer.android.newsdk.ZAndroidSystems;

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
        ZAndroidSystems.initApplication(this, getApplicationContext().getPackageName());
        initDB();
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/font_app_n.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }

    private void initDB() {
        DatabaseHelper helper = DatabaseHelper.getInstance(this);
        File file = this.getDatabasePath(DatabaseHelper.DB_NAME);
        if (!file.exists()) {
            helper.getReadableDatabase();
            helper.close();
            helper.copyDatabase(this);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
