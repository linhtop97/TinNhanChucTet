package com.vdt.trieuvu310198.goldtime.model;

import java.io.Serializable;
import java.util.ArrayList;

public class AlarmRV implements Serializable {
    private String hour;
    private String minute;
    private String day;
    private ArrayList<Integer> alarmDayList;
    private Boolean alarmStatus;
    private int popupMenu;

    public AlarmRV(String hour, String minute, String day, Boolean alarmStatus, int popupMenu) {
        this.hour = hour;
        this.minute = minute;
        this.day = day;
        this.alarmStatus = alarmStatus;
        this.popupMenu = popupMenu;
    }

    public AlarmRV() {
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getDay() {
        return day;
    }

    public Boolean getAlarmStatus() {
        return alarmStatus;
    }

    public int getPopupMenu() {
        return popupMenu;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setAlarmStatus(Boolean alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public void setPopupMenu(int popupMenu) {
        this.popupMenu = popupMenu;
    }

    public void setAlarmDayList(ArrayList<Integer> alarmDayList) {
        this.alarmDayList = alarmDayList;
    }

    public ArrayList<Integer> getAlarmDayList() {

        return alarmDayList;
    }
}
