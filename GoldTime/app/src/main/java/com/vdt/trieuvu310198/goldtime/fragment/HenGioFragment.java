package com.vdt.trieuvu310198.goldtime.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.vdt.trieuvu310198.goldtime.R;
import com.vdt.trieuvu310198.goldtime.adapter.RVAlarmAdapter;
import com.vdt.trieuvu310198.goldtime.customalarm.AlarmReceiver;
import com.vdt.trieuvu310198.goldtime.customalarm.CustomAlarmActivity;
import com.vdt.trieuvu310198.goldtime.data.DataAlarm;
import com.vdt.trieuvu310198.goldtime.model.AlarmRV;
import com.vdt.trieuvu310198.goldtime.model.DataAlarmModel;
import com.vdt.trieuvu310198.goldtime.util.Constrain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

public class HenGioFragment extends Fragment implements View.OnClickListener {

    private RVAlarmAdapter rvAlarmAdapter;

    private List<AlarmRV> listAlarmRV;
    private List<DataAlarmModel> listDataAlarmModel;

    private RecyclerView recyclerView;
    private FloatingActionButton btFABAdd;

    //    private BroadcastReceiver receiver;
//    private IntentFilter intentFilter;
    private Context context;
    private Calendar calendar;
    private AlarmManager alarmManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("EEE", "oncreate");
        View view = inflater.inflate(R.layout.fragment_hen_gio, container, false);
        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        inIt(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        xuLi();
        henGio();
    }

//    @Override
//    public void onDestroy() {
//        try {
//            context.unregisterReceiver(receiver);
//        } catch (Exception e) {
//
//        }
//        super.onDestroy();
//
//    }


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
        readDataAlarm();
        RVAlarmAdapter.OnCLickListener onCLickListener = new RVAlarmAdapter.OnCLickListener() {
            @Override
            public void onClick(View view, final int position) {
                RVAlarmAdapter.ViewHolder holder = new RVAlarmAdapter.ViewHolder(view);
                PopupMenu popupMenu = new PopupMenu(context, holder.icPopupMenu);
                popupMenu.getMenuInflater().inflate(R.menu.menu_rv_alarm, popupMenu.getMenu());
                // bat su kien
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
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
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        };
        RVAlarmAdapter.OnCheckedChangedListener onCheckedChangedListener = new RVAlarmAdapter.OnCheckedChangedListener() {
            @Override
            public void onChecked(CompoundButton compoundButton, boolean b, int position) {
                if (b == true) {
                    listDataAlarmModel.get(position).setStatus(true);
                    Toast.makeText(context, "đã bật", Toast.LENGTH_SHORT).show();
                    DataAlarm dataAlarm = new DataAlarm();
                    dataAlarm.saveDataAlarm(context, listDataAlarmModel);
                    henGio();
                } else {
                    listDataAlarmModel.get(position).setStatus(false);
                    Toast.makeText(context, "đã tắt", Toast.LENGTH_SHORT).show();
                    DataAlarm dataAlarm = new DataAlarm();
                    dataAlarm.saveDataAlarm(context, listDataAlarmModel);
                }

            }
        };
        RVAlarmAdapter.OnItemClickListener onItemClickListener = new RVAlarmAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, CustomAlarmActivity.class);
                intent.putExtra("POSITION", position);
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
                String strDay = "";
                if (!listDataAlarmModel.get(i).isMo() && !listDataAlarmModel.get(i).isTu() && !listDataAlarmModel.get(i).isWe()
                        && !listDataAlarmModel.get(i).isTh() && !listDataAlarmModel.get(i).isFr() && !listDataAlarmModel.get(i).isSa()
                        && !listDataAlarmModel.get(i).isSu() && calendar.getTimeInMillis() > System.currentTimeMillis()) {
                    strDay = " Hôm Nay";
                } else if (!listDataAlarmModel.get(i).isMo() && !listDataAlarmModel.get(i).isTu() && !listDataAlarmModel.get(i).isWe()
                        && !listDataAlarmModel.get(i).isTh() && !listDataAlarmModel.get(i).isFr() && !listDataAlarmModel.get(i).isSa()
                        && !listDataAlarmModel.get(i).isSu() && calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                    strDay = " Ngày Mai";
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
                listAlarmRV.add(new AlarmRV("" + listDataAlarmModel.get(i).getHour(), "" + listDataAlarmModel.get(i).getMinute(),
                        strDay, listDataAlarmModel.get(i).isStatus(), R.drawable.icon_popup_menu));
            }
        }
    }

    private void henGio() {
        int count = 0;
        long intervalTime;
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
                            Log.e("AAA", "" + ((calendar2.getTimeInMillis() - (System.currentTimeMillis()))));
                            intent.putExtra(Constrain.PROGRESS_SEEKBAR, listDataAlarmModel.get(i).getVolume());
                            intent.putExtra(Constrain.POSITION_MUSIC, listDataAlarmModel.get(i).getPositonMusic());
                            intent.putExtra(Constrain.POSITION_TIMEBACK, listDataAlarmModel.get(i).getPositionTimeBack());
                            intent.putExtra(Constrain.POSITION_TIMEON, listDataAlarmModel.get(i).getPositionTimeOn());

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ++count,
                                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

                            if (calendar2.getTimeInMillis() <= System.currentTimeMillis()) {
                                intervalTime = calendar2.getTimeInMillis() + 3600000 * 24 * 7;
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis() + 3600000 * 24 * 7
                                        , 3600000 * 24 * 7, pendingIntent);
                                //thời gian báo lại
                                for (int j = 0; j <= Integer.parseInt(listDataAlarmModel.get(i).getCountTimeBack()); j++) {
                                    PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 100 * j,
                                            intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    intervalTime = intervalTime + getTimeback(i) * 60000;
                                    alarmManager.set(AlarmManager.RTC_WAKEUP, intervalTime, pendingIntent2);

                                }
                            } else {
                                intervalTime = calendar2.getTimeInMillis();
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis()
                                        , 3600000 * 24 * 7, pendingIntent);
                                for (int j = 0; j <= Integer.parseInt(listDataAlarmModel.get(i).getCountTimeBack()); j++) {
                                    PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 100 * j,
                                            intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    intervalTime = intervalTime + getTimeback(i) * 60000;
                                    alarmManager.set(AlarmManager.RTC_WAKEUP, intervalTime, pendingIntent2);
                                }
                            }
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
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ++count,
                                intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        if (calendar2.getTimeInMillis() <= System.currentTimeMillis()) {
                            intervalTime = calendar2.getTimeInMillis() + 3600000 * 24;
                            alarmManager.set(AlarmManager.RTC_WAKEUP, intervalTime, pendingIntent);
                            //thời gian báo lại
                            for (int j = 0; j < Integer.parseInt(listDataAlarmModel.get(i).getCountTimeBack()); j++) {
                                PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 100 * j,
                                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                intervalTime = intervalTime + getTimeback(i) * 60000;
                                alarmManager.set(AlarmManager.RTC_WAKEUP, intervalTime, pendingIntent2);

                            }
                        } else {
                            intervalTime = calendar2.getTimeInMillis();
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis()
                                    , 3600000 * 24 * 7, pendingIntent);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, intervalTime, pendingIntent);
                            //thời gian báo lại
                            for (int j = 0; j < Integer.parseInt(listDataAlarmModel.get(i).getCountTimeBack()); j++) {
                                PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 100 * j,
                                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                intervalTime = intervalTime + getTimeback(i) * 60000;
                                alarmManager.set(AlarmManager.RTC_WAKEUP, intervalTime, pendingIntent2);
                            }
                        }
                    }
                }
            }
        }
    }

    private int getTimeback(int position) {
        switch (listDataAlarmModel.get(position).getPositionTimeBack()) {
            case 0:
                return 1;
            case 1:
                return 5;
            case 2:
                return 10;
            case 3:
                return 20;
            case 4:
                return 30;
            default:
                return 0;
        }
    }
}
