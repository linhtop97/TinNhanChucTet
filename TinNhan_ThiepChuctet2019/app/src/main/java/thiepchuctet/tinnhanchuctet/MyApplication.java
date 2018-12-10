package thiepchuctet.tinnhanchuctet;

import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication sMainApplication;

    public static MyApplication getInstance() {
        return sMainApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sMainApplication = this;
    }

}
