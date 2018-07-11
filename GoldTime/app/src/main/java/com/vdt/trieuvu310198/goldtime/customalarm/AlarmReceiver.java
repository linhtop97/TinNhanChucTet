package com.vdt.trieuvu310198.goldtime.customalarm;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.vdt.trieuvu310198.goldtime.data.DataAlarm;
import com.vdt.trieuvu310198.goldtime.data.DataIDAlarm;
import com.vdt.trieuvu310198.goldtime.model.DataAlarmModel;
import com.vdt.trieuvu310198.goldtime.util.Constrain;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {
    private List<DataAlarmModel> listAlarm;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, RingtoreService.class);
        //Log.e("dachuyen", "ok");
        myIntent.putExtra(Constrain.PROGRESS_SEEKBAR, intent.getIntExtra(Constrain.PROGRESS_SEEKBAR, 0));
        myIntent.putExtra(Constrain.POSITION_MUSIC, intent.getIntExtra(Constrain.POSITION_MUSIC, 0));
        myIntent.putExtra(Constrain.POSITION_TIMEBACK, intent.getIntExtra(Constrain.POSITION_TIMEBACK, 1));
        myIntent.putExtra(Constrain.POSITION_TIMEON, intent.getIntExtra(Constrain.POSITION_TIMEON, 2));
        myIntent.putExtra(Constrain.NOTE, intent.getStringExtra(Constrain.NOTE));
        Log.e("EEE", intent.getBooleanExtra(Constrain.VIBRATE, true) + "");
        myIntent.putExtra(Constrain.VIBRATE, intent.getBooleanExtra(Constrain.VIBRATE, true));
        Log.e("EEE", intent.getBooleanExtra(Constrain.VIBRATE, true) + "");
        int key = intent.getIntExtra("KEY", -1);
        Random random = new Random();
        if (key == 1) {
            long timeWeek = intent.getLongExtra("TIMEWEEK", -1);
            int id = intent.getIntExtra("IDALARMWEEK", -1);
            if ((System.currentTimeMillis() - timeWeek) > 60000) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                Log.e("CANCEL", "ok1");
                long interval = System.currentTimeMillis() - timeWeek;
                int countWeek = (int) ((interval / (24 * 7 * 3600000)) + 1);
                int idNew = random.nextInt(1000000000);
                DataIDAlarm dataIDAlarm = new DataIDAlarm();
                dataIDAlarm.addIDAlarm(context, idNew);
                intent.putExtra("TIMEWEEK", timeWeek + countWeek * (24 * 7 * 3600000));
                PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, idNew, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeWeek + countWeek * (24 * 7 * 3600000),
                        pendingIntent1);
            } else if ((Calendar.getInstance().getTimeInMillis() - timeWeek) <= 60000) {
                context.startService(myIntent);
            }
        } else {
            long timeDay = intent.getLongExtra("TIMEDAY", -1);
            int id = intent.getIntExtra("IDALARM", -1);
            if ((Calendar.getInstance().getTimeInMillis() - timeDay) > 60000) {
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id,
//                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                alarmManager.cancel(pendingIntent);
                Log.e("CANCEL", "ok");
                long interval = Calendar.getInstance().getTimeInMillis() - timeDay;
                int countDay = (int) ((interval / 86400000) + 1);
                Log.e("COUNT", countDay + ":" + interval);
                int idNew = random.nextInt(1000000000);
                DataIDAlarm dataIDAlarm = new DataIDAlarm();
                dataIDAlarm.addIDAlarm(context, idNew);
                intent.putExtra("TIMEDAY", timeDay + countDay * 86400000);
                PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, idNew, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeDay + countDay * 86400000, pendingIntent1);
            } else if ((Calendar.getInstance().getTimeInMillis() - timeDay) <= 60000){
                Log.e("SERVICE", "ok");
                context.startService(myIntent);
            }
        }


        if (key == 1) {

            long timeWeek = intent.getLongExtra("TIMEWEEK", -1);
            long intervaltime = (System.currentTimeMillis() + 3600000 * 24 * 7) - timeWeek;
            long timeCurrent = (System.currentTimeMillis() + 3600000 * 24 * 7) - (intervaltime % (3600000 * 24 * 7));
            Log.e("FFFFF", "da vao");

            int id = random.nextInt(1000000000);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            DataIDAlarm dataIDAlarm = new DataIDAlarm();
            dataIDAlarm.addIDAlarm(context, id);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeCurrent, pendingIntent);
            Log.e("TIMECU", "" + (timeCurrent - timeWeek));
            Log.e("TIMEEE", ((System.currentTimeMillis() + 3600000 * 24 * 7) - timeWeek) + "");
            Log.e("TIMEEE", timeWeek + "");
            Log.e("TIMEEE", System.currentTimeMillis() + "");

        } else {
            int positionAlarm = intent.getIntExtra("POSITIONALARM", -1);
            DataAlarm dataAlarm = new DataAlarm();
            listAlarm = dataAlarm.loadDataAlarm(context);
            if (listAlarm != null) {
                listAlarm.get(positionAlarm).setStatus(false);
            }
            dataAlarm.saveDataAlarm(context, listAlarm);
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
