package com.example.nttungg.passwordgenarator.screens.savescreen;


import android.content.Context;

import com.example.nttungg.passwordgenarator.models.UserData;

/**
 * This specifies the contract between the view and the presenter.
 */
interface SaveContract {
    /**
     * View.
     */
    interface View {
        String getMyTitle();
        String getAccount();
        String getMyCategory();
        String getPassword();
        Context getContext();
        void onWriteSucess();
        void onEditSucess();
    }

    /**
     * Presenter.
     */
    interface Presenter {
        void savePassword();
        void editUserData(UserData userData,int i);
    }
}
