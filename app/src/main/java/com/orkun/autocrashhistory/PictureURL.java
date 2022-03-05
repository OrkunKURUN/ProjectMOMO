package com.orkun.autocrashhistory;

public class PictureURL {
    public String url, urlId, userName;

    public PictureURL(){}

    public PictureURL(String urlId, String userName, String url){
        this.urlId = urlId;
        this.userName = userName;
        this.url = url;
    }

    public String getUrlId() {
        return urlId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUrl() {
        return url;
    }
}
