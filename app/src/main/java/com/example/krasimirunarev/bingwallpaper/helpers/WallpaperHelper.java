package com.example.krasimirunarev.bingwallpaper.helpers;

import android.app.WallpaperManager;
import android.content.Context;
import android.util.Log;

import com.example.krasimirunarev.bingwallpaper.Prefs;

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
}
