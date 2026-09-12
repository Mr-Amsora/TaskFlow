package com.ammar.taskflow.event;

public class TaskStatsObserver implements TaskObserver{

    private int totalCompletedTasks = 0;

    @Override
    public void onTaskEvent(TaskEvent event) {
        if (event instanceof TaskCompletedEvent) {
            totalCompletedTasks++;
        }
    }

    public int getTotalCompletedTasks() {
        return totalCompletedTasks;
    }
}
