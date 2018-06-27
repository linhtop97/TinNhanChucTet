package com.example.nttungg.passwordgenarator.models.sources;

import android.content.Context;

import com.example.nttungg.passwordgenarator.models.UserData;

import java.util.ArrayList;

/**
 * Created by nttungg on 6/22/18.
 */

public interface DataSource {

    interface Callback<T> {

        void onGetSuccess(T data);

        void onWriteSucess();

        void onGetFailure(String message);

    }

    void saveDataDefault(Callback callback , Context context, String account, String password, String catogory, String title);

    void editData(Callback callback , Context context, String account, String password, String catogory, String title,int i);

    void deleteData(Callback callback , Context context, int i);

    void readData(Context context,Callback<ArrayList<UserData>> callback,boolean isRemove);
}
