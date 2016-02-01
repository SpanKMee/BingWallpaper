package com.example.krasimirunarev.bingwallpaper;

import android.content.Context;
import android.content.SharedPreferences;

import org.joda.time.DateTime;

/**
 * @author MentorMate
 */
public class Prefs {


    private static SharedPreferences sPrefs;
    private static SharedPreferences.Editor sEditor;

    private static final String PREFS_NAME = "BingWallpaperPreferences";
    private static final String LAST_CHANGE = "last_change";
    private static final String APPLICATION_ON = "application_status";

    public static void init(Context context) {
        sPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sEditor = sPrefs.edit();
    }

    public static boolean haveWeUpdatedToday() {
        DateTime now = DateTime.now().withTimeAtStartOfDay();
        long lastUpdate = sPrefs.getLong(LAST_CHANGE, 0);

        return now.getMillis() == lastUpdate;
    }

    public static void updateWallpaper() {
        DateTime now = DateTime.now().withTimeAtStartOfDay();
        sEditor.putLong(LAST_CHANGE, now.getMillis()).apply();
    }

    public static void setApplicationStatus(boolean status) {
        sEditor.putBoolean(APPLICATION_ON, status).apply();
    }

    public static boolean isApplicationOn() {
        return sPrefs.getBoolean(APPLICATION_ON, false);
    }

}
