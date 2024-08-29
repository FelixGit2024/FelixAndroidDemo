package com.firstproject.androiddemofx.model;

public class ModelTask {
    private int img;
    private String dayTask;

    public ModelTask(int img, String dayTask) {
        this.img = img;
        this.dayTask = dayTask;
    }

    public int getImg() {
        return img;
    }

    public String getDayTask() {
        return dayTask;
    }
}
