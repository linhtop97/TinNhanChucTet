package com.vdt.trieuvu310198.goldtime.customalarm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.vdt.trieuvu310198.goldtime.R;
import com.vdt.trieuvu310198.goldtime.util.Constrain;

public class Music extends Service {

    private MediaPlayer mediaPlayer;

    private int positonMusic;
    private int positonTimeBack;
    private int positonTimeOn;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        setMusic(intent);

        mediaPlayer.setVolume(intent.getIntExtra(Constrain.PROGRESS_SEEKBAR, 0) * 0.01f,
                intent.getIntExtra(Constrain.PROGRESS_SEEKBAR, 0) * 0.01f);

        setTimeOn(intent);


        return START_NOT_STICKY;
    }

    private void setTimeOn(Intent intent) {
        positonTimeOn = intent.getIntExtra(Constrain.POSITION_TIMEON, 2);
        int millislnFuture = 0;

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
        }

        new CountDownTimer(millislnFuture, 1000) {
            @Override
            public void onTick(long l) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            }

            @Override
            public void onFinish() {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
        }.start();
    }



    private void setMusic(Intent intent) {
        positonMusic = intent.getIntExtra(Constrain.POSITION_MUSIC, 0);
        switch (positonMusic) {
            case 0:
                mediaPlayer = MediaPlayer.create(this, R.raw.cute_song_nhac_chuong);
                break;
            case 1:
                mediaPlayer = MediaPlayer.create(this, R.raw.firefly_nhacchuong);
                break;
            case 2:
                mediaPlayer = MediaPlayer.create(this, R.raw.honey_nhacchuong);
                break;
            case 3:
                mediaPlayer = MediaPlayer.create(this, R.raw.nagging_nhacchuong);
                break;
            case 4:
                mediaPlayer = MediaPlayer.create(this, R.raw.nghedethuongqua_nhacchuong);
                break;
            case 5:
                mediaPlayer = MediaPlayer.create(this, R.raw.nhac_baothuc);
                break;
            case 6:
                mediaPlayer = MediaPlayer.create(this, R.raw.nhacchuong_dethuong);
                break;
            case 7:
                mediaPlayer = MediaPlayer.create(this, R.raw.sac_moi_em_hong_nhacchuong);
                break;
            case 8:
                mediaPlayer = MediaPlayer.create(this, R.raw.doraemon_nhacchuong);
                break;
            case 9:
                mediaPlayer = MediaPlayer.create(this, R.raw.doraemon_nhacchuong);
                break;
            case 10:
                mediaPlayer = MediaPlayer.create(this, R.raw.doraemon_nhacchuong);
                break;

        }
    }
}
