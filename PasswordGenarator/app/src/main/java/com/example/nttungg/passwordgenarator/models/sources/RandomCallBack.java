package com.example.nttungg.passwordgenarator.models.sources;

/**
 * Created by nttungg on 6/29/18.
 */

public interface RandomCallBack{

    void onStartLoading();

    void onRandomSuccess(String data);

    void onRandomFailure(String message);

    void onComplete();

}
