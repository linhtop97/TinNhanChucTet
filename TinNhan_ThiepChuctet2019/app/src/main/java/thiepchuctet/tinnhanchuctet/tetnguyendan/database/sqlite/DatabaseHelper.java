package thiepchuctet.tinnhanchuctet.tetnguyendan.database.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import thiepchuctet.tinnhanchuctet.tetnguyendan.models.Message;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "TetNguyenDan.sqlite";
    private static DatabaseHelper sInstance;
    //public static final String DBLOCATION = "/data/data/thiepchuctet.tinnhanchuctet.tetnguyendan/databases/";
    private static final int DBVERSION = 1;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
        mContext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void openDataBase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open("database/" + DatabaseHelper.DBNAME);
            File outFile = context.getDatabasePath(DatabaseHelper.DBNAME);
            String outFileName = outFile.getPath();
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int lenght = 0;
            while ((lenght = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, lenght);
            }
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Message> getListMsg(String tbl) {
        openDataBase();
        List<Message> messages = new ArrayList<>();
        Cursor cursor = mDatabase.rawQuery("select * from " + tbl, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message message = new Message(cursor.getInt(cursor.getColumnIndex(TableEntity.GENERAL_ID)),
                    cursor.getString(cursor.getColumnIndex(TableEntity.GENERAL_CONTENT)));
            messages.add(message);
            cursor.moveToNext();
        }
        cursor.close();
        return messages;
    }

    public void insertNewMessage(String msg) {
        openDataBase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableEntity.GENERAL_CONTENT, msg);
        mDatabase.insert(TableEntity.TBL_MY_MESSAGE, null, contentValues);
    }
}
