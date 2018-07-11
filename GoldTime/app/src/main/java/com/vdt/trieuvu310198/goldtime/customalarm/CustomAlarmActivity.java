package com.vdt.trieuvu310198.goldtime.customalarm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.vdt.trieuvu310198.goldtime.R;
import com.vdt.trieuvu310198.goldtime.adapter.LVDialogMusicAdapter;
import com.vdt.trieuvu310198.goldtime.adapter.LVDialogTimeBackAdapter;
import com.vdt.trieuvu310198.goldtime.adapter.LVDialogTimeOnMusicAdapter;
import com.vdt.trieuvu310198.goldtime.customview.TimePickerCustom;
import com.vdt.trieuvu310198.goldtime.data.DataAlarm;
import com.vdt.trieuvu310198.goldtime.model.DataAlarmModel;
import com.vdt.trieuvu310198.goldtime.model.ModelLVDiaLogAmBao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomAlarmActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TextView txtMusic;
    private TextView txtTimeBack;
    private TextView txtTimeOn;
    private TextView txtGhiChu;

    private LinearLayout llMusic;
    private LinearLayout llTimeBack;
    private LinearLayout llTimeOn;
    private LinearLayout llNote;

    private Button btHenGio;
    private Button btCancel;

    private ListView lvDialogMusic;
    private ListView lvDialogTimeBack;
    private ListView lvDialogTimeOn;

    //adapter
    private LVDialogTimeOnMusicAdapter adapterLVDialogTimeOn;
    private LVDialogTimeBackAdapter adapterLVDialogTimeBack;
    private LVDialogMusicAdapter adapterLVDialogMusic;

    //list
    private List<ModelLVDiaLogAmBao> listModelDialogMusic;
    private List<ModelLVDiaLogAmBao> listTimeBack;
    private List<ModelLVDiaLogAmBao> listTimeOn;
    private List<DataAlarmModel> listDataAlarmModel;


    private ToggleButton tbMO;
    private ToggleButton tbTU;
    private ToggleButton tbWE;
    private ToggleButton tbTH;
    private ToggleButton tbFR;
    private ToggleButton tbSA;
    private ToggleButton tbSU;

    private int positionMusic = 0;
    private int positionTimeBack = 2;
    private int positionTimeOn = 2;
    private int positionRV;

    private TimePickerCustom timePickerCustom;
    private SeekBar sbVolume;
    private MediaPlayer mediaPlayer;

    private EditText edGhiChu;
    private Switch swVibrate;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_arlarm);
        inIt();
        xuLi();
        customSeekbarVolume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    private void customSeekbarVolume() {
        mediaPlayer = MediaPlayer.create(CustomAlarmActivity.this, R.raw.music1);
        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mediaPlayer.setVolume(i * 0.01f, i * 0.01f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void xuLi() {
        listTimeOn = new ArrayList<>();
        listTimeBack = new ArrayList<>();
        listModelDialogMusic = new ArrayList<>();
        listDataAlarmModel = new ArrayList<>();
        readDataAlarm();

//        ReadDataTime();
//        ReadDataSeekbar();
//        ReadDataDay();
//        ReadDataMusic();
//        ReadDataTimeBack();
//        ReadDataCountTimeBack();
//        ReadDataTimeOn();
//        ReadDataGhiChu();
    }

    @SuppressLint("WrongViewCast")
    private void inIt() {
        btHenGio = findViewById(R.id.bt_luu);
        btCancel = findViewById(R.id.bt_cancel);
        timePickerCustom = findViewById(R.id.time_picker_custom);
        txtMusic = findViewById(R.id.txt_menu_nhac);
        txtTimeBack = findViewById(R.id.txt_time_bao_lai);
        txtTimeOn = findViewById(R.id.txt_time_phat_nhac);
        txtGhiChu = findViewById(R.id.txt_ghi_chu);
        sbVolume = findViewById(R.id.seekbar_volume);
        tbMO = findViewById(R.id.bt_mo);
        tbTU = findViewById(R.id.bt_tu);
        tbWE = findViewById(R.id.bt_we);
        tbTH = findViewById(R.id.bt_th);
        tbFR = findViewById(R.id.bt_fr);
        tbSA = findViewById(R.id.bt_sa);
        tbSU = findViewById(R.id.bt_su);
        swVibrate = findViewById(R.id.switch_vibrate);
        llMusic = findViewById(R.id.ll_music);
        llTimeBack = findViewById(R.id.ll_timeback);
        llTimeOn = findViewById(R.id.ll_timeon);
        llNote = findViewById(R.id.ll_note);

        ((ScrollView) findViewById(R.id.scroll_view)).getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    customSeekbarVolume();
                }
            }
        });

        llMusic.setOnClickListener(this);
        llTimeBack.setOnClickListener(this);
        llTimeOn.setOnClickListener(this);
        llNote.setOnClickListener(this);

        btHenGio.setOnClickListener(this);
        btCancel.setOnClickListener(this);

        tbMO.setOnCheckedChangeListener(this);
        tbTU.setOnCheckedChangeListener(this);
        tbWE.setOnCheckedChangeListener(this);
        tbTH.setOnCheckedChangeListener(this);
        tbFR.setOnCheckedChangeListener(this);
        tbSA.setOnCheckedChangeListener(this);
        tbSU.setOnCheckedChangeListener(this);

        swVibrate.setOnCheckedChangeListener(this);


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_music:
                showDialogMusic();
                break;
            case R.id.ll_timeback:
                showDialogTimeBack();
                break;

            case R.id.ll_timeon:

                showDialogTimeOn();
                break;

            case R.id.ll_note:
                showDialogGhiChu();
                break;

            case R.id.bt_luu:
                saveData();
                Intent intent = new Intent("UPDATEALARM");
                sendBroadcast(intent);

 //               henGio();
//                Intent intent = new Intent("update_alarm");
//                if(positionRV != -1) {
//                    setDay();
//                    AlarmRV alarmRV = new AlarmRV("" + timePicker.getHour(), "" + timePicker.getMinute(),
//                            strDay, true, R.drawable.icon_popup_menu);
//                    intent.putExtra("ALARMRV", alarmRV);
//                    intent.putExtra("POSITIONRV", positionRV);
//                } else {
//                    setDay();
//                    AlarmRV alarmRV = new AlarmRV("" + timePicker.getHour(), "" + timePicker.getMinute(),
//                            strDay, true, R.drawable.icon_popup_menu);
//                    intent.putExtra("ALARMRV", alarmRV);
//                }
//                sendBroadcast(intent);
                finish();
                break;
            case R.id.bt_cancel:
                finish();
                break;
        }
    }


    private void showDialogGhiChu() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            customSeekbarVolume();
        }
        final Dialog dialogGhiChu = new Dialog(this);
        dialogGhiChu.setContentView(R.layout.dialog_ghi_chu);
        edGhiChu = dialogGhiChu.findViewById(R.id.ed_ghi_chu);
        edGhiChu.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        dialogGhiChu.setCanceledOnTouchOutside(false);
        dialogGhiChu.findViewById(R.id.bt_ok_dialog_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edGhiChu.getText() != null && !edGhiChu.getText().toString().trim().equals("")) {
                    txtGhiChu.setText(edGhiChu.getText().toString().trim());
                } else {
                    txtGhiChu.setText("none");
                }

                dialogGhiChu.dismiss();
            }
        });
        dialogGhiChu.findViewById(R.id.bt_huy_dialog_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogGhiChu.dismiss();
            }
        });
        dialogGhiChu.show();
    }

    private void showDialogTimeOn() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            customSeekbarVolume();
        }
        final Dialog dialogTimeOn = new Dialog(this);
        dialogTimeOn.setContentView(R.layout.lv_dialog_timeon_music);
        lvDialogTimeOn = dialogTimeOn.findViewById(R.id.lv_dialog_timeOn_music);
        dialogTimeOn.setCanceledOnTouchOutside(true);
        dialogTimeOn.findViewById(R.id.bt_ok_dialog_timeOn_music).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < listTimeOn.size(); i++) {
                    if (listTimeOn.get(i).isIscheckedMusic()) {
                        txtTimeOn.setText(listTimeOn.get(i).getNameMusic());
                        positionTimeOn = i;
                        break;
                    }
                }
                dialogTimeOn.dismiss();
            }
        });
        dialogTimeOn.findViewById(R.id.bt_huy_dialog_timeOn_music).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTimeOn.dismiss();
            }
        });

        // set click cho listview

        LVDialogTimeOnMusicAdapter.OnDialogListClickListener onDialogListClickListener3 = new
                LVDialogTimeOnMusicAdapter.OnDialogListClickListener() {

                    @Override
                    public void onItemClick(int position) {
                        //bắt sự kiện click thì thay đổi radio button
                        for (int i = 0; i < listTimeOn.size(); i++) {
                            if (listTimeOn.get(i).isIscheckedMusic() == true) {
                                listTimeOn.get(i).setIscheckedMusic(false);
                                break;
                            }
                        }
                        listTimeOn.get(position).setIscheckedMusic(true);
                        adapterLVDialogTimeOn.notifyDataSetChanged();
                    }
                };
        adapterLVDialogTimeOn = new LVDialogTimeOnMusicAdapter(this,
                R.layout.item_listview_am_bao, listTimeOn,
                onDialogListClickListener3);
        lvDialogTimeOn.setAdapter(adapterLVDialogTimeOn);
        dialogTimeOn.show();
    }

    private void showDialogTimeBack() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            customSeekbarVolume();
        }
        final Dialog dialogTimeBack = new Dialog(this);
        dialogTimeBack.setContentView(R.layout.lv_dialog_time_back);
        lvDialogTimeBack = dialogTimeBack.findViewById(R.id.lv_dialog_timeBack);
        dialogTimeBack.setCanceledOnTouchOutside(true);

        dialogTimeBack.findViewById(R.id.bt_ok_dialog_timeback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < listTimeBack.size(); i++) {
                    if (listTimeBack.get(i).isIscheckedMusic()) {
                        txtTimeBack.setText(listTimeBack.get(i).getNameMusic());
                        positionTimeBack = i;
                        break;
                    }
                }
                dialogTimeBack.dismiss();
            }
        });
        dialogTimeBack.findViewById(R.id.bt_huy_dialog_timeBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTimeBack.dismiss();
            }
        });

        // set click cho listview

        LVDialogTimeBackAdapter.OnDialogListClickListener onDialogListClickListener2 = new LVDialogTimeBackAdapter.OnDialogListClickListener() {

            @Override
            public void onItemClick(int position) {
                //bắt sự kiện click thì thay đổi radio button
                for (int i = 0; i < listTimeBack.size(); i++) {
                    if (listTimeBack.get(i).isIscheckedMusic() == true) {
                        listTimeBack.get(i).setIscheckedMusic(false);
                        break;
                    }
                }
                listTimeBack.get(position).setIscheckedMusic(true);
                adapterLVDialogTimeBack.notifyDataSetChanged();
            }
        };
        adapterLVDialogTimeBack = new LVDialogTimeBackAdapter(this,
                R.layout.item_listview_am_bao, listTimeBack,
                onDialogListClickListener2);
        lvDialogTimeBack.setAdapter(adapterLVDialogTimeBack);

        dialogTimeBack.show();
    }


    private void showDialogMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            customSeekbarVolume();
        }
        final Dialog dialogMusic = new Dialog(this);
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
                        txtMusic.setText(listModelDialogMusic.get(i).getNameMusic());
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
        adapterLVDialogMusic = new LVDialogMusicAdapter(this, R.layout.item_listview_am_bao, listModelDialogMusic,
                onDialogListClickListener);
        lvDialogMusic.setAdapter(adapterLVDialogMusic);
        dialogMusic.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.bt_mo:
                break;
            case R.id.bt_tu:

                break;
            case R.id.bt_we:

                break;
            case R.id.bt_th:

                break;
            case R.id.bt_fr:
                break;
            case R.id.bt_sa:
                break;
            case R.id.bt_su:
                break;
        }
    }


//    @SuppressLint("WrongConstant")
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void henGio() {
//        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        calendar = Calendar.getInstance();
//
//        Intent intent = new Intent(CustomAlarmActivity.this, AlarmReceiver.class);
//        intent.putExtra(Constrain.PROGRESS_SEEKBAR, sbVolume.getProgress());
//        intent.putExtra(Constrain.POSITION_MUSIC, positionMusic);
//        intent.putExtra(Constrain.POSITION_TIMEBACK, positionTimeBack);
//        intent.putExtra(Constrain.POSITION_TIMEON, positionTimeOn);
//
//        Log.e("CALENDA", "" + calendar.get(Calendar.HOUR_OF_DAY) + " : " + calendar.get(Calendar.MINUTE));
//        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
//        calendar.set(Calendar.MINUTE, timePicker.getMinute());
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        long intervalTime = calendar.getTimeInMillis();
//
//        //thời gian báo lại
//        for (int i = 0; i < Integer.parseInt(txtCountTimeBack.getText().toString()); i++) {
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(CustomAlarmActivity.this, i,
//                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            alarmManager.set(AlarmManager.RTC_WAKEUP, intervalTime, pendingIntent);
//            Log.e("AAA", "" + txtCountTimeBack.getText().toString());
//            intervalTime = intervalTime + getTimeback() * 60000;
//        }
//
//        //System.currentTimeMillis() là thời gian hiện tại chuẩn của hệ thống tính từ năm 1970 theo milis
//
//        Log.e("TIMEMILLIS", "" + (calendar.getTimeInMillis() - System.currentTimeMillis()));
//        Log.e("TIMEMILLIS", "" + System.currentTimeMillis());
//        Log.e("TIMEMILLIS", "" + Calendar.getInstance().getTimeInMillis());
//    }

    private void dataTimeOn() {
        listTimeOn.add(new ModelLVDiaLogAmBao("15 giây", false));
        listTimeOn.add(new ModelLVDiaLogAmBao("30 giây", false));
        listTimeOn.add(new ModelLVDiaLogAmBao("1 phút (mặc định)", false));
        listTimeOn.add(new ModelLVDiaLogAmBao("2 phút", false));
        listTimeOn.add(new ModelLVDiaLogAmBao("3 phút", false));
    }

    private void dataTimeBack() {
        listTimeBack.add(new ModelLVDiaLogAmBao("không", false));
        listTimeBack.add(new ModelLVDiaLogAmBao("5 phút", false));
        listTimeBack.add(new ModelLVDiaLogAmBao("10 phút (mặc định)", false));
        listTimeBack.add(new ModelLVDiaLogAmBao("20 phút", false));
        listTimeBack.add(new ModelLVDiaLogAmBao("30 phút", false));
        listTimeBack.add(new ModelLVDiaLogAmBao("1 giờ", false));
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void saveData() {
        int hour;
        int minute;
        long interval = 0;
        Calendar calendar = Calendar.getInstance();
        DataAlarm dataAlarm = new DataAlarm();
        if (positionRV != -1) { //luc sua data
            DataAlarmModel dataAlarmModel = listDataAlarmModel.get(positionRV);
            dataAlarmModel.setHour(timePickerCustom.getHour());
            dataAlarmModel.setMinute(timePickerCustom.getMinute());
            dataAlarmModel.setVolume(sbVolume.getProgress());
            dataAlarmModel.setMo(tbMO.isChecked());
            dataAlarmModel.setTu(tbTU.isChecked());
            dataAlarmModel.setWe(tbWE.isChecked());
            dataAlarmModel.setTh(tbTH.isChecked());
            dataAlarmModel.setFr(tbFR.isChecked());
            dataAlarmModel.setSa(tbSA.isChecked());
            dataAlarmModel.setSu(tbSU.isChecked());
            dataAlarmModel.setPositonMusic(positionMusic);
            dataAlarmModel.setVibrate(swVibrate.isChecked());
            dataAlarmModel.setPositionTimeBack(positionTimeBack);
            dataAlarmModel.setPositionTimeOn(positionTimeOn);
            dataAlarmModel.setNote(txtGhiChu.getText().toString());
            dataAlarmModel.setStatus(true);

            dataAlarm.saveDataAlarm(CustomAlarmActivity.this, listDataAlarmModel);



        } else {//them bao thuc
            DataAlarmModel dataAlarmModel = new DataAlarmModel(timePickerCustom.getHour(), timePickerCustom.getMinute(), sbVolume.getProgress(),
                    tbMO.isChecked(), tbTU.isChecked(), tbWE.isChecked(), tbTH.isChecked(), tbFR.isChecked(),
                    tbSA.isChecked(), tbSU.isChecked(), positionMusic, positionTimeBack, swVibrate.isChecked(),
                    positionTimeOn, txtGhiChu.getText().toString(), true);

            dataAlarm.addAlarm(CustomAlarmActivity.this, dataAlarmModel);
        }
        dataAlarm.sortDataAlarm(CustomAlarmActivity.this);

        calendar.set(Calendar.HOUR_OF_DAY, timePickerCustom.getHour());
        calendar.set(Calendar.MINUTE, timePickerCustom.getMinute());
        interval = calendar.getTimeInMillis() - System.currentTimeMillis();
        if (!tbMO.isChecked() && !tbTU.isChecked() && !tbWE.isChecked() && !tbTH.isChecked() && !tbFR.isChecked() &&
                !tbSA.isChecked() && !tbSU.isChecked()) {
            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                interval = calendar.getTimeInMillis() - System.currentTimeMillis() + 86400000;

            } else {
                interval = calendar.getTimeInMillis() - System.currentTimeMillis();
            }
            hour = (int) (interval/3600000);
            minute = (int) ((interval%3600000)/60000) + 1;
            Toast.makeText(this, "còn lại " + hour + "h:" + minute + "m", Toast.LENGTH_SHORT).show();
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void readDataAlarm() {
        positionRV = getIntent().getIntExtra("POSITION", -1);
        DataAlarm dataAlarm = new DataAlarm();
        listDataAlarmModel = dataAlarm.loadDataAlarm(CustomAlarmActivity.this);
        dataTimeOn();
        dataTimeBack();
        dataMusic();
        Log.e("POSITION", positionRV+"");
        if(positionRV != -1) {//ân vào từng item của rv
            timePickerCustom.setHour(listDataAlarmModel.get(positionRV).getHour());
            timePickerCustom.setMinute(listDataAlarmModel.get(positionRV).getMinute());
            sbVolume.setProgress(listDataAlarmModel.get(positionRV).getVolume());

            tbMO.setChecked(listDataAlarmModel.get(positionRV).isMo());
            tbTU.setChecked(listDataAlarmModel.get(positionRV).isTu());
            tbWE.setChecked(listDataAlarmModel.get(positionRV).isWe());
            tbTH.setChecked(listDataAlarmModel.get(positionRV).isTh());
            tbFR.setChecked(listDataAlarmModel.get(positionRV).isFr());
            tbSA.setChecked(listDataAlarmModel.get(positionRV).isSa());
            tbSU.setChecked(listDataAlarmModel.get(positionRV).isSu());
            swVibrate.setChecked(listDataAlarmModel.get(positionRV).isVibrate());

            positionMusic = listDataAlarmModel.get(positionRV).getPositonMusic();
            positionTimeBack = listDataAlarmModel.get(positionRV).getPositionTimeBack();
            positionTimeOn = listDataAlarmModel.get(positionRV).getPositionTimeOn();

            txtMusic.setText(listModelDialogMusic.get(listDataAlarmModel.get(positionRV).getPositonMusic()).getNameMusic());
            listModelDialogMusic.get(listDataAlarmModel.get(positionRV).getPositonMusic()).setIscheckedMusic(true);

            txtTimeBack.setText(listTimeBack.get(listDataAlarmModel.get(positionRV).getPositionTimeBack()).getNameMusic());
            listTimeBack.get(listDataAlarmModel.get(positionRV).getPositionTimeBack()).setIscheckedMusic(true);


            txtTimeOn.setText(listTimeOn.get(listDataAlarmModel.get(positionRV).getPositionTimeOn()).getNameMusic());
            listTimeOn.get(listDataAlarmModel.get(positionRV).getPositionTimeOn()).setIscheckedMusic(true);

            txtGhiChu.setText(listDataAlarmModel.get(positionRV).getNote());

        } else {//ấn vào dấu +
            sbVolume.setProgress(50);

            tbMO.setChecked(false);
            tbTU.setChecked(false);
            tbWE.setChecked(false);
            tbTH.setChecked(false);
            tbFR.setChecked(false);
            tbSA.setChecked(false);
            tbSU.setChecked(false);
            swVibrate.setChecked(true);

            listModelDialogMusic.get(0).setIscheckedMusic(true);
            txtMusic.setText(listModelDialogMusic.get(0).getNameMusic());

            listTimeBack.get(2).setIscheckedMusic(true);
            txtTimeBack.setText(listTimeBack.get(2).getNameMusic());

            listTimeOn.get(2).setIscheckedMusic(true);
            txtTimeOn.setText(listTimeOn.get(2).getNameMusic());

            txtGhiChu.setText("none");
        }
    }



}
