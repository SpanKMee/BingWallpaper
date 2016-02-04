package com.example.krasimirunarev.bingwallpaper.helpers;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.krasimirunarev.bingwallpaper.Prefs;
import com.example.krasimirunarev.bingwallpaper.db.mypictures.MyPicture;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author MentorMate
 */
public class WallpaperHelper {

    public static void updateWallpaper(final Context context, final InputStream inputStream) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                WallpaperManager manager = WallpaperManager.getInstance(context);
                try {
                    manager.setStream(inputStream);
                    Prefs.updateWallpaper();
                    Log.d("asd", "Updated wallpaper");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }

    public static void updateWallpaper(final Context context, final MyPicture picture) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                WallpaperManager manager = WallpaperManager.getInstance(context);
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(picture.getImagePath(), options);

                    manager.setBitmap(bitmap);

                    Prefs.updateWallpaper();
                    Log.d("asd", "Updated wallpaper");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }
}
