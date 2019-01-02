package com.tinnhantet.nhantin.hengio.listeners;

public interface DataCallBack<T> {

    void onDataSuccess(T data);

    void onDataFailed(String msg);
}
