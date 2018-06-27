package com.example.nttungg.passwordgenarator.models.sources;

import android.content.Context;

import com.example.nttungg.passwordgenarator.models.UserData;

import java.util.ArrayList;

/**
 * Created by nttungg on 6/22/18.
 */

public class DataReposite implements DataSource {

    public MyFile mMyFile;

    public DataReposite(MyFile myfile) {
        mMyFile = myfile;
    }
    @Override
    public void saveDataDefault(Callback callback, Context context, String account, String password, String catogory, String title) {
        mMyFile.saveDataDefault(callback,context,account,password,catogory,title);
    }

    @Override
    public void editData(Callback callback, Context context, String account, String password, String catogory, String title, int i) {
        mMyFile.editData(callback,context,account,password,catogory,title,i);
    }

    @Override
    public void deleteData(Callback callback, Context context, int i) {
        mMyFile.deleteData(callback,context,i);
    }

    @Override
    public void readData(Context context, Callback<ArrayList<UserData>> callback,boolean isRemove) {
        mMyFile.readData(context,callback,isRemove);
    }
}
