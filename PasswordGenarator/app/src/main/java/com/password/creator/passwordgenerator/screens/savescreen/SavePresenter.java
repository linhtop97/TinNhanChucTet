package com.password.creator.passwordgenerator.screens.savescreen;

import android.util.Log;

import com.password.creator.passwordgenerator.models.UserData;
import com.password.creator.passwordgenerator.models.sources.DataReposite;
import com.password.creator.passwordgenerator.models.sources.DataSource;
import com.password.creator.passwordgenerator.models.sources.MyFile;
import com.password.creator.passwordgenerator.utils.Constant;


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
        if (title.equals("")){
            title = Constant.defaultString;
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
