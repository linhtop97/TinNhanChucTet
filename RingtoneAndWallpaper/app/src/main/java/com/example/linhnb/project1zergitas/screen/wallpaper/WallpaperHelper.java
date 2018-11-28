package com.example.linhnb.project1zergitas.screen.wallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.linhnb.project1zergitas.data.source.DataCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WallpaperHelper {
    private Context mContext;

    public WallpaperHelper(Context context) {
        mContext = context;
    }

    public void setWallpaper(Bitmap bitmap, String fileName, DataCallback<String> callback) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyWallpaper";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dirPath, fileName);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            WallpaperManager myWallpaperManager = WallpaperManager
                    .getInstance(mContext);
            String imageFilePath = dirPath + "/" + fileName;
            Bitmap myBitmap = BitmapFactory.decodeFile(imageFilePath);
            if (myBitmap != null) {
                try {
                    myWallpaperManager.setBitmap(myBitmap);
                    callback.onGetDataSuccess("OK");
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onGetDataSuccess("Failed");
                }
            } else {
                callback.onGetDataSuccess("Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onGetDataSuccess("Failed");
        }
    }

}