package com.tinnhantet.nhantin.hengio.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.tinnhantet.nhantin.hengio.utils.StringUtils;

import java.util.Calendar;
import java.util.List;

public class Message implements Parcelable {

    public Message() {

    }

    public Message(int pendingId, String content, String time, String listContact, Boolean isSend) {
        mPendingId = pendingId;
        mContent = content;
        mTime = time;
        mListContact = listContact;
        mIsSend = isSend;
    }

    private int mPendingId;
    private String mContent;
    private String mTime;
    private String mListContact;
    private Boolean mIsSend;

    protected Message(Parcel in) {
        mPendingId = in.readInt();
        mContent = in.readString();
        mTime = in.readString();
        mListContact = in.readString();
        byte tmpMIsSend = in.readByte();
        mIsSend = tmpMIsSend == 0 ? null : tmpMIsSend == 1;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public int getPendingId() {
        return mPendingId;
    }

    public Message setPendingId(int pendingId) {
        mPendingId = pendingId;
        return this;
    }

    public String getContent() {
        return mContent;
    }

    public Message setContent(String content) {
        mContent = content;
        return this;
    }

    public String getTime() {
        return mTime;
    }

    public Message setTime(String time) {
        mTime = time;
        return this;
    }

    public String getListContact() {
        return mListContact;
    }

    public Message setListContact(String listContact) {
        mListContact = listContact;
        return this;
    }

    public Boolean getSend() {
        return mIsSend;
    }

    public Message setSend(Boolean send) {
        mIsSend = send;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mPendingId);
        dest.writeString(mContent);
        dest.writeString(mTime);
        dest.writeString(mListContact);
        dest.writeByte((byte) (mIsSend == null ? 0 : mIsSend ? 1 : 2));
    }

    public static Message getMessage(List<Contact> contacts, String content, Calendar c) {
        String listContact = StringUtils.listContactString((contacts));
        Message message = new Message();
        message.setListContact(listContact);
        message.setSend(false);
        message.setTime(String.valueOf(c.getTimeInMillis()));
        message.setContent(content);
        return message;
    }
}
