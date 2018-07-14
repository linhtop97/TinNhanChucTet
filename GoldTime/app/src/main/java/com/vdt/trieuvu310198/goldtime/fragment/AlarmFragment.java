package com.vdt.trieuvu310198.goldtime.fragment;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.vdt.trieuvu310198.goldtime.MainActivity;
import com.vdt.trieuvu310198.goldtime.R;
import com.vdt.trieuvu310198.goldtime.adapter.RVAlarmAdapter;
import com.vdt.trieuvu310198.goldtime.customalarm.AlarmReceiver;
import com.vdt.trieuvu310198.goldtime.customalarm.CustomAlarmActivity;
import com.vdt.trieuvu310198.goldtime.customalarm.RingtoreService;
import com.vdt.trieuvu310198.goldtime.data.DataAlarm;
import com.vdt.trieuvu310198.goldtime.data.DataIDAlarm;
import com.vdt.trieuvu310198.goldtime.model.AlarmRV;
import com.vdt.trieuvu310198.goldtime.model.DataAlarmModel;
import com.vdt.trieuvu310198.goldtime.util.Constrain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;

public class AlarmFragment extends Fragment implements View.OnClickListener {

    private RVAlarmAdapter rvAlarmAdapter;

    private List<AlarmRV> listAlarmRV;
    private List<DataAlarmModel> listDataAlarmModel;
    private List<Integer> listDataID;

    private RecyclerView recyclerView;
    private FloatingActionButton btFABAdd;

    private BroadcastReceiver receiver;
    private IntentFilter filter;
    private Context context;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private Random random;

    private Notification notification;
    private RemoteViews remoteViews;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder builder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("EEE", "oncreate");
        View view = inflater.inflate(R.layout.fragment_hen_gio, container, false);
        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        permisonAndroid();
        inIt(view);
        confirmHenGio();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        xuLi();
        //henGio();
    }

    private void confirmHenGio() {
        receiver = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case "UPDATEALARM":
                        cancelAlarmManager();
                        henGio();
                        break;
                    default:
                        break;
                }
            }
        };
        filter = new IntentFilter();
        filter.addAction("UPDATEALARM");
        context.registerReceiver(receiver, filter);
    }

    private void permisonAndroid() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + context.getPackageName())), 12);
            }
        }
    }

    @Override
    public void onDestroy() {
        try {
            context.unregisterReceiver(receiver);
        } catch (Exception e) {

        }
        super.onDestroy();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void inIt(View view) {
        recyclerView = view.findViewById(R.id.rv_alarm);
        btFABAdd = view.findViewById(R.id.btn_fab_add);
        btFABAdd.setOnClickListener(this);

//        receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                switch (intent.getAction()) {
//
//                    case "update_alarm":
//                        int positonRV = intent.getIntExtra("POSITIONRV", -1);
//                        if (positonRV != -1) {
//                            AlarmRV alarmRV1 = (AlarmRV) intent.getExtras().getSerializable("ALARMRV");
//                            listAlarmRV.get(positonRV).setHour(alarmRV1.getHour());
//                            listAlarmRV.get(positonRV).setMinute(alarmRV1.getMinute());
//                            listAlarmRV.get(positonRV).setDay(alarmRV1.getDay());
//                            listAlarmRV.get(positonRV).setAlarmStatus(alarmRV1.getAlarmStatus());
//                            rvAlarmAdapter.notifyDataSetChanged();
//                        } else {
//                            AlarmRV alarmRV = (AlarmRV) intent.getExtras().getSerializable("ALARMRV");
//                            listAlarmRV.add(alarmRV);
//                            rvAlarmAdapter.notifyDataSetChanged();
//                        }
//
//                        break;
//
//                    default:
//                        break;
//                }
//            }
//        };
//        intentFilter =new IntentFilter();
//        intentFilter.addAction("update_alarm");
//        context.registerReceiver(receiver,intentFilter);
    }


    private void xuLi() {
        listAlarmRV = new ArrayList<>();
        listDataAlarmModel = new ArrayList<>();
        listDataID = new ArrayList<>();
        readDataAlarm();
        RVAlarmAdapter.OnCLickListener onCLickListener = new RVAlarmAdapter.OnCLickListener() {
            @Override
            public void onClick(View view, final int position) {
                RVAlarmAdapter.ViewHolder holder = new RVAlarmAdapter.ViewHolder(view);
                PopupMenu popupMenu = new PopupMenu(context, holder.icPopupMenu);
                popupMenu.getMenuInflater().inflate(R.menu.menu_rv_alarm, popupMenu.getMenu());
                // bat su kien
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_edit:
                                Intent intent = new Intent(context, CustomAlarmActivity.class);
                                intent.putExtra("POSITION", position);
                                context.startActivity(intent);
                                break;
                            case R.id.item_delete:
                                DataAlarm dataAlarm = new DataAlarm();
                                listAlarmRV.remove(position);
                                listDataAlarmModel.remove(position);
                                rvAlarmAdapter.notifyDataSetChanged();
                                dataAlarm.removeDataAlarm(context, position);
                                cancelAlarmManager();
                                henGio();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        };
        RVAlarmAdapter.OnCheckedChangedListener onCheckedChangedListener = new RVAlarmAdapter.OnCheckedChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChecked(CompoundButton compoundButton, boolean b, int position) {
                if (b == true) {
                    listDataAlarmModel.get(position).setStatus(true);
                    DataAlarm dataAlarm = new DataAlarm();
                    dataAlarm.saveDataAlarm(context, listDataAlarmModel);
                    Toast.makeText(context, "đã bật", Toast.LENGTH_SHORT).show();
                    Log.e("4444", "ok");
                    cancelAlarmManager();
                    henGio();
                    //henGio();
                } else {
                    listDataAlarmModel.get(position).setStatus(false);
                    Toast.makeText(context, "đã tắt", Toast.LENGTH_SHORT).show();
                    DataAlarm dataAlarm = new DataAlarm();
                    dataAlarm.saveDataAlarm(context, listDataAlarmModel);
                    cancelAlarmManager();
                    henGio();
                }

            }
        };


        RVAlarmAdapter.OnItemClickListener onItemClickListener = new RVAlarmAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, CustomAlarmActivity.class);
                intent.putExtra("POSITION", position);
                context.stopService(new Intent(context, RingtoreService.class));
                context.startActivity(intent);
            }
        };

        rvAlarmAdapter = new RVAlarmAdapter(listAlarmRV, context, onItemClickListener, onCheckedChangedListener,
                onCLickListener);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(rvAlarmAdapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fab_add:
                Intent intent = new Intent(context, CustomAlarmActivity.class);
                context.stopService(new Intent(context, RingtoreService.class));
                context.startActivity(intent);
                break;
        }
    }

    public void readDataAlarm() {
        calendar = Calendar.getInstance();
        DataAlarm dataAlarm = new DataAlarm();
        listDataAlarmModel = dataAlarm.loadDataAlarm(context);
        if (listDataAlarmModel != null) {
            for (int i = 0; i < listDataAlarmModel.size(); i++) {
                calendar.set(Calendar.HOUR_OF_DAY, listDataAlarmModel.get(i).getHour());
                calendar.set(Calendar.MINUTE, listDataAlarmModel.get(i).getMinute());
                Log.e("AAA", "" + listDataAlarmModel.get(i).getHour() + " " + listDataAlarmModel.get(i).getMinute());
                String strDay = "";
                if (!listDataAlarmModel.get(i).isMo() && !listDataAlarmModel.get(i).isTu() && !listDataAlarmModel.get(i).isWe()
                        && !listDataAlarmModel.get(i).isTh() && !listDataAlarmModel.get(i).isFr() && !listDataAlarmModel.get(i).isSa()
                        && !listDataAlarmModel.get(i).isSu() && listDataAlarmModel.get(i).isStatus()) {
                    strDay = " Báo thức";
                }
                if (listDataAlarmModel.get(i).isMo()) {
                    strDay = strDay + " MO";
                }

                if (listDataAlarmModel.get(i).isTu()) {
                    strDay = strDay + " TU";
                }

                if (listDataAlarmModel.get(i).isWe()) {
                    strDay = strDay + " WE";
                }

                if (listDataAlarmModel.get(i).isTh()) {
                    strDay = strDay + " TH";
                }

                if (listDataAlarmModel.get(i).isFr()) {
                    strDay = strDay + " FR";
                }

                if (listDataAlarmModel.get(i).isSa()) {
                    strDay = strDay + " SA";
                }

                if (listDataAlarmModel.get(i).isSu()) {
                    strDay = strDay + " SU";
                }
                listAlarmRV.add(new AlarmRV(String.format("%02d", listDataAlarmModel.get(i).getHour()),
                        String.format("%02d", listDataAlarmModel.get(i).getMinute()),
                        strDay, listDataAlarmModel.get(i).isStatus(), R.drawable.icon_popup_menu));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void henGio() {
        listDataAlarmModel = new ArrayList<>();
        //doc lại listData
        DataAlarm dataAlarm = new DataAlarm();
        listDataAlarmModel = dataAlarm.loadDataAlarm(context);
        DataIDAlarm dataIDAlarm = new DataIDAlarm();
        Log.e("VVVV", "da vao");
        random = new Random();
        long startTime;
        Log.e("ABC", "" + listDataAlarmModel.size());
        if (listDataAlarmModel != null) {
            for (int i = 0; i < listDataAlarmModel.size(); i++) {

                if (listDataAlarmModel.get(i).isStatus()) {
                    int d = -1;
                    int[] dayOfWeek = new int[100];
                    Calendar calendar2 = Calendar.getInstance();

                    if (listDataAlarmModel.get(i).isMo()) {
                        dayOfWeek[++d] = 2;
                    }
                    if (listDataAlarmModel.get(i).isTu()) {
                        dayOfWeek[++d] = 3;
                    }
                    if (listDataAlarmModel.get(i).isWe()) {
                        dayOfWeek[++d] = 4;
                    }
                    if (listDataAlarmModel.get(i).isTh()) {
                        dayOfWeek[++d] = 5;
                    }
                    if (listDataAlarmModel.get(i).isFr()) {
                        dayOfWeek[++d] = 6;
                    }
                    if (listDataAlarmModel.get(i).isSa()) {
                        dayOfWeek[++d] = 7;
                    }
                    if (listDataAlarmModel.get(i).isSu()) {
                        dayOfWeek[++d] = 1;
                    }

                    if (d != -1) {

                        for (int k = 0; k <= d; k++) {
                            calendar2.set(Calendar.DAY_OF_WEEK, dayOfWeek[k]);
                            calendar2.set(Calendar.HOUR_OF_DAY, listDataAlarmModel.get(i).getHour());
                            calendar2.set(Calendar.MINUTE, listDataAlarmModel.get(i).getMinute());
                            calendar2.set(Calendar.SECOND, 0);
                            calendar2.set(Calendar.MILLISECOND, 0);
                            Intent intent = new Intent(context, AlarmReceiver.class);
                            Log.e("ABC", "" + ((calendar2.getTimeInMillis() - (System.currentTimeMillis()))));
                            intent.putExtra("KEY", 1);
                            intent.putExtra(Constrain.PROGRESS_SEEKBAR, listDataAlarmModel.get(i).getVolume());
                            intent.putExtra(Constrain.POSITION_MUSIC, listDataAlarmModel.get(i).getPositonMusic());
                            intent.putExtra(Constrain.POSITION_TIMEBACK, listDataAlarmModel.get(i).getPositionTimeBack());
                            intent.putExtra(Constrain.POSITION_TIMEON, listDataAlarmModel.get(i).getPositionTimeOn());
                            intent.putExtra(Constrain.NOTE, listDataAlarmModel.get(i).getNote());
                            intent.putExtra(Constrain.VIBRATE, listDataAlarmModel.get(i).isVibrate());
                            if (calendar2.getTimeInMillis() <= System.currentTimeMillis()) {
                                startTime = calendar2.getTimeInMillis() + 3600000 * 24 * 7;
                            } else {
                                Log.e("CCCCC", "chạy vô đây");
                                startTime = calendar2.getTimeInMillis();
                                Log.e("CCCCCCC", "" + (startTime - System.currentTimeMillis()));
                            }
                            intent.putExtra("TIMEWEEK", startTime);
                            int id = random.nextInt(1000000000);
                            intent.putExtra("IDALARMWEEK", id);
                            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, id,
                                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            listDataID.add(id);
                            dataIDAlarm.addIDAlarm(context, id);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, startTime, pendingIntent1);
                        }
                    } else {
                        calendar2.set(Calendar.HOUR_OF_DAY, listDataAlarmModel.get(i).getHour());
                        calendar2.set(Calendar.MINUTE, listDataAlarmModel.get(i).getMinute());
                        calendar2.set(Calendar.SECOND, 0);
                        calendar2.set(Calendar.MILLISECOND, 0);
                        Intent intent = new Intent(context, AlarmReceiver.class);
                        Log.e("AAA", "" + ((calendar2.getTimeInMillis() - (System.currentTimeMillis()))));
                        intent.putExtra(Constrain.PROGRESS_SEEKBAR, listDataAlarmModel.get(i).getVolume());
                        intent.putExtra(Constrain.POSITION_MUSIC, listDataAlarmModel.get(i).getPositonMusic());
                        intent.putExtra(Constrain.POSITION_TIMEBACK, listDataAlarmModel.get(i).getPositionTimeBack());
                        intent.putExtra(Constrain.POSITION_TIMEON, listDataAlarmModel.get(i).getPositionTimeOn());
                        intent.putExtra(Constrain.NOTE, listDataAlarmModel.get(i).getNote());
                        intent.putExtra(Constrain.VIBRATE, listDataAlarmModel.get(i).isVibrate());

                        Log.e("EEE", listDataAlarmModel.get(i).isVibrate() + "dasd");
                        if (calendar2.getTimeInMillis() <= System.currentTimeMillis()) {
                            startTime = calendar2.getTimeInMillis() + 3600000 * 24;
                        } else {
                            startTime = calendar2.getTimeInMillis();
                        }
                        intent.putExtra("TIMEDAY", startTime );
                        int id2 = random.nextInt(1000000000);
                        intent.putExtra("IDALARM", id2);
                        intent.putExtra("POSITIONALARM", i);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id2,
                                intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        listDataID.add(id2);
                        dataIDAlarm.addIDAlarm(context, id2);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, startTime, pendingIntent);
                    }
                }
            }
        }
    }

    private void cancelAlarmManager() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        DataIDAlarm dataIDAlarm = new DataIDAlarm();
        if (dataIDAlarm.loadDataIDAlarm(context) != null) {
            listDataID = dataIDAlarm.loadDataIDAlarm(context);
            for (int i = 0; i < listDataID.size(); i++) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, listDataID.get(i).intValue()
                        , intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
            }
            listDataID.clear();
            dataIDAlarm.clearDataIDAlarm(context);
            Log.e("Size", "" + listDataID.size());
        }

    }

}
