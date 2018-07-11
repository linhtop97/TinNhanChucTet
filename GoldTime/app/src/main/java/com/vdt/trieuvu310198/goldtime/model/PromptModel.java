package com.vdt.trieuvu310198.goldtime.model;

public class PromptModel {
    private String note;
    private String calendar;
    private String time;
    private int icon;
    private int background;
    private int ringTore;
    private int icpopup;

    public PromptModel() {
    }

    public PromptModel(String note, String calendar, String time, int icon, int background, int ringTore, int icpopup) {
        this.note = note;
        this.calendar = calendar;
        this.time = time;
        this.icon = icon;
        this.background = background;
        this.ringTore = ringTore;
        this.icpopup = icpopup;
    }


    public void setNote(String note) {
        this.note = note;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public void setRingTore(int ringTore) {
        this.ringTore = ringTore;
    }

    public void setIcpopup(int icpopup) {
        this.icpopup = icpopup;
    }

    public String getNote() {
        return note;
    }

    public String getCalendar() {
        return calendar;
    }

    public String getTime() {
        return time;
    }

    public int getIcon() {
        return icon;
    }

    public int getBackground() {
        return background;
    }

    public int getRingTore() {
        return ringTore;
    }

    public int getIcpopup() {
        return icpopup;
    }
}
