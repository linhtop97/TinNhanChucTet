package com.tinnhantet.nhantin.hengio.utils;

import java.text.SimpleDateFormat;

public class DateTimeUtil {
    public static String convertTimeToString(Long time) {
        String myFormat1 = "kk:mm";
        String myFormat2 = "dd/MM/yyyy";
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1);
        SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2);
        String s1 = "Vào lúc: " + sdf1.format(time);
        String s2 = " Ngày: " + sdf2.format(time);
        return s1 + s2;
    }
}
