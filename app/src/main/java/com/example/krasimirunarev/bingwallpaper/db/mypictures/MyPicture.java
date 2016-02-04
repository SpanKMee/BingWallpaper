package com.example.krasimirunarev.bingwallpaper.db.mypictures;

import com.example.krasimirunarev.bingwallpaper.db.BingDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * @author MentorMate
 */
@Table(database = BingDatabase.class, name = "mypictures")
public class MyPicture extends BaseModel {

    @PrimaryKey(autoincrement = true)
    long id;

    @Column(name = "filename")
    String mFilename;

    @Column(name = "image_id")
    long mImageId;

    @Column(name = "image_path")
    String mImagePath;

    @Column(name = "is_wallpaper")
    boolean mIsWallpaper;

    public String getFilename() {
        return mFilename;
    }

    public void setFilename(String filename) {
        mFilename = filename;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getImageId() {
        return mImageId;
    }

    public void setImageId(long imageId) {
        mImageId = imageId;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    public boolean isWallpaper() {
        return mIsWallpaper;
    }

    public void setIsWallpaper(boolean isWallpaper) {
        mIsWallpaper = isWallpaper;
    }
}
