package com.example.linhnb.project1zergitas.data.source;

public interface DataCallback<T> {
    void onGetDataSuccess(T data);

    void onGetDataFailed(String msg);
}
