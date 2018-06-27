package com.example.nttungg.passwordgenarator.screens.homescreen;

import com.example.nttungg.passwordgenarator.utils.RandomString;

import java.util.Random;

/**
 * Listens to user actions from the UI ({@link HomeActivity}), retrieves the data and updates
 * the UI as required.
 */
public class HomePresenter  implements HomeContract.Presenter {

    private HomeContract.View mView;
    private String mRandomStringResult;

    public HomePresenter(HomeContract.View view) {
        mView = view;
    }

    @Override
    public String RandomString(boolean isNotSimilar, boolean isCap, boolean isLower, boolean isNumber,
                               boolean isOptionaChar, boolean isOption,boolean isSign,int length) {
        if (!isOption && isNotSimilar){
            RandomString.randomString = RandomString.alphanum;
            return new RandomString(length).nextStringNotSimilar();
        }else if (!isOption && !isNotSimilar){
            RandomString.randomString = RandomString.alphanum;
            return new RandomString(length).nextString();
        }else{
            String randomString  = new String();
            if (isCap){
                randomString = randomString + RandomString.upper;
            }
            if (isLower){
                randomString = randomString + RandomString.lower;
            }
            if (isNumber){
                randomString = randomString + RandomString.digits;
            }
            if (isSign){
                randomString = randomString + RandomString.sign;
            }
            if(!isCap && !isLower && !isNumber && !isSign) {
                randomString = RandomString.alphanum;
            }
            RandomString.randomString = randomString;
            if (isNotSimilar){
                mRandomStringResult = new RandomString(length).nextStringNotSimilar();
            }else{
                mRandomStringResult = new RandomString(length).nextString();
            }
            if (isOptionaChar){
                String optinalString;
                if (mView.getOptinalString() != null){
                    optinalString = mView.getOptinalString();
                    StringBuilder stringBuilder = new StringBuilder(mRandomStringResult);
                    int startindex = new Random().nextInt(length-optinalString.length()-1);
                    int endindex = startindex + optinalString.length();
                    mRandomStringResult = stringBuilder.replace(startindex,endindex,optinalString).toString();
                }
            }
        }
        return mRandomStringResult;
    }
}
