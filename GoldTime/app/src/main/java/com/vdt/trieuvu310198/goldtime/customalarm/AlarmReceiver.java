package com.vdt.trieuvu310198.goldtime.customalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vdt.trieuvu310198.goldtime.util.Constrain;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent myIntent = new Intent(context, Music.class);
        myIntent.putExtra(Constrain.PROGRESS_SEEKBAR, intent.getIntExtra(Constrain.PROGRESS_SEEKBAR, 0));
        myIntent.putExtra(Constrain.POSITION_MUSIC, intent.getIntExtra(Constrain.POSITION_MUSIC, 0));
        myIntent.putExtra(Constrain.POSITION_TIMEBACK, intent.getIntExtra(Constrain.POSITION_TIMEBACK, 1));
        myIntent.putExtra(Constrain.POSITION_TIMEON, intent.getIntExtra(Constrain.POSITION_TIMEON, 2));
        context.startService(myIntent);
    }
}
