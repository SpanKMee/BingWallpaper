package com.example.krasimirunarev.bingwallpaper;

import android.util.Log;

import com.example.krasimirunarev.bingwallpaper.ws.DownloadWallpaper;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

/**
 * @author MentorMate
 */
public class DownloadService extends GcmTaskService {

    @Override
    public int onRunTask(TaskParams taskParams) {
        if(!Prefs.haveWeUpdatedToday()) {
            Log.d("asd", "starting updating wallpaper");
            new DownloadWallpaper(getApplicationContext()).execute();
        } else {
            Log.d("asd", "we have updated today already");
        }

        return GcmNetworkManager.RESULT_SUCCESS;
    }
}
