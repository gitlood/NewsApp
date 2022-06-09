package com.example.news;

public class Article {
    private String title;
    private String webUrl;
    private String date;
    private String sectionName;
    private String thumbnail;

    public Article(String title, String webUrl, String date, String sectionName, String thumbnail) {
        this.title = title;
        this.webUrl = webUrl;
        this.date = date;
        this.sectionName = sectionName;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
