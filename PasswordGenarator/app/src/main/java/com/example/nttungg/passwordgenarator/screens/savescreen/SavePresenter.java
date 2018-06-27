package com.example.nttungg.passwordgenarator.screens.savescreen;

import android.util.Log;

import com.example.nttungg.passwordgenarator.models.UserData;
import com.example.nttungg.passwordgenarator.models.sources.DataReposite;
import com.example.nttungg.passwordgenarator.models.sources.DataSource;
import com.example.nttungg.passwordgenarator.models.sources.MyFile;
import com.example.nttungg.passwordgenarator.utils.Constant;


/**
 * Listens to user actions from the UI ({@link SaveActivity}), retrieves the data and updates
 * the UI as required.
 */
public class SavePresenter implements SaveContract.Presenter {

    private SaveContract.View mView;
    private DataReposite mDataReposite;

    public SavePresenter(SaveContract.View  view) {
        mView = view;
        mDataReposite = new DataReposite(new MyFile(mView.getContext()));
    }

    @Override
    public void savePassword() {
        String title = mView.getMyTitle();
        String account = mView.getAccount();
        String password = mView.getPassword();
        String category = mView.getMyCategory();
        if (title.equals("")){
            title = Constant.defaultString;
        }
        mDataReposite.saveDataDefault(new DataSource.Callback() {
            @Override
            public void onGetSuccess(Object data) {

            }

            @Override
            public void onWriteSucess() {
                mView.onWriteSucess();
            }

            @Override
            public void onGetFailure(String message) {

            }
        },mView.getContext(),account,password,category,title);
    }

    @Override
    public void editUserData(UserData userData,int i) {
        String title = mView.getMyTitle();
        String account = mView.getAccount();
        String password = mView.getPassword();
        String category = mView.getMyCategory();
        if (title.equals("") || account.equals("")){
            title = Constant.defaultString;
            account = Constant.defaultAccount;
        }
        mDataReposite.editData(new DataSource.Callback() {
            @Override
            public void onGetSuccess(Object data) {

            }

            @Override
            public void onWriteSucess() {
                mView.onEditSucess();
            }

            @Override
            public void onGetFailure(String message) {

            }
        },mView.getContext(),account,password,category,title,i);
    }
}
