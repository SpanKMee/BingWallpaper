package com.example.krasimirunarev.bingwallpaper;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * @author MentorMate
 */
public class BingWallpaperApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Prefs.init(this);
        JodaTimeAndroid.init(this);
    }
}
