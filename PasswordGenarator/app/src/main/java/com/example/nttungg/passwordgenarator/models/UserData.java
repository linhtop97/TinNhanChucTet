package com.example.nttungg.passwordgenarator.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by nttungg on 6/19/18.
 */

public class UserData implements Parcelable,Serializable{
    private String title;
    private String account;
    private String password;
    private String catogory;

    protected UserData(Parcel in) {
        title = in.readString();
        account = in.readString();
        password = in.readString();
        catogory = in.readString();
    }

    public UserData(String title, String account, String password, String catogory) {
        this.title = title;
        this.account = account;
        this.password = password;
        this.catogory = catogory;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCatogory(String catogory) {
        this.catogory = catogory;
    }

    public String getTitle() {
        return title;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getCatogory() {
        return catogory;
    }

    public static Creator<UserData> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(account);
        parcel.writeString(password);
        parcel.writeString(catogory);
    }

    @Override
    public String toString() {
        return title + "\n" + account + "\n" + password + "\n" + catogory + "\n";
    }
}
