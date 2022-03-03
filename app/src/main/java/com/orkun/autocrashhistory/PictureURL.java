package com.orkun.autocrashhistory;

public class PictureURL {
    public int urlId, recordId;
    public String url;

    public PictureURL(){}

    public PictureURL(int urlId, int recordId, String url){
        this.urlId = urlId;
        this.recordId = recordId;
        this.url = url;
    }

    public int getUrlId() {
        return urlId;
    }

    public int getRecordId() {
        return recordId;
    }

    public String getUrl() {
        return url;
    }
}
