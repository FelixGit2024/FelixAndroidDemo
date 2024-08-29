package com.firstproject.androiddemofx.model;

public class ModelDay {
    private int img;
    private String day;
    private String dayContent;
    private String dayContentStatus;

    public ModelDay(int img, String day, String dayContent, String dayContentStatus) {
        this.img = img;
        this.day = day;
        this.dayContent = dayContent;
        this.dayContentStatus = dayContentStatus;
    }

    public int getImg() {
        return img;
    }

    public String getDay() {
        return day;
    }

    public String getDayContent() {
        return dayContent;
    }

    public String getDayContentStatus() {
        return dayContentStatus;
    }
}
