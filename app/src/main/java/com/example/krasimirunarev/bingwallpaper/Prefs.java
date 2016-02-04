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
    private static final String BING_SOURCE_UPDATING_STATUS = "bing_source_updating_status";
    private static final String CUSTOM_SOURCE_UPDATING_STATUS = "custom_source_updating_status";
    private static final String PICTURE_SOURCE = "picture_source";

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

    public static void setPictureSource(int source) {
        sEditor.putInt(PICTURE_SOURCE, source).apply();
    }

    public static int getPictureSouce() {
        return sPrefs.getInt(PICTURE_SOURCE, C.PICTURE_SOURCE_BING);
    }

    public static void setBingSourceUpdatingStatus(boolean status) {
        sEditor.putBoolean(BING_SOURCE_UPDATING_STATUS, status).apply();
    }

    public static boolean isBingSourceUpdatingOn() {
        return sPrefs.getBoolean(BING_SOURCE_UPDATING_STATUS, false);
    }

    public static void setCustomSourceUpdatingStatus(boolean status) {
        sEditor.putBoolean(CUSTOM_SOURCE_UPDATING_STATUS, status).apply();
    }

    public static boolean isCustomSourceUpdatingOn() {
        return sPrefs.getBoolean(CUSTOM_SOURCE_UPDATING_STATUS, false);
    }

    public static boolean isApplicationUpdatingOn() {
        return (isBingSourceUpdatingOn() || isCustomSourceUpdatingOn()) ? true : false;
    }

}
