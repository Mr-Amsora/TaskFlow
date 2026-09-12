package com.ammar.taskflow.event;

import java.util.ArrayList;
import java.util.List;

public class TaskEventPublisher {

    private List<TaskObserver> observers = new ArrayList<>();

    public void subscribe(TaskObserver observer) {
        observers.add(observer);
    }

    public void publish(TaskEvent event) {
        observers.forEach(observer -> observer.onTaskEvent(event));
    }
}
