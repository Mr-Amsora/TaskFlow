package com.ammar.taskflow.event;

import com.ammar.taskflow.domain.Reminder;
import com.ammar.taskflow.reminder.ReminderFactory;
import com.ammar.taskflow.reminder.ReminderSender;
import com.ammar.taskflow.repository.ReminderRepository;

public class ReminderTriggerObserver implements TaskObserver{

    private final ReminderRepository reminderRepository;
    public ReminderTriggerObserver(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    @Override
    public void onTaskEvent(TaskEvent event) {
        if (event instanceof TaskCreatedEvent) {
            Reminder reminder = reminderRepository.findByTask(event.getTask());
            if (reminder != null) {
                ReminderSender sender = ReminderFactory.getSender(reminder.getDeliveryChannel());
                sender.send(reminder);
            }
        }
    }
}
