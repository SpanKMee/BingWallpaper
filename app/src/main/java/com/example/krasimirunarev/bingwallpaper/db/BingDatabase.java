package com.example.krasimirunarev.bingwallpaper.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * @author MentorMate
 */
@Database(name = BingDatabase.DB_NAME, version = BingDatabase.DB_VERSION)
public class BingDatabase {
    public static final String DB_NAME = "BingWallpaperDb";
    public static final int DB_VERSION = 1;
}
