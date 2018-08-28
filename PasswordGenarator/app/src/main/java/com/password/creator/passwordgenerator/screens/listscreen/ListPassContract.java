package com.password.creator.passwordgenerator.screens.listscreen;

import android.content.Context;

import com.password.creator.passwordgenerator.models.UserData;

import java.util.ArrayList;

/**
 * This specifies the contract between the view and the presenter.
 */
interface ListPassContract {
    /**
     * View.
     */
    interface View {
        Context getContext();
        void getSuccess(ArrayList<UserData> userData);
        void getFailed();
        void deleteSuccess(ArrayList<UserData> userData);
        boolean getIsCategory();
        void deleteCategoryData(int i);
        void showFloatingWindow(UserData userData);
        void dataEmpty(ArrayList<Integer> position);
    }

    /**
     * Presenter.
     */
    interface Presenter {
        void readData();
        void deleteData(int i);
        void searchCategory(ArrayList<UserData> userData,String category);
        void setNewPositon();
        void searchTitle(ArrayList<UserData> userData,String title);
        void showWindow(UserData userData);
        ArrayList<Integer> getPositionList();
        void initPosition(ArrayList<UserData> userData);
    }
}
