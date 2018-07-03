package com.example.nttungg.passwordgenarator.screens.listscreen;

import android.content.Context;

import com.example.nttungg.passwordgenarator.models.UserData;

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
    }
}
