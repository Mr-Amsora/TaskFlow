package com.ammar.taskflow.cli;

import com.ammar.taskflow.service.UserService;

import java.util.Scanner;

public class UserCLI {

    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService;

    public UserCLI(UserService userService) {
        this.userService = userService;
    }

    public void start() {
        while (true) {
            System.out.println("==============================");
            System.out.println("User Management CLI");
            System.out.println("1. Create User");
            System.out.println("2. Delete User");
            System.out.println("3. Update User");
            System.out.println("4. List Users");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createUser();
                case "2" -> deleteUser();
                case "3" -> updateUser();
                case "4" -> listUsers();
                case "5" -> {
                    System.out.println("Exiting User Management CLI.");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void createUser() {
        System.out.print("Enter user name: ");
        String name = scanner.nextLine();
        System.out.print("Enter user email: ");
        String email = scanner.nextLine();

        try {
            userService.createUser(name, email);
            System.out.println("User created successfully.");
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    private void deleteUser() {
        System.out.print("Enter user ID to delete: ");
        try {
            Long userId = Long.parseLong(scanner.nextLine());
            userService.deleteUserById(userId);
            System.out.println("User deleted successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid user ID its must be a number.");
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    private void updateUser(){
        System.out.print("Enter user ID to update: ");
        Long userId ;
        try {
            userId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid user ID. Please enter a valid number.");
            return;
        }
        System.out.print("Enter new email (leave blank to keep current): ");
        String newEmail = scanner.nextLine();
        if (!newEmail.isEmpty()) {
            try {
                userService.updateUserEmail(userId, newEmail);
            }
            catch (Exception e) {
                System.out.println("Error updating user email: " + e.getMessage());
            }
        }
        System.out.print("Enter new name (leave blank to keep current): ");
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) {
            try {
                userService.updateUserName(userId, newName);
            }
            catch (Exception e) {
                System.out.println("Error updating user name: " + e.getMessage());
            }
        }
    }

    private void listUsers() {
        try {
            userService.getAllUsers().forEach(user -> System.out.println("User ID: " + user.getId() + ", Name: " + user.getName() + ", Email: " + user.getEmail()));
        } catch (Exception e) {
            System.out.println("Error listing users: " + e.getMessage());
        }
    }
}
