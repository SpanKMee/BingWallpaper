package com.example.krasimirunarev.bingwallpaper.ws;

import com.google.gson.annotations.SerializedName;

import java.io.InputStream;
import java.util.List;

/**
 * @author MentorMate
 */
public class BingResponse {

    private static final String BASE_URL = "https://bing.com";

    private List<Image> images;

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getImageUrl() {
        return BASE_URL + getImages().get(0).getUrl();
    }

    public static class Image {
        @SerializedName("urlbase")
        private String url;

        private String copyright;
        private InputStream inputStream;

        public String getFullUrl(String resolution) {
            return BASE_URL + url + "_" + resolution + ".jpg";
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCopyright() {
            return copyright;
        }

        public void setCopyright(String copyright) {
            this.copyright = copyright;
        }

        public InputStream getInputStream() {
            return inputStream;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }
    }

}
