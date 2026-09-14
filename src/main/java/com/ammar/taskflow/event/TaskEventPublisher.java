package com.ammar.taskflow.event;

import java.util.ArrayList;
import java.util.List;

public class TaskEventPublisher {

    private List<TaskObserver> observers = new ArrayList<>();

    public synchronized void subscribe(TaskObserver observer) {
        observers.add(observer);
    }

    public synchronized void publish(TaskEvent event) {
        observers.forEach(observer -> observer.onTaskEvent(event));
    }
}
