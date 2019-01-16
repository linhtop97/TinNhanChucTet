package com.tinnhantet.nhantin.hengio.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.tinnhantet.nhantin.hengio.MyApplication;
import com.tinnhantet.nhantin.hengio.services.MessageService;
import com.tinnhantet.nhantin.hengio.utils.Constant;

public class SendMessageBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MessageService.class);
        Bundle bundle = new Bundle();
        Bundle bundle1 = intent.getExtras();
        if (bundle1 != null) {
            long pId = bundle1.getLong(Constant.EXTRA_ID);
            long time = bundle1.getLong(Constant.EXTRA_TIME);
            bundle.putLong(Constant.EXTRA_ID, pId);
            i.putExtras(bundle);
            PendingIntent pendingIntent = PendingIntent.getService(context, (int) pId, i, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager manager = (AlarmManager) MyApplication.getInstance().getSystemService(context.ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        time, pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                manager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            } else {
                manager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
        }
    }
}
