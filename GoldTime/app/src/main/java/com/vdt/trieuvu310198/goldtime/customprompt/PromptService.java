package com.vdt.trieuvu310198.goldtime.customprompt;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.VoiceInteractor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
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

import com.vdt.trieuvu310198.goldtime.R;
import com.vdt.trieuvu310198.goldtime.customalarm.GroupViewWindowManager;
import com.vdt.trieuvu310198.goldtime.fragment.TimerFragment;
import com.vdt.trieuvu310198.goldtime.util.Constrain;

import java.util.List;

public class PromptService extends Service {

    private MediaPlayer mediaPlayer;
    private int positonRingtore;
    private boolean vibrate;
    private Vibrator vibrator;
    private CountDownTimer countDownTimer;
    private GroupViewWindowManager myViewGroup;
    private Button btStop;
    private TextView tvNote;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private String strNote;
    private Notification notification;
    private RemoteViews remoteViews;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder builder;
    private int position;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        turnOffNotification();
        setMusic(intent);
        setVibrate(intent);
        inIt(intent);
        xuLi();
        inItView();
        return START_NOT_STICKY;
    }

    private void inIt(Intent intent) {
        strNote = intent.getStringExtra("NOTEPROMPT");
        position = intent.getIntExtra("POSITION", -1);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        createIconView();


    }

    private void turnOffNotification() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.cancel(100+position);
    }

    private void inItView() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        createIconView();
        showWM();

    }

    private void showWM() {
        windowManager.addView(myViewGroup, layoutParams);
    }

    private void createIconView() {
        myViewGroup = new GroupViewWindowManager(this);

        View view = View.inflate(this, R.layout.windowmanager_prompt, myViewGroup);
        btStop = view.findViewById(R.id.bt_stop_windowmanager);
        tvNote = view.findViewById(R.id.tv_note_windowmanager);

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

        tvNote.setText(strNote);

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                windowManager.removeView(myViewGroup);
                mediaPlayer.stop();
                mediaPlayer.reset();
                countDownTimer.cancel();
                stopSelf();
            }
        });


    }



    private void xuLi() {
        countDownTimer = new CountDownTimer(60000, 1000) {
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
                    windowManager.removeView(myViewGroup);
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    stopSelf();

                }
            }
        };
        countDownTimer.start();
    }

    private void setMusic(Intent intent) {
        positonRingtore = intent.getIntExtra("RINGTORE", 0);
        switch (positonRingtore) {
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

    private void setVibrate(Intent intent) {
        vibrate = intent.getBooleanExtra(Constrain.VIBRATE, true);
        Log.e("EEE", vibrate + "");
    }
}
