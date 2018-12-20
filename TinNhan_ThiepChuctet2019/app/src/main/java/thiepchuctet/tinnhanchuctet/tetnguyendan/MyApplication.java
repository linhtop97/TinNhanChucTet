package thiepchuctet.tinnhanchuctet.tetnguyendan;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import java.io.File;

import thiepchuctet.tinnhanchuctet.tetnguyendan.database.sqlite.DatabaseHelper;

public class MyApplication extends Application {

    private static MyApplication sInstance;

    public static MyApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        requestPermision();
        initDB();
    }

    private void requestPermision() {
    }

    private void initDB() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        File database = this.getDatabasePath(DatabaseHelper.DBNAME);
        if (!database.exists()) {
            databaseHelper.getReadableDatabase();
            databaseHelper.close();
            //copyDB
            if (databaseHelper.copyDatabase(this)) {
                Toast.makeText(this, "Copy sucessful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Copy failed", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
