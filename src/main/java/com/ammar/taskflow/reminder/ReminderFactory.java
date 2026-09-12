package com.ammar.taskflow.reminder;

import com.ammar.taskflow.domain.DeliveryChannel;
import com.ammar.taskflow.exception.InvalidDeliveryChannelException;

public class ReminderFactory {
    public static ReminderSender getSender(DeliveryChannel channel) {
        return switch (channel) {
            case EMAIL -> new EmailReminderSender();
            case SMS -> new SmsReminderSender();
            case WHATSAPP -> new WhatsappReminderSender();
            case NOTIFICATION -> new NotificationReminderSender();
            default -> throw new InvalidDeliveryChannelException("Unsupported delivery channel");
        };
    }
}
