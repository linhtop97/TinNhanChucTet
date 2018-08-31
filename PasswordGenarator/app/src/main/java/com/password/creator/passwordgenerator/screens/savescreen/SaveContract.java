package com.password.creator.passwordgenerator.screens.savescreen;


import android.content.Context;

import com.password.creator.passwordgenerator.models.UserData;

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
