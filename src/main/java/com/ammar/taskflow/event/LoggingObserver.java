package com.ammar.taskflow.event;

public class LoggingObserver implements TaskObserver{
    @Override
    public void onTaskEvent(TaskEvent event) {
        System.out.println(event.getClass().getSimpleName()
                + " — task: " + event.getTask().getTitle());
    }
}
