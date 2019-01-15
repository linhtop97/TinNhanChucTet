package com.tinnhantet.nhantin.hengio.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SentMessage extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(DeliveredMessage.TAG, "onReceive: " + "đã gửi");
    }
}
