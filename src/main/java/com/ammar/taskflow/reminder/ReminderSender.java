package com.ammar.taskflow.reminder;

import com.ammar.taskflow.domain.Reminder;

public interface ReminderSender {
    public void send(Reminder reminder);
}
