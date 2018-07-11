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
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import com.vdt.trieuvu310198.goldtime.MainActivity;
import com.vdt.trieuvu310198.goldtime.R;
import com.vdt.trieuvu310198.goldtime.adapter.RVPromptadapter;
import com.vdt.trieuvu310198.goldtime.customprompt.CustomPromptActivity;
import com.vdt.trieuvu310198.goldtime.customprompt.PromptReceiver;
import com.vdt.trieuvu310198.goldtime.customprompt.PromptService;
import com.vdt.trieuvu310198.goldtime.data.DataAlarm;
import com.vdt.trieuvu310198.goldtime.data.DataPrompt;
import com.vdt.trieuvu310198.goldtime.model.PromptModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.CONNECTIVITY_SERVICE;

public class NhacNhoFragmnet extends android.support.v4.app.Fragment implements View.OnClickListener {
    private FloatingActionButton fabAddPrompt;
    private Context context;
    private RVPromptadapter adapter;
    private RecyclerView rvPrompt;
    private List<PromptModel> listPrompt;
    private List<PromptModel> listDataPrompt;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private IntentFilter filter;
    private BroadcastReceiver receiver;

    private Notification notification;
    private RemoteViews remoteViews;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder builder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nhac_nho, container, false);
        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        inIt(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        xuLi();
        confimHenGio();
    }

    private void confimHenGio() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case "UPDATEPROMPT":
                        Log.e("CONFIM", "ok");
                        henGio();
                        break;
                    default:
                        break;
                }
            }
        };
        filter = new IntentFilter();
        filter.addAction("UPDATEPROMPT");
        context.registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            context.unregisterReceiver(receiver);
        } catch (Exception e) {

        }
    }

    private void inIt(View view) {
        fabAddPrompt = view.findViewById(R.id.btn_fab_add_prompt);
        rvPrompt = view.findViewById(R.id.rv_prompt);

        fabAddPrompt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fab_add_prompt:
                Intent intent = new Intent(context, CustomPromptActivity.class);
                context.startActivity(intent);
                break;
        }
    }
    private void xuLi() {
        listPrompt = new ArrayList<>();
        readData();
        Log.e("SIZEEEE", listPrompt.size() + "");
        RVPromptadapter.OnItemClickListener onItemClickListener = new RVPromptadapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, CustomPromptActivity.class);
                intent.putExtra("POSITION", position);
                context.startActivity(intent);
            }
        };

        RVPromptadapter.OnCLickListener onCLickListener = new RVPromptadapter.OnCLickListener() {
            @Override
            public void onClick(View view, final int position) {
                RVPromptadapter.ViewHolder holder = new RVPromptadapter.ViewHolder(view);
                PopupMenu popupMenu = new PopupMenu(context, holder.icPopupMenu);
                popupMenu.getMenuInflater().inflate(R.menu.menu_rv_alarm, popupMenu.getMenu());
                // bat su kien
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_edit:
                                Intent intent = new Intent(context, CustomPromptActivity.class);
                                intent.putExtra("POSITION", position);
                                context.startActivity(intent);
                                break;
                            case R.id.item_delete:
                                DataPrompt dataPrompt = new DataPrompt();
                                listPrompt.remove(position);
                                Log.e("SIZEEEE", listPrompt.size() + "");
                                adapter.notifyDataSetChanged();
                                dataPrompt.removeDataPrompt(context, position);
                                henGio();
                                mNotificationManager.cancel(100+position);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        };
        adapter = new RVPromptadapter(listPrompt, context, onItemClickListener, onCLickListener);
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvPrompt.setAdapter(adapter);
        rvPrompt.setLayoutManager(manager);

    }

    private void readData() {
        DataPrompt dataPrompt = new DataPrompt();
        if (dataPrompt.loadDataPrompt(context) != null) {
            listPrompt = dataPrompt.loadDataPrompt(context);
        }
    }

    private void henGio() {
        listDataPrompt = new ArrayList<>();
        DataPrompt dataPrompt = new DataPrompt();
        listDataPrompt = dataPrompt.loadDataPrompt(context);
        calendar = Calendar.getInstance();
        if (listDataPrompt != null) {
            for(int i = 0; i < listDataPrompt.size(); i++) {
                PromptModel promptModel = listDataPrompt.get(i);
                calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(promptModel.getCalendar().substring(6, 10)));
                Log.e("TIME", Integer.parseInt(promptModel.getCalendar().substring(6, 10)) + "");
                calendar.set(Calendar.MONTH, Integer.parseInt(promptModel.getCalendar().substring(3, 5)) - 1);
                Log.e("TIME", Integer.parseInt(promptModel.getCalendar().substring(3, 5)) + "");
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(promptModel.getCalendar().substring(0,2)));
                Log.e("TIME", Integer.parseInt(promptModel.getCalendar().substring(0, 2)) + "");
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(promptModel.getTime().substring(0, 2)));
                Log.e("TIME", Integer.parseInt(promptModel.getTime().substring(0, 2)) + "");
                calendar.set(Calendar.MINUTE, Integer.parseInt(promptModel.getTime().substring(3, 5)));
                Log.e("TIME", Integer.parseInt(promptModel.getTime().substring(3, 5)) + "");
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Intent intent = new Intent(context, PromptReceiver.class);
                intent.putExtra("RINGTORE", promptModel.getRingTore());
                intent.putExtra("NOTEPROMPT", promptModel.getNote());
                intent.putExtra("SIZE", listPrompt.size());
                intent.putExtra("TIME", calendar.getTimeInMillis());
                intent.putExtra("POSITION", i);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Log.e("TIME", "" + (calendar.getTimeInMillis() - System.currentTimeMillis()));
                if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    String time = "ngày " + promptModel.getCalendar() + " lúc " + promptModel.getTime();
                    setnotification(100+i, promptModel.getNote(), time);
                }
            }
        }
    }
    private void setnotification(int id, String title, String time) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "channel-01";
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_timer);
        remoteViews.setImageViewResource(R.id.img_noti_icon, R.drawable.ic_clock);
        remoteViews.setTextViewText(R.id.tv_title_noti, title);
        remoteViews.setTextViewText(R.id.tv_noti_timer, time);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("TABLAYOUT", "PROMPT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder = new NotificationCompat.Builder(context);
        notification = builder.setSmallIcon(R.drawable.ic_clock)
                .setCustomContentView(remoteViews)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setChannelId(channelId)
                .setOngoing(true)
                .build();
        //mNotificationManager.notify(0, notification);
        //notification.flags |= Notification.FLAG_AUTO_CANCEL;

        String channelName ="Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        mNotificationManager.notify(id, notification);
    }
}
