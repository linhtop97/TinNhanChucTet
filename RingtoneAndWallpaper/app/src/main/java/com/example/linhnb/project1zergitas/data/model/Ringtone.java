package com.example.linhnb.project1zergitas.data.model;

public class Ringtone {
    private String mName;

    public Ringtone(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public Ringtone setName(String name) {
        mName = name;
        return this;
    }
}
