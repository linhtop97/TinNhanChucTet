package com.vdt.trieuvu310198.goldtime.customprompt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
            context.startService(myIntent);
        }
    }
}
