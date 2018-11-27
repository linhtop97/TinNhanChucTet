package com.example.linhnb.project1zergitas.screen.wallpaper;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.example.linhnb.project1zergitas.data.source.DataCallback;
import com.example.linhnb.project1zergitas.data.source.local.sharedprf.SharedPrefsImpl;
import com.example.linhnb.project1zergitas.data.source.local.sharedprf.SharedPrefsKey;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WallpaperHelper {
    private Context mContext;

    public WallpaperHelper(Context context) {
        mContext = context;
    }

    public boolean setWallpaper(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/MyWallpaper/" + fileName);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }


    }

    public void copyWallpaper(DataCallback<String> callback) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyWallpaper";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        AssetManager assetManager = mContext.getAssets();

        String[] files = null;
        try {
            files = assetManager.list("wallpaper");

        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }

        if (files != null) {
            for (String filename : files) {
                InputStream in = null;
                OutputStream out = null;

                try {
                    in = assetManager.open("wallpaper/" + filename);
                    File f = new File(dirPath + "/" + filename);
                    if (f.exists()) {
                        return;
                    }
                    File outFile = new File(dirPath, filename);
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
            new SharedPrefsImpl(mContext).put(SharedPrefsKey.WALLPAPER_COPIED, true);
            callback.onGetDataSuccess("");
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