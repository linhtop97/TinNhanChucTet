package com.tinnhantet.nhantin.hengio.models;

import android.os.Parcel;
import android.os.Parcelable;

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
