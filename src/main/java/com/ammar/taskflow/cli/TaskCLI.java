package com.ammar.taskflow.cli;

import com.ammar.taskflow.domain.Priority;
import com.ammar.taskflow.domain.Task;
import com.ammar.taskflow.domain.User;
import com.ammar.taskflow.service.TaskService;
import com.ammar.taskflow.service.UserService;

import java.time.LocalDateTime;
import java.util.Scanner;

public class TaskCLI {

    private final Scanner scanner = new Scanner(System.in);
    private final TaskService taskService;
    private final UserService userService;

    public TaskCLI(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    public void start() {
        while (true) {
            System.out.println("==============================");
            System.out.println("Tasks Management CLI");
            System.out.println("1. Create Task");
            System.out.println("2. Start Task");
            System.out.println("3. Complete Task");
            System.out.println("4. Delete Task");
            System.out.println("5. Update Task");
            System.out.println("6. List Tasks");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> createTask();
                case "2" -> startTask();
                case "3" -> completeTask();
                case "4" -> deleteTask();
                case "5" -> updateTask();
                case "6" -> listTasks();
                case "7" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void createTask(){
        System.out.print("Enter The Owner ID:");
        Long ownerId = Long.parseLong(scanner.nextLine());
        User user;
        try {
            user = userService.getUserById(ownerId);
        } catch (Exception e) {
            System.out.println("Error occurred while fetching user: " + e.getMessage());
            return;
        }
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();
        System.out.print("Enter task Priority (LOW, MEDIUM, HIGH): ");
        Priority priority ;
        try {
            priority = Priority.valueOf(scanner.nextLine().toUpperCase());
        } catch (Exception e) {
            System.out.println("Invalid priority. Please enter LOW, MEDIUM, or HIGH.");
            return;
        }
        System.out.print("Enter the Due is how many days from now: ");
        int dueInDays;
        try {
            dueInDays = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid number of days.");
            return;
        }
        Task task = taskService.createTask(user, title, LocalDateTime.now().plusDays(dueInDays), priority);
        System.out.println("Task created successfully. Task ID: " + task.getId());
    }

    private void startTask() {
        System.out.print("Enter task ID to start: ");
        Long taskId;
        try {
            taskId = Long.parseLong(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid task ID.");
            return;
        }
        try {
            taskService.startTask(taskId);
        } catch (Exception e) {
            System.out.println("Error occurred while starting task: " + e.getMessage());
        }
    }

    private void completeTask() {
        System.out.print("Enter task ID to complete: ");
        Long taskId;
        try {
            taskId = Long.parseLong(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid task ID.");
            return;
        }
        try {
            taskService.completeTask(taskId);
        } catch (Exception e) {
            System.out.println("Error occurred while completing task: " + e.getMessage());
        }
    }

    private void deleteTask() {
        System.out.print("Enter task ID to delete: ");
        Long taskId;
        try {
            taskId = Long.parseLong(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid task ID.");
            return;
        }
        try {
            taskService.deleteTaskById(taskId);
        } catch (Exception e) {
            System.out.println("Error occurred while deleting task: " + e.getMessage());
        }
    }

    private void listTasks(){
        try {
            taskService.getAllTasks().forEach(task -> {
                System.out.println("Task ID: " + task.getId());
                System.out.println("Title: " + task.getTitle());
                System.out.println("Owner: " + task.getOwner().getName());
                System.out.println("Due Date: " + task.getDueDate());
                System.out.println("Priority: " + task.getPriority());
                System.out.println("Status: " + task.getStatus());
                System.out.println("---------------------------");
            });
        } catch (Exception e) {
            System.out.println("Error occurred while listing tasks: " + e.getMessage());
        }
    }

    private void updateTask(){
        System.out.print("Enter task ID to update: ");
        Long taskId;
        try {
            taskId = Long.parseLong(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid task ID.");
            return;
        }
        Task task = taskService.getTaskById(taskId);
        System.out.print("Enter new title (leave blank to keep current): ");
        String newTitle = scanner.nextLine();
        if (!newTitle.isBlank()) {
            try {
                taskService.updateTaskTitle(taskId, newTitle);
            } catch (Exception e) {
                System.out.println("Error occurred while updating task title: " + e.getMessage());
            }
        }
        System.out.print("Enter new due date in days from now (leave blank to keep current): ");
        String dueInDaysInput = scanner.nextLine();
        int dueInDays;
        if (!dueInDaysInput.isBlank()) {
            try {
                dueInDays = Integer.parseInt(dueInDaysInput);
                taskService.updateTaskDueDate(taskId, LocalDateTime.now().plusDays(dueInDays));
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number of days.");
                return;
            }
        }
        System.out.print("Enter new priority (LOW, MEDIUM, HIGH) (leave blank to keep current): ");
        String priorityInput = scanner.nextLine();
        Priority newPriority;
        if (!priorityInput.isBlank()) {
            try {
                newPriority = Priority.valueOf(priorityInput.toUpperCase());
                taskService.updateTaskPriority(taskId, newPriority);
            } catch (Exception e) {
                System.out.println("Invalid priority. Please enter LOW, MEDIUM, or HIGH.");
                return;
            }
        }
    }

}
