package com.ammar.taskflow.event;

public class TaskStatsObserver implements TaskObserver{

    private int totalCompletedTasks = 0;

    @Override
    public synchronized void onTaskEvent(TaskEvent event) {
        if (event instanceof TaskCompletedEvent) {
            totalCompletedTasks++;
        }
    }

    public synchronized int getTotalCompletedTasks() {
        return totalCompletedTasks;
    }
}
