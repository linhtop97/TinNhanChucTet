package com.password.creator.passwordgenerator.screens.listscreen;

import android.util.Log;

import com.password.creator.passwordgenerator.models.UserData;
import com.password.creator.passwordgenerator.models.sources.DataReposite;
import com.password.creator.passwordgenerator.models.sources.DataSource;
import com.password.creator.passwordgenerator.models.sources.MyFile;

import java.util.ArrayList;

/**
 * Listens to user actions from the UI ({@link ListPassActivity}), retrieves the data and updates
 * the UI as required.
 */
public class ListPassPresenter implements ListPassContract.Presenter {
    private ListPassContract.View mView;
    private ArrayList<UserData> mUserDatas = new ArrayList<>();
    private ArrayList<Integer> mPositions = new ArrayList<>();
    private DataReposite mDataReposite;

    public ListPassPresenter(ListPassContract.View view) {
        mView = view;
        mDataReposite = new DataReposite(new MyFile(mView.getContext()));
    }


    @Override
    public void readData() {
        setNewPositon();
        mDataReposite.readData(mView.getContext(), new DataSource.Callback<ArrayList<UserData>>() {
            @Override
            public void onGetSuccess(ArrayList<UserData> data) {
                mUserDatas = new ArrayList<>();
                mUserDatas.addAll(data);
                initPosition(data);
                mView.dataEmpty(mPositions);
                Log.d("TAG", "onGetSuccess: " + mPositions.size());
                mView.getSuccess(mUserDatas);
            }

            @Override
            public void onWriteSucess() {

            }

            @Override
            public void onGetFailure(String message) {
                mView.getFailed();
            }
        },true);
    }

    @Override
    public void deleteData(int i){
        if (mView.getIsCategory()){
            mView.deleteCategoryData(i);
        }
        mDataReposite.deleteData(new DataSource.Callback<ArrayList<UserData>>() {
            @Override
            public void onGetSuccess(ArrayList<UserData> data) {
                mUserDatas = new ArrayList<>();
                mUserDatas.addAll(data);
                initPosition(data);
                mView.deleteSuccess(data);
            }

            @Override
            public void onWriteSucess() {

            }

            @Override
            public void onGetFailure(String message) {

            }
        },mView.getContext(),mPositions.get(i));
    }

    @Override
    public void initPosition(ArrayList<UserData> userData){
        mPositions = new ArrayList<>();
        for (int i=0;i<userData.size();i++){
            mPositions.add(i);
        }
    }

    @Override
    public void searchCategory(ArrayList<UserData> userDatas,String category) {
        for (int i=0;i< mUserDatas.size();i++){
            if (mUserDatas.get(i).getCatogory().equals(category)){
                userDatas.add(mUserDatas.get(i));
                mPositions.add(i);
            }
        }
        mView.dataEmpty(mPositions);
    }

    @Override
    public void setNewPositon() {
        mPositions = new ArrayList<>();
    }

    @Override
    public void searchTitle(ArrayList<UserData> userData, String title) {
        for (int i=0;i< mUserDatas.size();i++){
            if (mUserDatas.get(i).getTitle().contains(title)
                    || mUserDatas.get(i).getTitle().toLowerCase().contains(title.toLowerCase())
                    || mUserDatas.get(i).getTitle().toUpperCase().contains(title.toUpperCase())){
                userData.add(mUserDatas.get(i));
                mPositions.add(i);
            }
        }
        mView.dataEmpty(mPositions);
    }

    @Override
    public void showWindow(UserData userData) {
        mView.showFloatingWindow(userData);
    }

    @Override
    public ArrayList<Integer> getPositionList() {
        return mPositions;
    }
}
