package com.example.krasimirunarev.bingwallpaper.db.mypictures;

import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * @author MentorMate
 */
public class MyPictureModel {

    public static List<MyPicture> getMyPictures() {
        return SQLite.select().from(MyPicture.class).orderBy(MyPicture_Table.id, false).queryList();
    }

    public static MyPicture findPicture(MyPicture pic) {
        MyPicture picture = SQLite.select().from(MyPicture.class)
                                  .where(MyPicture_Table.filename.is(pic.getFilename()))
                                  .and(MyPicture_Table.image_id.is(pic.getImageId()))
                                  .querySingle();

        return picture;
    }

    public static void deletePicture(MyPicture pic) {
        pic.delete();
    }

    public static void setPictureAsWallpaper(MyPicture pic) {
        // first mark all as not wallpapers
        SQLite.update(MyPicture.class).set(MyPicture_Table.is_wallpaper.eq(false)).execute();

        // then mark and the save the current one as the wallpaper
        pic.setIsWallpaper(true);
        pic.save();
    }

    public static MyPicture getRandomPicture(boolean includeWallpaper) {
        MyPicture pic;

        if (includeWallpaper) {
            pic =
                SQLite.select().from(MyPicture.class)
                      .orderBy(new NameAlias("RANDOM()").tickName(false), true)
                      .limit(1)
                      .querySingle();
        } else {
            pic =
                SQLite.select().from(MyPicture.class)
                      .where(MyPicture_Table.is_wallpaper.is(false))
                      .orderBy(new NameAlias("RANDOM()").tickName(false), true)
                      .limit(1)
                      .querySingle();
        }

        return pic;
    }
}
