package com.example.android.booklisting;

import android.os.Parcelable;

public class googleBook {
    private String author;
    private String title_info;
    private String url;

    public googleBook(String author, String title_info, String url) {
        this.author = author;
        this.title_info = title_info;
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle_info() {
        return title_info;
    }

    public void setTitle_info(String title_info) {
        this.title_info = title_info;
    }

    public String getUrl() {
        return url;
    }

}
