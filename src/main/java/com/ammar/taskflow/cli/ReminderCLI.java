package com.ammar.taskflow.cli;

import com.ammar.taskflow.domain.DeliveryChannel;
import com.ammar.taskflow.domain.Reminder;
import com.ammar.taskflow.domain.Task;
import com.ammar.taskflow.service.ReminderService;
import com.ammar.taskflow.service.TaskService;

import java.time.LocalDateTime;
import java.util.Scanner;

public class ReminderCLI {

    private final Scanner scanner = new Scanner(System.in);
    private final ReminderService reminderService;
    private final TaskService taskService;

    public ReminderCLI(ReminderService reminderService, TaskService taskService) {
        this.reminderService = reminderService;
        this.taskService = taskService;
    }

    public void start() {
        while (true){
            System.out.println("==============================");
            System.out.println("Reminder Management CLI");
            System.out.println("1. Create Reminder");
            System.out.println("2. Update Reminder");
            System.out.println("3. Delete Reminder");
            System.out.println("4. List Reminders");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> createReminder();
                case "2" -> updateReminder();
                case "3" -> deleteReminder();
                case "4" -> listReminders();
                case "5" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void createReminder(){
        System.out.print("Enter Task ID for the reminder: ");
        String taskId = scanner.nextLine();
        Task task;
        try {
            task = taskService.getTaskById(Long.parseLong(taskId));
        } catch (NumberFormatException e) {
            System.out.println("Invalid Task ID. Please enter a valid number.");
            return;
        } catch (Exception e) {
            System.out.println("Error retrieving task: " + e.getMessage());
            return;
        }
        if (task.getReminder() != null) {
            System.out.println("A reminder already exists for this task.");
            return;
        }
        System.out.print("Enter in how many hours the reminder from now should: ");
        String hoursInput = scanner.nextLine();
        int hours;
        try {
            hours = Integer.parseInt(hoursInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number of hours. Please enter a valid number.");
            return;
        }
        System.out.print("Enter delivery channel (EMAIL, SMS, WHATSAPP, NOTIFICATION): ");
        String channelInput = scanner.nextLine();
        DeliveryChannel channel;
        try {
            channel = DeliveryChannel.valueOf(channelInput.toUpperCase());
        } catch (Exception e) {
            System.out.println("Invalid delivery channel. Please enter one of the following: EMAIL, SMS, WHATSAPP, NOTIFICATION.");
            return;
        }
        try {
            Reminder reminder = reminderService.createReminder(task, LocalDateTime.now().plusHours(hours), channel);
            System.out.println("Reminder created successfully for task with ID: " + reminder.getLinkedTask().getId());
        } catch (Exception e) {
            System.out.println("Error creating reminder: " + e.getMessage());
        }
    }

    private void updateReminder() {
        System.out.print("Enter Task ID for the reminder to update: ");
        String taskId = scanner.nextLine();
        Task task;
        try {
            task = taskService.getTaskById(Long.parseLong(taskId));
        } catch (NumberFormatException e) {
            System.out.println("Invalid Task ID. Please enter a valid number.");
            return;
        } catch (Exception e) {
            System.out.println("Error retrieving task: " + e.getMessage());
            return;
        }
        Reminder reminder;
        try {
            reminder = reminderService.getReminderByLinkedTask(task);
        } catch (Exception e) {
            System.out.println("Error retrieving reminder: " + e.getMessage());
            return;
        }
        System.out.print("Enter new trigger time in hours from now (leave blank to keep current): ");
        String hoursInput = scanner.nextLine();
        int hours = 0;
        if (!hoursInput.isEmpty()) {
            try {
                hours = Integer.parseInt(hoursInput);
                LocalDateTime newTriggerTime = LocalDateTime.now().plusHours(hours);
                if (newTriggerTime.isAfter(task.getDueDate())) {
                    System.out.println("Trigger time cannot be after the task's due date.");
                    return;
                }
                reminderService.updateReminderTriggerTime(task, newTriggerTime);
                System.out.println("Reminder updated successfully for task with ID: " + task.getId());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number of hours. Please enter a valid number.");
                return;
            }
        }
        System.out.print("Enter new delivery channel (EMAIL, SMS, WHATSAPP, NOTIFICATION) (leave blank to keep current): ");
        String channelInput = scanner.nextLine();
        if (!channelInput.isEmpty()) {
            DeliveryChannel channel;
            try {
                channel = DeliveryChannel.valueOf(channelInput.toUpperCase());
                reminderService.updateReminderDeliveryChannel(task, channel);
                System.out.println("Delivery channel updated successfully for task with ID: " + task.getId());
            } catch (Exception e) {
                System.out.println("Invalid delivery channel. Please enter one of the following: EMAIL, SMS, WHATSAPP, NOTIFICATION.");
                return;
            }
        }
    }

    private void deleteReminder() {
        System.out.print("Enter Task ID for the reminder to delete: ");
        String taskId = scanner.nextLine();
        Task task;
        try {
            task = taskService.getTaskById(Long.parseLong(taskId));
        } catch (NumberFormatException e) {
            System.out.println("Invalid Task ID. Please enter a valid number.");
            return;
        } catch (Exception e) {
            System.out.println("Error retrieving task: " + e.getMessage());
            return;
        }
        try {
            reminderService.deleteReminderByLinkedTask(task);
            System.out.println("Reminder deleted successfully for task with ID: " + task.getId());
        } catch (Exception e) {
            System.out.println("Error deleting reminder: " + e.getMessage());
        }
    }

    private void listReminders(){
        System.out.println("Listing all reminders:");
        for (Task task : taskService.getAllTasks()) {
            Reminder reminder = task.getReminder();
            if (reminder != null) {
                System.out.println("Task ID: " + task.getId() + ", Reminder Trigger Time: " + reminder.getTriggerTime() + ", Delivery Channel: " + reminder.getDeliveryChannel());
            }
        }
    }
}
