package com.example.linhnb.project1zergitas.screen.ringtone;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RingtoneHelper {
    private MediaPlayer mMediaPlayer;
    private Context mContext;

    public RingtoneHelper(Context context) {
        mMediaPlayer = new MediaPlayer();
        mContext = context;
    }

    public void listent(Context context, String fileName) {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }
        AssetFileDescriptor descriptor = null;
        try {
            descriptor = context.getAssets().openFd("ringtone/" + fileName);
            long start = descriptor.getStartOffset();
            long end = descriptor.getLength();
            mMediaPlayer.setDataSource(descriptor.getFileDescriptor(), start, end);
            mMediaPlayer.prepare();
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
        } catch (IOException e) {
            //
        }

    }

    public void reset() {
        mMediaPlayer.reset();
    }

    public boolean setRingtone(String fileName) {
        copyAssets(fileName);

        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/MyRingtone/" + fileName);
        if (file.exists()) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
            values.put(MediaStore.MediaColumns.TITLE, fileName.replace("_", " "));
            values.put(MediaStore.MediaColumns.SIZE, file.length());
            values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/aac");
            values.put(MediaStore.Audio.Media.ARTIST, fileName.replace(".aac", ""));
            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
            values.put(MediaStore.Audio.Media.IS_ALARM, false);
            values.put(MediaStore.Audio.Media.IS_MUSIC, false);

            Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
            Uri newUri = mContext.getContentResolver().insert(uri, values);

            RingtoneManager.setActualDefaultRingtoneUri(
                    mContext,
                    RingtoneManager.TYPE_RINGTONE,
                    newUri
            );
            return true;
        } else {
            return false;
        }
    }

    public void copyAssets(String fileName) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyRingtone";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        AssetManager assetManager = mContext.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open("ringtone/" + fileName);
            File outFile = new File(dirPath, fileName);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }

    }
}