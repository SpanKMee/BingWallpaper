package com.example.krasimirunarev.bingwallpaper.listeners;

import com.example.krasimirunarev.bingwallpaper.ws.BingResponse;

/**
 * @author MentorMate
 */
public interface OnDownloadComplete {
    void onDownloadComplete(BingResponse images);
}
