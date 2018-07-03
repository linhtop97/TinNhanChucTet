package com.example.nttungg.passwordgenarator.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.nttungg.passwordgenarator.models.sources.RandomCallBack;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

/**
 * Created by nttungg on 6/18/18.
 */

public class RandomString extends AsyncTask<String, String, String> {

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String lower = upper.toLowerCase(Locale.ROOT);

    public static final String digits = "0123456789";

    public static final String sign = "!@#$%^&*";

    public static  boolean isSimilar = false;

    public static String alphanum = upper + lower + digits + sign;

    public static String randomString = alphanum;

    private Random random;

    private final char[] symbols;

    private final char[] buf;

    private RandomCallBack mCallback;

    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    public String nextStringNotSimilar(){
        for (int idx = 0; idx < buf.length; ++idx){
            char s = symbols[random.nextInt(symbols.length)];
            while (checkSimilar(idx,s)){
                s = symbols[random.nextInt(symbols.length)];
            }
            buf[idx] = s;
        }
        return new String(buf);
    }

    public boolean checkSimilar(int idx, char s){
        for (int i = 0; i < idx; ++i){
            if (buf[i] == s) {
                return true;
            }
        }
        return  false;
    }


    public RandomString(int length, Random random, String symbols,RandomCallBack randomCallBack) {
        mCallback = randomCallBack;
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    public RandomString(int length, Random random, RandomCallBack randomCallBack) {
        this(length, random, randomString,randomCallBack);
    }

    public RandomString(int length, RandomCallBack randomCallBack) {
        this(length, new SecureRandom(),randomCallBack);
    }

    @Override
    protected void onPreExecute() {
        mCallback.onStartLoading();
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = null;
        if (isSimilar){
            result = nextString();
        }else {
            result  = nextStringNotSimilar();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (mCallback == null) return;
        if (result != null) {
            mCallback.onRandomSuccess(result);
            mCallback.onComplete();
        }
    }
}
