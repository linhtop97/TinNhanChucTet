package com.vdt.trieuvu310198.goldtime.fragment;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.vdt.trieuvu310198.goldtime.R;
import com.vdt.trieuvu310198.goldtime.adapter.LVDialogMusicAdapter;
import com.vdt.trieuvu310198.goldtime.customview.NumberPickerCustomHour;
import com.vdt.trieuvu310198.goldtime.customview.NumberPickerCustomMinute;
import com.vdt.trieuvu310198.goldtime.customview.NumberPickerCustomSecond;
import com.vdt.trieuvu310198.goldtime.model.ModelLVDiaLogAmBao;
import com.vdt.trieuvu310198.goldtime.customtimer.ServiceTimer;
import com.vdt.trieuvu310198.goldtime.util.Constrain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimerFragment extends Fragment {

    private Context context;
    private Calendar myCalendar;

    private int hour;
    private int minute;
    private int second;
    private int positionMusic;

    private String time;

    private TextView tvRingtore;

    private Intent intent;

    private NumberPickerCustomHour numberPickerCustomHour;
    private NumberPickerCustomMinute numberPickerCustomMinute;
    private NumberPickerCustomSecond numberPickerCustomSecond;

    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;

    private Button btStart;
    private Button btHuy;

    private TextView tvSetTime;
    private LinearLayout llSetTime;
    private RelativeLayout rlRingtore;
    private ListView lvDialogMusic;
    private List<ModelLVDiaLogAmBao> listModelDialogMusic;
    private LVDialogMusicAdapter adapterLVDialogMusic;

    private Boolean checkStart;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dem_nguoc, container, false);

        inIt(view);
        readData();
        xuLi();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        receiverTime();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onStop() {
        super.onStop();
        savaData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(receiver);
    }

    private void inIt(View view) {
        tvSetTime = view.findViewById(R.id.tv_set_time);
        llSetTime = view.findViewById(R.id.ll_set_time);
        btStart = view.findViewById(R.id.bt_start_timer);
        btHuy = view.findViewById(R.id.bt_huy_timer);
        rlRingtore = view.findViewById(R.id.rl_ringtore);
        tvRingtore = view.findViewById(R.id.tv_ringtore);
        numberPickerCustomHour = view.findViewById(R.id.time_hour);
        numberPickerCustomMinute = view.findViewById(R.id.time_minute);
        numberPickerCustomSecond = view.findViewById(R.id.time_second);

        rlRingtore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialogMusic = new Dialog(context);
                dialogMusic.setContentView(R.layout.lv_dialog_am_bao);
                lvDialogMusic = dialogMusic.findViewById(R.id.lv_dialog_music);

                //dialog.setCancelable(true);
                dialogMusic.setCanceledOnTouchOutside(true);

                //bắt sự kiên 2 bt hủy và ok
                dialogMusic.findViewById(R.id.bt_ok_dialog_music).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i = 0; i < listModelDialogMusic.size(); i++) {
                            if (listModelDialogMusic.get(i).isIscheckedMusic()) {
                                tvRingtore.setText(listModelDialogMusic.get(i).getNameMusic());
                                positionMusic = i;
                                break;
                            }
                        }
                        dialogMusic.dismiss();
                    }
                });
                dialogMusic.findViewById(R.id.bt_huy_dialog_music).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogMusic.dismiss();
                    }
                });

                // set click cho listview

                LVDialogMusicAdapter.OnDialogListClickListener onDialogListClickListener = new LVDialogMusicAdapter.OnDialogListClickListener() {

                    @Override
                    public void onItemClick(int position) {
                        //bắt sự kiện click thì thay đổi radio button
                        for (int i = 0; i < listModelDialogMusic.size(); i++) {
                            if (listModelDialogMusic.get(i).isIscheckedMusic() == true) {
                                listModelDialogMusic.get(i).setIscheckedMusic(false);
                                break;
                            }
                        }
                        listModelDialogMusic.get(position).setIscheckedMusic(true);
                        adapterLVDialogMusic.notifyDataSetChanged();
                    }
                };
                adapterLVDialogMusic = new LVDialogMusicAdapter(context, R.layout.item_listview_am_bao, listModelDialogMusic,
                        onDialogListClickListener);
                lvDialogMusic.setAdapter(adapterLVDialogMusic);
                dialogMusic.show();
            }
        });


        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTime();
            }
        });
        btHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("BTHUY");
                context.sendBroadcast(intent);
                if (isMyServiceRunning(ServiceTimer.class) == true) {
                    context.stopService(new Intent(context, ServiceTimer.class));
                }
                rlRingtore.setEnabled(true);
                numberPickerCustomHour.setValue(0);
                numberPickerCustomMinute.setValue(0);
                numberPickerCustomSecond.setValue(0);
                tvSetTime.setVisibility(View.GONE);
                llSetTime.setVisibility(view.VISIBLE);
                btHuy.setEnabled(false);
                btStart.setEnabled(true);
                Log.e("CHECK", "" + isMyServiceRunning(ServiceTimer.class));
            }
        });

    }

    private void xuLi() {
        if (!isMyServiceRunning(ServiceTimer.class)) {
            llSetTime.setVisibility(View.VISIBLE);
            tvSetTime.setVisibility(View.GONE);
        } else {
            llSetTime.setVisibility(View.GONE);
            tvSetTime.setVisibility(View.VISIBLE);
        }




    }

    private void sendTime() {
        hour = numberPickerCustomHour.getValue();
        minute = numberPickerCustomMinute.getValue();
        second = numberPickerCustomSecond.getValue();
        if (hour != 0 || minute != 0 || second != 0) {
            tvSetTime.setVisibility(View.VISIBLE);
            llSetTime.setVisibility(View.GONE);
            intent = new Intent(context, ServiceTimer.class);
            intent.putExtra("HOUR", hour);
            intent.putExtra("MINUTE", minute);
            intent.putExtra("SECOND", second);
            intent.putExtra(Constrain.POSITION_MUSIC, positionMusic);
            context.startService(intent);
            rlRingtore.setEnabled(false);
            checkStart = false;
            btStart.setEnabled(false);
            btHuy.setEnabled(true);
        }
        else {
            Toast.makeText(context, "mời chọn giờ", Toast.LENGTH_SHORT).show();
        }


    }

    private void receiverTime() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case "DATA":
                        long timemillis = intent.getLongExtra("TIMILLIS", -1);
                        Log.e("QQQQ", "" + numberPickerCustomHour.getValue() + "aaaa");
                        int hourReceiver = (int) (timemillis / 3600);
                        int minuteReceiver = (int) ((timemillis % 3600) / 60);
                        int secondReceiver = (int) ((timemillis % 60));
                        time = String.format("%02d", hourReceiver) + ":" + String.format("%02d", minuteReceiver)
                                + ":" + String.format("%02d", secondReceiver);
                        tvSetTime.setText(time);

                        if(timemillis == 0) {

                            btHuy.setEnabled(false);
                            rlRingtore.setEnabled(true);
                            llSetTime.setVisibility(View.VISIBLE);
                            tvSetTime.setVisibility(View.GONE);
                            numberPickerCustomHour.setValue(0);
                            numberPickerCustomMinute.setValue(0);
                            numberPickerCustomSecond.setValue(0);
                            btStart.setEnabled(true);

                        }
                        break;
                    default:
                        break;
                }
            }
        };
        intentFilter = new IntentFilter();
        intentFilter.addAction("DATA");
        context.registerReceiver(receiver, intentFilter);

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;

    }

    private void savaData() {
        SharedPreferences settings = context.getSharedPreferences("DATA_TIMER", context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putInt("POSITIONMUSIC", positionMusic);
        Log.e("POS", positionMusic + "");
        editor.commit();
    }

    private void readData() {
        listModelDialogMusic = new ArrayList<>();
        dataMusic();
        SharedPreferences settings = context.getSharedPreferences("DATA_TIMER", context.MODE_PRIVATE);
        positionMusic = settings.getInt("POSITIONMUSIC", 0);
        listModelDialogMusic.get(positionMusic).setIscheckedMusic(true);
        tvRingtore.setText(listModelDialogMusic.get(positionMusic).getNameMusic());

        Log.e("pos", "" + positionMusic);
        if (isMyServiceRunning(ServiceTimer.class) == true) {
            btStart.setEnabled(false);
            btHuy.setEnabled(true);
        }
        else {
            btStart.setEnabled(true);
            btHuy.setEnabled(false);
        }
    }

    private void dataMusic() {
        listModelDialogMusic.add(new ModelLVDiaLogAmBao("music 1 (mặc định)", false));
        listModelDialogMusic.add(new ModelLVDiaLogAmBao("music 2", false));
        listModelDialogMusic.add(new ModelLVDiaLogAmBao("music 3", false));
        listModelDialogMusic.add(new ModelLVDiaLogAmBao("music 4", false));
        listModelDialogMusic.add(new ModelLVDiaLogAmBao("sound 1", false));
        listModelDialogMusic.add(new ModelLVDiaLogAmBao("sound 2", false));
        listModelDialogMusic.add(new ModelLVDiaLogAmBao("bell 1", false));
        listModelDialogMusic.add(new ModelLVDiaLogAmBao("bell 2", false));
        listModelDialogMusic.add(new ModelLVDiaLogAmBao("bell 3", false));
        listModelDialogMusic.add(new ModelLVDiaLogAmBao("bell 4", false));
        listModelDialogMusic.add(new ModelLVDiaLogAmBao("harp 1", false));
        listModelDialogMusic.add(new ModelLVDiaLogAmBao("harp 2", false));
        listModelDialogMusic.add(new ModelLVDiaLogAmBao("alert", false));
        listModelDialogMusic.add(new ModelLVDiaLogAmBao("xmas", false));

    }

}
