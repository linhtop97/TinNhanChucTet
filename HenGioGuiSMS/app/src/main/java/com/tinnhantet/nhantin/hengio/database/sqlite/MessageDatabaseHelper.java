package com.tinnhantet.nhantin.hengio.database.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tinnhantet.nhantin.hengio.models.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageDatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "MessageDB.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "MessageDatabaseHelper";
    private Context mContext;
    private static MessageDatabaseHelper sInstance;

    public static final String TBL_MSG = "tbl_MessageShedule";
    public static final String ID = "id";
    public static final String CONTENT = "Content";
    public static final String LIST_CONTACT = "ListContact";
    public static final String TIME = "Time";
    public static final String IS_SENT = "IsSend";


    public static synchronized MessageDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MessageDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private MessageDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        mContext = context;
        Log.d(TAG, "MessageDatabaseHelper: " + script);
    }

    String script = "CREATE TABLE " + TBL_MSG + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CONTENT + " TEXT NOT NULL,"
            + LIST_CONTACT + " TEXT NOT NULL,"
            + TIME + " TEXT NOT NULL,"
            + IS_SENT + " INTEGER DEFAULT 0"
            + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_MSG);
        onCreate(db);
    }

    public long addMsg(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTENT, message.getContent());
        values.put(LIST_CONTACT, message.getListContact());
        values.put(TIME, message.getTime());
        values.put(IS_SENT, 0);
        long id = db.insert(TBL_MSG, null, values);
        db.close();
        return id;
    }

    public int removeMsgToSent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IS_SENT, 1);
        return db.update(TBL_MSG, values,
                ID + "=?", new String[]{String.valueOf(id)});
    }

    public int deleteMsg(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TBL_MSG, ID + "=?", new String[]{String.valueOf(id)});
    }

    public int updateMsg(Message msg) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTENT, msg.getContent());
        values.put(LIST_CONTACT, msg.getListContact());
        values.put(TIME, msg.getTime());
        if (msg.getSend()) {
            values.put(IS_SENT, 1);
        } else {
            values.put(IS_SENT, 0);
        }
        values.put(IS_SENT, 0);
        return db.update(TBL_MSG, values,
                ID + "=?", new String[]{String.valueOf(msg.getPendingId())});
    }

    public List<Message> getAllMsgPending() {
        List<Message> msgList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TBL_MSG;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Message msg = new Message();
                msg.setPendingId(cursor.getInt(cursor.getColumnIndex(ID)));
                msg.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
                msg.setListContact(cursor.getString(cursor.getColumnIndex(LIST_CONTACT)));
                msg.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                int b = cursor.getInt(cursor.getColumnIndex(IS_SENT));
                if (b == 0) {
                    msg.setSend(false);
                    msgList.add(msg);
                }
            }
            while (cursor.moveToNext());
        }
        int size = msgList.size();
        if (size > 1) {
            for (int i = 0; i < size; i++) {
                for (int j = i + 1; j < size; j++) {
                    Message one = msgList.get(i);
                    Message two = msgList.get(j);
                    if (Long.parseLong(one.getTime()) > Long.parseLong(two.getTime())) {
                        msgList.set(i, two);
                        msgList.set(j, one);
                    }
                }
            }
        }

        return msgList;
    }

    public Message getMessageById(long id) {
        Message msg = null;
        String selectQuery = "SELECT * FROM " + TBL_MSG + " where id = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            msg = new Message();
            msg.setPendingId(cursor.getInt(cursor.getColumnIndex(ID)));
            msg.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
            msg.setListContact(cursor.getString(cursor.getColumnIndex(LIST_CONTACT)));
            msg.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
        }
        return msg;
    }

    public List<Message> getAllMsgSent() {
        List<Message> msgList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TBL_MSG;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Message msg = new Message();
                msg.setPendingId(cursor.getInt(cursor.getColumnIndex(ID)));
                msg.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
                msg.setListContact(cursor.getString(cursor.getColumnIndex(LIST_CONTACT)));
                msg.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                int b = cursor.getInt(cursor.getColumnIndex(IS_SENT));
                if (b == 1) {
                    msg.setSend(true);
                    msgList.add(msg);
                }
            }
            while (cursor.moveToNext());
        }

        int size = msgList.size();
        if (size > 1) {
            for (int i = 0; i < size; i++) {
                for (int j = i + 1; j < size; j++) {
                    Message one = msgList.get(i);
                    Message two = msgList.get(j);
                    if (Long.parseLong(one.getTime()) < Long.parseLong(two.getTime())) {
                        msgList.set(i, two);
                        msgList.set(j, one);
                    }
                }
            }
        }
        return msgList;
    }
}
