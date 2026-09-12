package com.ammar.taskflow.event;

import com.ammar.taskflow.domain.Task;

public class TaskCompletedEvent extends TaskEvent{
    public TaskCompletedEvent(Task task) {
        super(task);
    }
}
