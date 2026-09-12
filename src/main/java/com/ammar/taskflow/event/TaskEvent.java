package com.ammar.taskflow.event;

import com.ammar.taskflow.domain.Task;

public abstract class TaskEvent {
    private final Task task;

    public TaskEvent(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
}
