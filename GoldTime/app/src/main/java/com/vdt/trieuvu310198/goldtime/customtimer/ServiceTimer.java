package com.vdt.trieuvu310198.goldtime.customtimer;

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
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.print.PrinterId;
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
import com.vdt.trieuvu310198.goldtime.customalarm.GroupViewWindowManager;
import com.vdt.trieuvu310198.goldtime.fragment.TimerFragment;
import com.vdt.trieuvu310198.goldtime.util.Constrain;

public class ServiceTimer extends Service {

    private int hour;
    private int minute;
    private int second;
    private long timiilis;
    private Handler handler;
    private BroadcastReceiver receiver;
    private Notification notification;
    private RemoteViews remoteViews;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder builder;
    private MediaPlayer mediaPlayer;
    private int positonMusic;
    private CountDownTimer countDownTimer;
    private boolean vibrate;
    private Vibrator vibrator;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private Button btStop;
    private GroupViewWindowManager myViewGroup;
    private TextView tvNote;

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
        setnotification();
        setMusic(intent);
        setVibrate(intent);

        inIt(intent);


        return START_STICKY;
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
        tvNote = view.findViewById(R.id.tv_note_windowmanager);
        btStop = view.findViewById(R.id.bt_stop_windowmanager);

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

        tvNote.setText("00:00:00");

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

    private void setnotification() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId = "channel-01";
        remoteViews = new RemoteViews(getPackageName(), R.layout.notification_timer);
        remoteViews.setImageViewResource(R.id.img_noti_icon, R.drawable.ic_clock);
        remoteViews.setTextViewText(R.id.tv_title_noti, "Đếm giờ");
        remoteViews.setTextViewText(R.id.tv_noti_timer, "00:00");

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("TABLAYOUT", "TIMER");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder = new NotificationCompat.Builder(this);
        notification = builder.setSmallIcon(R.drawable.ic_clock)
                .setCustomContentView(remoteViews)
                .setContentIntent(pendingIntent)
                .setContentTitle("Đếm giờ")
                .setChannelId(channelId)
                .setVisibility(1)
                .build();
        //mNotificationManager.notify(0, notification);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        String channelName ="Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        mNotificationManager.notify(1, notification);
    }


    private void inIt(Intent intent) {
        hour = intent.getIntExtra("HOUR", -1);
        minute = intent.getIntExtra("MINUTE", -1);
        second = intent.getIntExtra("SECOND", -1);
        Log.e("TIME", "" + second);
        handler = new Handler();
        timiilis = hour * 3600 + minute * 60 + second;
        handler.post(runnable);
        receiverData();

    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timiilis = timiilis - 1;
            Intent intent = new Intent("DATA");
            intent.putExtra("TIMILLIS", timiilis);
            intent.putExtra(Constrain.POSITION_MUSIC, positonMusic);
            sendBroadcast(intent);
            int hour = (int) (timiilis / 3600);
            int minute = (int) ((timiilis % 3600) / 60);
            int second = (int) ((timiilis % 60));
            String time = String.format("%02d", hour) + ":" + String.format("%02d", minute)
                    + ":" + String.format("%02d", second);
            remoteViews.setTextViewText(R.id.tv_noti_timer, time);
            startForeground(1, notification);
            handler.postDelayed(this, 1000);
            Log.e("TIMER", ""+timiilis);
            if (timiilis == 0) {
                playMusic();
                inItView();
                handler.removeCallbacks(runnable);
            }
        }
    };

    private void receiverData() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case "BTHUY":
                        handler.removeCallbacksAndMessages(null);
                        stopSelf();
                        break;

                    default:
                        break;
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("BTHUY");
        registerReceiver(receiver, filter);
    }

    private void setMusic(Intent intent) {
        positonMusic = intent.getIntExtra(Constrain.POSITION_MUSIC, 0);
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

    private void playMusic() {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("SERVICE", "TRUE");
        unregisterReceiver(receiver);
    }
    private void setVibrate(Intent intent) {
        vibrate = intent.getBooleanExtra(Constrain.VIBRATE, true);
        Log.e("EEE", vibrate + "");
    }
}
