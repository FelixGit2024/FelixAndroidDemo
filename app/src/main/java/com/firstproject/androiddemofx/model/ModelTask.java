package com.firstproject.androiddemofx.model;

public class ModelTask {
    private String taskNum;
    private String dayTask;

    public ModelTask(String taskNum, String dayTask) {
        this.taskNum = taskNum;
        this.dayTask = dayTask;
    }

    public String getTaskNum() {
        return taskNum;
    }

    public String getDayTask() {
        return dayTask;
    }
}
