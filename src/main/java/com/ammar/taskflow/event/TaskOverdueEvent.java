package com.ammar.taskflow.event;

import com.ammar.taskflow.domain.Task;

public class TaskOverdueEvent extends TaskEvent{
    public TaskOverdueEvent(Task task) {
        super(task);
    }
}
