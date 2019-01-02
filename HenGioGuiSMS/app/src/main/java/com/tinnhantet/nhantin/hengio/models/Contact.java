package com.tinnhantet.nhantin.hengio.models;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

import com.tinnhantet.nhantin.hengio.listeners.DataCallBack;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class Contact implements Parcelable {

    private int mId;
    private String mName;
    private String mPhone;
    private boolean mIsSelected;

    public Contact() {
    }

    public Contact(int id, String name, String phone) {
        mId = id;
        mName = name;
        mPhone = phone;
        mIsSelected = false;
    }

    protected Contact(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mPhone = in.readString();
        mIsSelected = in.readByte() != 0;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public void getAllContact(Context context, DataCallBack<List<Contact>> callBack) {
        List<Contact> contacts = new ArrayList<>();
        //tạo đối tượng ContentResolver
        ContentResolver cr = context.getContentResolver();
        //truy vấn lấy về Cursor chứa tất cả dữ liệu của danh bạ
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    if (pCur != null) {
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                            contacts.add(new Contact(Integer.parseInt(id), name, phoneNo));
                        }
                        pCur.close();
                    }
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
        callBack.onDataSuccess(contacts);
    }

    public Bitmap openPhoto(Context context, long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
                }
            }
        } finally {
            cursor.close();
        }
        return null;

    }

    //read only file system
//    public void addContact(Context context,Contact contact){
//        ContentResolver cr = context.getContentResolver();
//        ContentValues values = new ContentValues();
//            values.put(ContactsContract.Contacts.DISPLAY_NAME, contact.getmName());
//            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getmPhone());
//            Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, 10000000);
//            cr.insert(uri,values);
//        }
    public int getId() {
        return mId;
    }

    public Contact setId(int id) {
        mId = id;
        return this;
    }

    public String getName() {
        return mName;
    }

    public Contact setName(String name) {
        mName = name;
        return this;
    }

    public String getPhone() {
        return mPhone;
    }

    public Contact setPhone(String phone) {
        mPhone = phone;
        return this;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public Contact setSelected(boolean selected) {
        mIsSelected = selected;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mPhone);
        dest.writeByte((byte) (mIsSelected ? 1 : 0));
    }
}
