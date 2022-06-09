package com.example.news;

public class Article {
    private String title;
    private String webUrl;
    private String date;
    private String sectionName;
    private String thumbnail;
    private String author;

    public Article(String title, String webUrl, String date, String sectionName, String thumbnail, String author) {
        this.title = title;
        this.webUrl = webUrl;
        this.date = date;
        this.sectionName = sectionName;
        this.thumbnail = thumbnail;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
