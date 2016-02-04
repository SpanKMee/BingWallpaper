package com.example.krasimirunarev.bingwallpaper.ws;

import android.content.Context;
import android.os.AsyncTask;

import com.example.krasimirunarev.bingwallpaper.Resolutions;
import com.example.krasimirunarev.bingwallpaper.helpers.WallpaperHelper;
import com.example.krasimirunarev.bingwallpaper.listeners.OnDownloadComplete;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author MentorMate
 */
public class DownloadWallpaper extends AsyncTask<Void, Void, Void> {
    private OnDownloadComplete mOnDownloadComplete;
    private BingResponse images;
    private OkHttpClient mClient;
    private Context mContext;

    private int mNumImages = 1;
    private boolean updateWallpaper = false;

    public DownloadWallpaper(Context context) {
        mContext = context;
        mClient = new OkHttpClient();
        updateWallpaper = true;
    }

    public DownloadWallpaper(Context context, int numImages,
                             OnDownloadComplete onDownloadComplete) {
        mContext = context;
        mOnDownloadComplete = onDownloadComplete;
        mNumImages = numImages;
        mClient = new OkHttpClient();
        updateWallpaper = false;
    }

    @Override
    protected Void doInBackground(Void... params) {
        String json = "";

        Request request = new Request.Builder()
            .url("http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=" + mNumImages +
                     "&mkt=en-US")
            .build();

        Response response;
        try {
            response = mClient.newCall(request).execute();
            json = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        images = new Gson().fromJson(json, BingResponse.class);

        for (int i = 0; i < images.getImages().size(); i++) {
            BingResponse.Image image = images.getImages().get(i);
            image.setInputStream(getInputStream(image));

            if (i == 0 && updateWallpaper) {
                WallpaperHelper.updateWallpaper(mContext, image.getInputStream());
            }
        }

        return null;
    }

    private InputStream getInputStream(BingResponse.Image image) {
        InputStream inputStream = null;

        Request request = new Request.Builder()
            .url(image.getFullUrl(Resolutions.VERY_HIGH))
            .build();

        Response response;
        try {
            response = mClient.newCall(request).execute();
            inputStream = response.body().byteStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputStream;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (mOnDownloadComplete != null) {
            mOnDownloadComplete.onDownloadComplete(images);
        }

    }
}
