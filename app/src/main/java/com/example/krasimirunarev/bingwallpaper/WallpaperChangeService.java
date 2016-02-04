package com.example.krasimirunarev.bingwallpaper;

import android.util.Log;

import com.example.krasimirunarev.bingwallpaper.db.mypictures.MyPicture;
import com.example.krasimirunarev.bingwallpaper.db.mypictures.MyPictureModel;
import com.example.krasimirunarev.bingwallpaper.helpers.WallpaperHelper;
import com.example.krasimirunarev.bingwallpaper.ws.DownloadWallpaper;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

/**
 * @author MentorMate
 */
public class WallpaperChangeService extends GcmTaskService {

    @Override
    public int onRunTask(TaskParams taskParams) {
        if(!Prefs.haveWeUpdatedToday()) {

            // check the source
            if(Prefs.isBingSourceUpdatingOn()) {
                new DownloadWallpaper(getApplicationContext()).execute();

                Log.d("asd", "starting updating wallpaper from bing");
            } else {
                Log.d("asd", "starting updating wallpaper from custom source");

                MyPicture newPic = MyPictureModel.getRandomPicture(false);
                WallpaperHelper.updateWallpaper(getApplicationContext(), newPic);
                MyPictureModel.setPictureAsWallpaper(newPic);
            }
        } else {
            Log.d("asd", "we have updated today already");
        }

        return GcmNetworkManager.RESULT_SUCCESS;
    }
}
