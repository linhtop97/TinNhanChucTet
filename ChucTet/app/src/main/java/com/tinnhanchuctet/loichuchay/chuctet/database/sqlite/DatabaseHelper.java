package com.tinnhanchuctet.loichuchay.chuctet.database.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tinnhanchuctet.loichuchay.chuctet.models.Message;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "tinnhanchuctet.sqlite";
    private static DatabaseHelper sInstance;
    public static final String DB_LOCATION = "/data/data/com.tinnhanchuctet.loichuchay.chuctet/databases/";
    private static final int DB_VERSION = 1;
    private Context mCxt;
    private SQLiteDatabase mSQLiteDatabase;


    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mCxt = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//CREATE TABLE `tbl_General` ( `ID` INTEGER PRIMARY KEY AUTOINCREMENT, `Content` TEXT NOT NULL )
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void openDataBase() {
        String dbPath = mCxt.getDatabasePath(DB_NAME).getPath();
        if (mSQLiteDatabase != null && mSQLiteDatabase.isOpen()) {
            return;
        }
        mSQLiteDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mSQLiteDatabase != null) {
            mSQLiteDatabase.close();
        }
    }

    public boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open("database/" + DatabaseHelper.DB_NAME);
            String outFileName = DatabaseHelper.DB_LOCATION + DatabaseHelper.DB_NAME;
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
        Cursor cursor = mSQLiteDatabase.rawQuery("select * from " + tbl, null);
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
        mSQLiteDatabase.insert(TableEntity.TBL_MY_MESSAGE, null, contentValues);
    }

    public int updateMessage(Message msg) {
        openDataBase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableEntity.GENERAL_CONTENT, msg.getContent());
        return mSQLiteDatabase.update(TableEntity.TBL_MY_MESSAGE, contentValues,
                TableEntity.GENERAL_ID + "=?", new String[]{String.valueOf(msg.getId())});
    }

    public int deleteMessage(String tblName, int id) {
        openDataBase();
        return mSQLiteDatabase.delete(tblName, TableEntity.GENERAL_ID + "=?", new String[]{String.valueOf(id)});
    }
}
