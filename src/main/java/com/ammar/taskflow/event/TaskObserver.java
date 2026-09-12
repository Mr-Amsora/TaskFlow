package com.ammar.taskflow.event;

public interface TaskObserver {
    void onTaskEvent(TaskEvent event);
}
