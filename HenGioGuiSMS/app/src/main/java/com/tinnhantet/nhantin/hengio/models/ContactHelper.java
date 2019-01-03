package com.tinnhantet.nhantin.hengio.models;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.tinnhantet.nhantin.hengio.listeners.DataCallBack;

import java.util.ArrayList;
import java.util.List;

public class ContactHelper {
    public static void getAllContact(Context context, DataCallBack<List<Contact>> callBack) {
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
}
