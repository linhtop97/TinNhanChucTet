package com.vdt.trieuvu310198.goldtime.model;

public class RVTimeZoneModel {
    private String nameCountry;
    private String day;
    private String time;
    private int icpopupmenu;

    public RVTimeZoneModel(String nameCountry, String day, String time, int icpopupmenu) {
        this.nameCountry = nameCountry;
        this.day = day;
        this.time = time;
        this.icpopupmenu = icpopupmenu;
    }

    public RVTimeZoneModel() {
    }

    public String getNameCountry() {
        return nameCountry;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public int getIcpopupmenu() {
        return icpopupmenu;
    }

    public void setNameCountry(String nameCountry) {
        this.nameCountry = nameCountry;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setIcpopupmenu(int icpopupmenu) {
        this.icpopupmenu = icpopupmenu;
    }
}
