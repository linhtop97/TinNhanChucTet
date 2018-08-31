package com.password.creator.passwordgenerator.screens.homescreen;

/**
 * This specifies the contract between the view and the presenter.
 */
interface HomeContract {
    /**
     * View.
     */
    interface View {
        String getOptinalString();
        void randomSuccess(String result);
        void showEmptyDialog();
        void showLengthDialog();
    }

    /**
     * Presenter.
     */
    interface Presenter {
        void RandomString(boolean isNotSimilar,boolean isCap, boolean isLower,
                            boolean isNumber, boolean isOptionaChar, boolean isOption,boolean isSign,int length);
    }
}
