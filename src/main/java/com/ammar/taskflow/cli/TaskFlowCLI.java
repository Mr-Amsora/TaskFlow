package com.ammar.taskflow.cli;

import com.ammar.taskflow.domain.Priority;
import com.ammar.taskflow.domain.Task;
import com.ammar.taskflow.domain.User;
import com.ammar.taskflow.service.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TaskFlowCLI {

    private final Scanner scanner = new Scanner(System.in);

    private final UserCLI userCLI;
    private final TaskCLI taskCLI;
    private final ReminderCLI reminderCLI;
    private final UserService userService;
    private final TaskReportService taskReportService;
    private final TaskCacheService taskCacheService;

    public TaskFlowCLI(UserService userService, TaskService taskService, ReminderService reminderService,
                       TaskReportService taskReportService, TaskCacheService taskCacheService) {
        this.userCLI = new UserCLI(userService);
        this.taskCLI = new TaskCLI(taskService, userService);
        this.reminderCLI = new ReminderCLI(reminderService, taskService);
        this.userService = userService;
        this.taskReportService = taskReportService;
        this.taskCacheService = taskCacheService;
    }

    public void start() {
        while (true) {
            System.out.println("==============================");
            System.out.println("TaskFlow Main Menu");
            System.out.println("1. Users");
            System.out.println("2. Tasks");
            System.out.println("3. Reminders");
            System.out.println("4. Reports");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> userCLI.start();
                case "2" -> taskCLI.start();
                case "3" -> reminderCLI.start();
                case "4" -> reportsMenu();
                case "5" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void reportsMenu() {
        while (true) {
            System.out.println("Reports Menu");
            System.out.println("1. Due Soon Report");
            System.out.println("2. Overdue Tasks by Priority");
            System.out.println("3. Average Completion Time");
            System.out.println("4. Tasks Completed This Week (by user)");
            System.out.println("5. Refresh and Show Task Cache");
            System.out.println("6. Back");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> showDueSoonReport();
                case "2" -> showOverdueByPriority();
                case "3" -> showAverageCompletion();
                case "4" -> showCompletedThisWeek();
                case "5" -> showCache();
                case "6" -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void showDueSoonReport() {
        System.out.print("Show tasks due within how many days: ");
        int days;
        try {
            days = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number of days.");
            return;
        }

        try {
            List<Task> dueSoon = taskReportService.getDueSoonReport(LocalDateTime.now().plusDays(days));
            if (dueSoon.isEmpty()) {
                System.out.println("No tasks due soon.");
                return;
            }
            for (Task task : dueSoon) {
                System.out.println("Task ID: " + task.getId());
                System.out.println("Title: " + task.getTitle());
                System.out.println("Due Date: " + task.getDueDate());
                System.out.println("Priority: " + task.getPriority());
                System.out.println("---------------------------");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving due soon report: " + e.getMessage());
        }
    }

    private void showOverdueByPriority() {
        try {
            Map<Priority, Long> result = taskReportService.overdueTaskCountByPriority();
            if (result.isEmpty()) {
                System.out.println("No overdue tasks.");
                return;
            }
            result.forEach((priority, count) -> System.out.println(priority + ": " + count));
        } catch (Exception e) {
            System.out.println("Error retrieving overdue tasks by priority: " + e.getMessage());
        }
    }

    private void showAverageCompletion() {
        try {
            double avg = taskReportService.averageCompletionHours();
            System.out.println("Average completion time: " + avg + " hours");
        } catch (Exception e) {
            System.out.println("Error calculating average completion time: " + e.getMessage());
        }
    }

    private void showCompletedThisWeek() {
        System.out.print("Enter user ID: ");
        Long userId;
        try {
            userId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid user ID. Please enter a valid number.");
            return;
        }

        try {
            User user = userService.getUserById(userId);
            long count = taskReportService.userTaskCompletedThisWeek(user);
            System.out.println("Tasks completed this week: " + count);
        } catch (Exception e) {
            System.out.println("Error retrieving completed tasks: " + e.getMessage());
        }
    }

    private void showCache() {
        try {
            taskCacheService.refreshCache();
            List<Task> cached = taskCacheService.getAllTasks();
            System.out.println("Cached " + cached.size() + " task(s):");
            for (Task task : cached) {
                System.out.println("Task ID: " + task.getId() + ", Title: " + task.getTitle());
            }
        } catch (Exception e) {
            System.out.println("Error refreshing task cache: " + e.getMessage());
        }
    }
}