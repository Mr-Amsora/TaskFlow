package com.ammar.taskflow.reminder;

import com.ammar.taskflow.domain.Reminder;

public class WhatsappReminderSender implements ReminderSender{
    @Override
    public void send(Reminder reminder) {
        System.out.println("Sending WhatsApp reminder for task: " + reminder.getLinkedTask().getTitle() + " at " + reminder.getTriggerTime());
    }
}
