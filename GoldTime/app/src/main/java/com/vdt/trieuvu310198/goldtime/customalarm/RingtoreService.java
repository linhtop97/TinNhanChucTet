package com.vdt.trieuvu310198.goldtime.customalarm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.vdt.trieuvu310198.goldtime.MainActivity;
import com.vdt.trieuvu310198.goldtime.R;
import com.vdt.trieuvu310198.goldtime.model.DataAlarmModel;
import com.vdt.trieuvu310198.goldtime.util.Constrain;

import java.util.List;

public class RingtoreService extends Service {

    private MediaPlayer mediaPlayer;

    private int positonMusic;
    private int positonTimeBack;
    private int positonTimeOn;
    private int progressSeekbar;
    private GroupViewWindowManager myViewGroup;
    private Button btWMTimeBack;
    private Button btWMStop;
    private boolean vibrate;
    private Vibrator vibrator;
    private CountDownTimer countDownTimer;
    private TextView tvTimeBack;
    private TextView tvNote;

    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowManager;

    private Notification notification;
    private RemoteViews remoteViews;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder builder;

    private BroadcastReceiver receiver;
    private IntentFilter filter;
    private Handler handler;
    private int countDown;
    private String note;
    private List<DataAlarmModel> listAlarm;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        readData(intent);
        setMusic();
        progressSeekbar = intent.getIntExtra(Constrain.PROGRESS_SEEKBAR, 0);
        mediaPlayer.setVolume(progressSeekbar * 0.01f,
                progressSeekbar * 0.01f);
        setTimeOn();
        initView();
        setClicked();
        return START_STICKY;
    }


    private void readData(Intent intent) {
        positonMusic = intent.getIntExtra(Constrain.POSITION_MUSIC, 0);
        positonTimeOn = intent.getIntExtra(Constrain.POSITION_TIMEON, 0);
        positonTimeBack = intent.getIntExtra(Constrain.POSITION_TIMEBACK, -1);
        vibrate = intent.getBooleanExtra(Constrain.VIBRATE, false);
        note = intent.getStringExtra(Constrain.NOTE);
        Log.e("NOTE", note);
    }


    private void initView() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        createIconView();
        showWM();

    }

    private void showWM() {
        windowManager.addView(myViewGroup, layoutParams);
    }

    private void createIconView() {
        myViewGroup = new GroupViewWindowManager(this);

        View view = View.inflate(this, R.layout.windowmanager_alarm, myViewGroup);
        btWMTimeBack = view.findViewById(R.id.wd_bt_alarm_back);
        btWMStop = view.findViewById(R.id.wd_bt_alarm_stop);
        tvTimeBack = view.findViewById(R.id.tv_timer_alarm);
        tvNote = view.findViewById(R.id.tv_note_alarm);

        layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        //layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //layoutParams.type |= WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

        layoutParams.flags = WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

        tvTimeBack.setText("");
        tvNote.setText(note);

        btWMStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                windowManager.removeView(myViewGroup);
                mediaPlayer.stop();
                mediaPlayer.reset();
                countDownTimer.cancel();
                stopSelf();
            }
        });
        btWMTimeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    windowManager.removeView(myViewGroup);
                    setTimeBack();
                }
            }
        });

    }

    private void setTimeOn() {

        int millislnFuture;

        switch (positonTimeOn) {
            case 0:
                millislnFuture = 15000;
                break;
            case 1:
                millislnFuture = 30000;
                break;
            case 2:
                millislnFuture = 60000;
                break;
            case 3:
                millislnFuture = 12000;
                break;
            case 4:
                millislnFuture = 180000;
                break;
            default:
                millislnFuture = 0;
                break;
        }

        countDownTimer = new CountDownTimer(millislnFuture, 1000) {
            @Override
            public void onTick(long l) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();

                } else {
                    if (vibrate == true) {
                        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            vibrator.vibrate(500);
                        }
                    }
                }
            }

            @Override
            public void onFinish() {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    windowManager.removeView(myViewGroup);
                    setTimeBack();
                }
            }
        };
        countDownTimer.start();
    }

    private void setNotification() {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "channel-01";
        remoteViews = new RemoteViews(this.getPackageName(), R.layout.custom_noti_alarm);
        remoteViews.setImageViewResource(R.id.img_noti_icon, R.drawable.ic_clock);
        remoteViews.setTextViewText(R.id.tv_title_noti, "Báo Lại");
        remoteViews.setTextViewText(R.id.tv_noti_timer, "");
        remoteViews.setTextViewText(R.id.bt_noti_stop, "Dừng");
        Intent btIntent = new Intent("BTCLICKED");
        PendingIntent p_button_intent = PendingIntent.getBroadcast(this, 123, btIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.bt_noti_stop, p_button_intent);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("TABLAYOUT", "ALARM");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 4, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder = new NotificationCompat.Builder(this);
        notification = builder.setSmallIcon(R.drawable.ic_clock)
                .setCustomContentView(remoteViews)
                .setContentIntent(pendingIntent)
                .setContentTitle("Báo Lại")
                .setChannelId(channelId)
                .setOngoing(true)
                .build();

        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            mNotificationManager.createNotificationChannel(mChannel);
        }
        startForeground(4, notification);

    }


    private void setClicked() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    //sự kiện nút dừng trên notification
                    case "BTCLICKED":
                        handler.removeCallbacks(runnable);
                        mNotificationManager.cancel(4);
                        stopSelf();
                        break;
                    default:
                        break;
                }
            }
        };
        filter = new IntentFilter();
        filter.addAction("BTCLICKED");
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void setTimeBack() {
        setNotification();
        handler = new Handler();
        countDown = getTimeback(positonTimeBack);
        handler.post(runnable);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            countDown = countDown - 1;
            handler.postDelayed(this, 1000);
            String time = String.format("%02d", countDown / 60) + ":" + String.format("%02d", countDown % 60);
            remoteViews.setTextViewText(R.id.tv_noti_timer, time);
            mNotificationManager.notify(4, notification);
            if (countDown == 0) {
                handler.removeCallbacks(this);
                mNotificationManager.cancel(4);
                setMusic();
                setTimeOn();
                initView();
            }
        }
    };

    private int getTimeback(int position) {
        switch (position) {
            case 0:
                return 0;
            case 1:
                return 5 * 60;
            case 2:
                return 10 * 60;
            case 3:
                return 20 * 60;
            case 4:
                return 30 * 60;
            default:
                return 0;
        }
    }

    private void setMusic() {

        Log.e("POSITIONMUSIC", "" + positonMusic);
        switch (positonMusic) {
            case 0:
                mediaPlayer = MediaPlayer.create(this, R.raw.music1);
                break;
            case 1:
                mediaPlayer = MediaPlayer.create(this, R.raw.music2);
                break;
            case 2:
                mediaPlayer = MediaPlayer.create(this, R.raw.music3);
                break;
            case 3:
                mediaPlayer = MediaPlayer.create(this, R.raw.music4);
                break;
            case 4:
                mediaPlayer = MediaPlayer.create(this, R.raw.sound1);
                break;
            case 5:
                mediaPlayer = MediaPlayer.create(this, R.raw.sound2);
                break;
            case 6:
                mediaPlayer = MediaPlayer.create(this, R.raw.bells1);
                break;
            case 7:
                mediaPlayer = MediaPlayer.create(this, R.raw.bell2);
                break;
            case 8:
                mediaPlayer = MediaPlayer.create(this, R.raw.bell3);
                break;
            case 9:
                mediaPlayer = MediaPlayer.create(this, R.raw.bell4);
                break;
            case 10:
                mediaPlayer = MediaPlayer.create(this, R.raw.harp1);
                break;
            case 11:
                mediaPlayer = MediaPlayer.create(this, R.raw.harp2);
                break;
            case 12:
                mediaPlayer = MediaPlayer.create(this, R.raw.alert);
                break;
            case 13:
                mediaPlayer = MediaPlayer.create(this, R.raw.xmas1);
                break;

        }
    }

}
