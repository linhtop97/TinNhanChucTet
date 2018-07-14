package com.vdt.trieuvu310198.goldtime.customprompt;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

public class PromptReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, PromptService.class);
        myIntent.putExtra("RINGTORE", intent.getIntExtra("RINGTORE", -1));
        myIntent.putExtra("NOTEPROMPT", intent.getStringExtra("NOTEPROMPT"));
        myIntent.putExtra("POSITION", intent.getIntExtra("POSITION", -1));
        long time = intent.getLongExtra("TIME", -1);
        if (System.currentTimeMillis() - time > 60000) {

        } else {
            Log.e("XXXXX", "okokok");
            if (!isMyServiceRunning(PromptService.class, context)) {
                context.startService(myIntent);
            }

        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;

    }
}
