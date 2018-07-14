package com.vdt.trieuvu310198.goldtime.model;

public class DataAlarmModel {
    private int hour;
    private int minute;
    private int volume;
    private boolean mo;
    private boolean tu;
    private boolean we;
    private boolean th;
    private boolean fr;
    private boolean sa;
    private boolean su;
    private int positonMusic;
    private int positionTimeBack;
    private String countTimeBack;
    private int positionTimeOn;
    private String note;
    private boolean status;

    public DataAlarmModel() {
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {

        return status;
    }

    public DataAlarmModel(int hour, int minute, int volume, boolean mo,
                          boolean tu, boolean we, boolean th, boolean fr,
                          boolean sa, boolean su, int positonMusic, int positionTimeBack,
                          String countTimeBack, int positionTimeOn, String note, boolean status) {
        this.hour = hour;
        this.minute = minute;
        this.volume = volume;
        this.mo = mo;
        this.tu = tu;
        this.we = we;
        this.th = th;
        this.fr = fr;
        this.sa = sa;
        this.su = su;
        this.positonMusic = positonMusic;
        this.positionTimeBack = positionTimeBack;
        this.countTimeBack = countTimeBack;
        this.positionTimeOn = positionTimeOn;
        this.note = note;
        this.status = status;

    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getVolume() {
        return volume;
    }

    public boolean isMo() {
        return mo;
    }

    public boolean isTu() {
        return tu;
    }

    public boolean isWe() {
        return we;
    }

    public boolean isTh() {
        return th;
    }

    public boolean isFr() {
        return fr;
    }

    public boolean isSa() {
        return sa;
    }

    public boolean isSu() {
        return su;
    }

    public int getPositonMusic() {
        return positonMusic;
    }

    public int getPositionTimeBack() {
        return positionTimeBack;
    }

    public String getCountTimeBack() {
        return countTimeBack;
    }

    public int getPositionTimeOn() {
        return positionTimeOn;
    }

    public String getNote() {
        return note;
    }

    public void setHour(int hour) {

        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setMo(boolean mo) {
        this.mo = mo;
    }

    public void setTu(boolean tu) {
        this.tu = tu;
    }

    public void setWe(boolean we) {
        this.we = we;
    }

    public void setTh(boolean th) {
        this.th = th;
    }

    public void setFr(boolean fr) {
        this.fr = fr;
    }

    public void setSa(boolean sa) {
        this.sa = sa;
    }

    public void setSu(boolean su) {
        this.su = su;
    }

    public void setPositonMusic(int positonMusic) {
        this.positonMusic = positonMusic;
    }

    public void setPositionTimeBack(int positionTimeBack) {
        this.positionTimeBack = positionTimeBack;
    }

    public void setCountTimeBack(String countTimeBack) {
        this.countTimeBack = countTimeBack;
    }

    public void setPositionTimeOn(int positionTimeOn) {
        this.positionTimeOn = positionTimeOn;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
