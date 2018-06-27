package com.example.nttungg.passwordgenarator.utils;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

/**
 * Created by nttungg on 6/18/18.
 */

public class RandomString {

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String lower = upper.toLowerCase(Locale.ROOT);

    public static final String digits = "0123456789";

    public static final String sign = "!@#$%^&*";

    public static String alphanum = upper + lower + digits + sign;

    public static String randomString = alphanum;

    private final Random random;

    private final char[] symbols;

    private final char[] buf;

    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
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

    public RandomString(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Create an alphanumeric string generator.
     */
    public RandomString(int length, Random random) {
        this(length, random, randomString);
    }

    /**
     * Create an alphanumeric strings from a secure generator.
     */
    public RandomString(int length) {
        this(length, new SecureRandom());
    }

    /**
     * Create session identifiers.
     */
    public RandomString() {
        this(10);
    }
}
