package com.example.nttungg.passwordgenarator.screens.homescreen;


import android.content.Context;

/**
 * This specifies the contract between the view and the presenter.
 */
interface HomeContract {
    /**
     * View.
     */
    interface View {
        String getOptinalString();
    }

    /**
     * Presenter.
     */
    interface Presenter {
        String RandomString(boolean isNotSimilar,boolean isCap, boolean isLower,
                            boolean isNumber, boolean isOptionaChar, boolean isOption,boolean isSign,int length);
    }
}
