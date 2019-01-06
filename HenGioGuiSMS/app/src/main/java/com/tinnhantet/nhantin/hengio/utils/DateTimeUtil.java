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

    public static String convertTimeToTime(Long time) {
        String myFormat1 = "kk:mm";
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1);
        String s1 = sdf1.format(time);
        return s1;
    }

    public static String convertTimeToDate(Long time) {
        String myFormat2 = "dd/MM/yyyy";
        SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2);
        String s2 = sdf2.format(time);
        return s2;
    }

    public static String[] separateTime(Long time) {
        String myFormat1 = "kk:mm";
        String myFormat2 = "dd/MM/yyyy";
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1);
        SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2);
        String s1 = sdf1.format(time);
        String s2 = sdf2.format(time);
        String[] timeArr = s1.split(":");
        String[] dateArr = s2.split("/");
        String[] dateTimeArr = new String[]{timeArr[0], timeArr[1], dateArr[0], dateArr[1], dateArr[2]};
        return dateTimeArr;
    }
}
