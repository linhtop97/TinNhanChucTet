package com.tinnhantet.nhantin.hengio.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DeliveredMessage extends BroadcastReceiver {
    public static final String TAG = "DeliveredMessage";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (getResultCode() == Activity.RESULT_OK) {
            Log.i(TAG, "onReceive: " + "dã nhận");
        }
    }
}
