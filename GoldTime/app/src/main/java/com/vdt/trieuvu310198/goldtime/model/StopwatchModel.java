package com.vdt.trieuvu310198.goldtime.model;

public class StopwatchModel {
    private int count;
    private String time;

    public StopwatchModel() {
    }

    public StopwatchModel(int count, String time) {
        this.count = count;
        this.time = time;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public String getTime() {
        return time;
    }
}
