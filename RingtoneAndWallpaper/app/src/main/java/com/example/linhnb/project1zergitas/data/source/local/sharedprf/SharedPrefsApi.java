package com.example.linhnb.project1zergitas.data.source.local.sharedprf;

public interface SharedPrefsApi {

    <T> T get(String key, Class<T> clazz);

    <T> void put(String key, T data);

    void clear();
}
