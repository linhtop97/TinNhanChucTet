package com.example.nttungg.passwordgenarator.screens.homescreen;

import android.util.Log;

import com.example.nttungg.passwordgenarator.models.sources.RandomCallBack;
import com.example.nttungg.passwordgenarator.utils.RandomString;

import java.util.Random;

/**
 * Listens to user actions from the UI ({@link HomeActivity}), retrieves the data and updates
 * the UI as required.
 */
public class HomePresenter  implements HomeContract.Presenter {

    private HomeContract.View mView;
    private String mRandomStringResult;
    private boolean mIsOptinalString;
    private int mLength;
    private boolean isEmpty;

    public HomePresenter(HomeContract.View view) {
        mView = view;
    }

    @Override
    public void RandomString(boolean isNotSimilar, boolean isCap, boolean isLower, boolean isNumber,
                               boolean isOptionaChar, boolean isOption,boolean isSign,int length) {
        mIsOptinalString = isOptionaChar;
        mLength = length;
        if (!isOption){
            RandomString.randomString = RandomString.alphanum;
            randomPass(length,isNotSimilar);
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
            randomPass(length,isNotSimilar);
        }
    }

    private void randomOptinalChar(){
        isEmpty = false;
        if (mIsOptinalString){
            String optinalString;
            if (!mView.getOptinalString().equals("")){
                optinalString = mView.getOptinalString();
                Random rd = new Random();
                int number = rd.nextInt(optinalString.length());
                for (int i = 0; i< number ; i++){
                    int rdChar = rd.nextInt(optinalString.length());
                    char s = optinalString.charAt(rdChar);
                    char S = Character.toUpperCase(s);
                    char[] myChars = optinalString.toCharArray();
                    myChars[rdChar] = S;
                    optinalString = String.valueOf(myChars);
                }
                StringBuilder stringBuilder = new StringBuilder(mRandomStringResult);
                if (mLength-optinalString.length()-1 >= 0){
                    int startindex = new Random().nextInt(mLength-optinalString.length()-1);
                    int endindex = startindex + optinalString.length();
                    mRandomStringResult = stringBuilder.replace(startindex,endindex,optinalString).toString();
                    isEmpty = false;
                }else {
                    mView.showLengthDialog();
                    isEmpty = true;
                }
            }else{
                mView.showEmptyDialog();
                isEmpty = true;
            }
        }
    }

    public void randomPass(int length, final boolean isSimilar){
        RandomString.isSimilar = isSimilar;
        new RandomString(length, new RandomCallBack() {
            @Override
            public void onStartLoading() {

            }

            @Override
            public void onRandomSuccess(String data) {
               if (data != null) {
                   mRandomStringResult = data;
                   randomOptinalChar();
                   if (!isEmpty){
                       mView.randomSuccess(mRandomStringResult);
                   }
               }
            }

            @Override
            public void onRandomFailure(String message) {

            }

            @Override
            public void onComplete() {

            }
        }).execute();
    }
}
