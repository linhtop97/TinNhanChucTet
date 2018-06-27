package com.example.nttungg.passwordgenarator.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.example.nttungg.passwordgenarator.R;

import org.w3c.dom.Text;

/**
 * Created by nttungg on 6/26/18.
 */

public class Utils {
    private static final String FIRST_TIME = "first time";
    private static final String MY_PASSWORD = "my_password";
    private static final String GOOD_PASSWORD = "GOOD";
    private static final String NORMAL_PASSWORD = "NORMAL";
    private static final String BAD_PASSWORD = "BAD";

    public static boolean isFirst(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        if (!pref.getBoolean(FIRST_TIME, true)) {
            return false;
        }
        return true;
    }

    public static String getPass(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(MY_PASSWORD,null);
    }


    public static void setFirstTime(Context context,String myPassword) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FIRST_TIME, false);
        editor.putString(MY_PASSWORD,myPassword);
        editor.commit();
    }

    public static void calculatePasswordStrength(Context context,String password, TextView textView){
        int iPasswordScore = 0;

        if( password.length() < 8 )
            textView.setText(BAD_PASSWORD);
        else if( password.length() >= 10 )
            iPasswordScore += 2;
        else
            iPasswordScore += 1;

        //if it contains one digit, add 2 to total score
        if( password.matches("(?=.*[0-9]).*") )
            iPasswordScore += 2;

        //if it contains one lower case letter, add 2 to total score
        if( password.matches("(?=.*[a-z]).*") )
            iPasswordScore += 2;

        //if it contains one upper case letter, add 2 to total score
        if( password.matches("(?=.*[A-Z]).*") )
            iPasswordScore += 2;

        //if it contains one special character, add 2 to total score
        if( password.matches("(?=.*[~!@#$%^&*()_-]).*") )
            iPasswordScore += 2;
        if (iPasswordScore >=0 && iPasswordScore<=3){
            textView.setText(BAD_PASSWORD);
            textView.setTextColor(context.getResources().getColor(R.color.colorRed));
        }else if(iPasswordScore >= 4 && iPasswordScore <=7){
            textView.setText(NORMAL_PASSWORD);
            textView.setTextColor(context.getResources().getColor(R.color.colorOrange));
        }else{
            textView.setText(GOOD_PASSWORD);
            textView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
    }
}
