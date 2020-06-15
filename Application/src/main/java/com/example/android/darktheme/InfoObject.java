package com.example.android.darktheme;

public class InfoObject {
    String date;
    Integer pages;
    Double speed;

    public InfoObject(Integer pages, String date, Double speed) {
        this.date = date;
        this.pages = pages;
        this.speed = speed;
    }

    public Integer getPages() {
        return pages;
    }

    public String getDate() {
        return date;
    }

    public Double getSpeed() {
        return speed;
    }
}
