package com.ammar.taskflow.reminder;

import com.ammar.taskflow.domain.Reminder;

public class NotificationReminderSender implements ReminderSender{
    @Override
    public void send(Reminder reminder) {
        System.out.println("Sending notification reminder for task: " + reminder.getLinkedTask().getTitle() + " at " + reminder.getTriggerTime());
    }
}
