package com.ammar.taskflow.service;

import com.ammar.taskflow.domain.DeliveryChannel;
import com.ammar.taskflow.domain.Reminder;
import com.ammar.taskflow.domain.Task;
import com.ammar.taskflow.repository.ReminderRepository;

import java.time.LocalDateTime;

public class ReminderService {

    private final ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    public Reminder createReminder(Task linkedTask, LocalDateTime triggerTime, DeliveryChannel deliveryChannel) {
        Reminder existingReminder = reminderRepository.findByTask(linkedTask);
        if (existingReminder != null) {
            throw new IllegalArgumentException("A reminder already exists for this task");
        }
        Reminder reminder = new Reminder(linkedTask, triggerTime, deliveryChannel);
        return reminderRepository.save(reminder);
    }

    public void deleteReminderByLinkedTask(Task linkedTask) {
        getReminderByLinkedTask(linkedTask);
        reminderRepository.deleteById(linkedTask.getId());
    }

    public Reminder updateReminderTriggerTime(Task linkedTask, LocalDateTime newTriggerTime) {
        Reminder reminder = getReminderByLinkedTask(linkedTask);
        reminder.updateTriggerTime(newTriggerTime);
        return reminderRepository.update(reminder);
    }

    public Reminder updateReminderDeliveryChannel(Task linkedTask, DeliveryChannel newDeliveryChannel) {
        Reminder reminder = getReminderByLinkedTask(linkedTask);
        reminder.updateDeliveryChannel(newDeliveryChannel);
        return reminderRepository.update(reminder);
    }


    public Reminder getReminderByLinkedTask(Task task) {
        Reminder reminder = reminderRepository.findByTask(task);
        if (reminder == null) {
            throw new IllegalArgumentException("Reminder not found for this task");
        }
        return reminder;
    }
}
