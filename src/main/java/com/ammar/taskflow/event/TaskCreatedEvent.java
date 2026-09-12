package com.ammar.taskflow.event;

import com.ammar.taskflow.domain.Task;

public class TaskCreatedEvent extends TaskEvent {

    public TaskCreatedEvent(Task task) {
        super(task);
    }
}
