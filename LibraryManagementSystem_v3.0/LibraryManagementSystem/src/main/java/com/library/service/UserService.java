package com.library.service;

import com.library.model.User;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * User business logic service class
 * Core responsibility: Encapsulate all user-related operations (add, delete, query, update),
 * store and manage user data based on an array, ensuring data validity and integrity.
 */
public class UserService {
    // Maximum array capacity: Maximum number of users the system can store
    private static final int MAX_CAPACITY = 50;
    // User array: Stores all user objects
    private User[] userArray = new User[MAX_CAPACITY];
    // Actual user count: Number of users currently stored in the array (<= MAX_CAPACITY)
    private int userCount = 0;
    // File path for saving user data
    private String filePath = null;

    /**
     * Add a new user to the system
     * @param newUser User object to add (contains name, ID, password)
     * @return true if added successfully; false if invalid info, array full, or duplicate ID
     */
    public boolean addUser(User newUser) {
        // 1. Validate parameter legality
        if (newUser == null) {
            System.out.println("Error: User info is empty!");
            return false;
        }
        if (newUser.getUserId() == null || newUser.getUserId().trim().isEmpty()) {
            System.out.println("Error: User ID cannot be empty!");
            return false;
        }
        if (newUser.getUserName() == null || newUser.getUserName().trim().isEmpty()) {
            System.out.println("Error: User name cannot be empty!");
            return false;
        }
        if (newUser.getUserPassword() == null || newUser.getUserPassword().trim().isEmpty()) {
            System.out.println("Error: User password cannot be empty!");
            return false;
        }

        // 2. Check if array is full
        if (userCount >= MAX_CAPACITY) {
            System.out.println("Error: User array is full, cannot add new users!");
            return false;
        }

        // 3. Check if user ID already exists (ID is unique)
        for (int i = 0; i < userCount; i++) {
            if (userArray[i].getUserId().equals(newUser.getUserId())) {
                System.out.println("Error: User with ID [" + newUser.getUserId() + "] already exists!");
                return false;
            }
        }

        // 4. All checks passed, add to array
        userArray[userCount++] = newUser;
        System.out.println("Successfully added user: " + newUser.getUserName() + " (User ID: " + newUser.getUserId() + ")");

        // 5. Save to file if file path is set
        if (filePath != null) {
            saveUsersToFile();
        }

        return true;
    }

    /**
     * Delete a user by ID
     * @param userId ID of the user to delete
     * @return true if deleted successfully; false if ID is empty or user not found
     */
    public boolean deleteUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            System.out.println("Error: User ID cannot be empty!");
            return false;
        }
        for (int i = 0; i < userCount; i++) {
            if (userArray[i].getUserId().equals(userId)) {
                // Shift elements forward to cover the deleted user
                for (int j = i; j < userCount - 1; j++) {
                    userArray[j] = userArray[j + 1];
                }
                userArray[--userCount] = null; // Empty last element for GC
                System.out.println("Successfully deleted user with ID [" + userId + "]!");

                // Save to file if file path is set
                if (filePath != null) {
                    saveUsersToFile();
                }

                return true;
            }
        }
        System.out.println("No user found with ID [" + userId + "]!");
        return false;
    }

    /**
     * Update user information
     * @param userId ID of the user to update
     * @param newUserName New user name
     * @param newPassword New password
     * @return true if updated successfully; false if invalid params or user not found
     */
    public boolean updateUser(String userId, String newUserName, String newPassword) {
        // 1. Validate parameters
        if (userId == null || userId.trim().isEmpty()) {
            System.out.println("Error: User ID cannot be empty!");
            return false;
        }
        if (newUserName == null || newUserName.trim().isEmpty()) {
            System.out.println("Error: New user name cannot be empty!");
            return false;
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            System.out.println("Error: New password cannot be empty!");
            return false;
        }

        // 2. Find user and update info
        for (int i = 0; i < userCount; i++) {
            if (userArray[i].getUserId().equals(userId)) {
                userArray[i].setUserName(newUserName);
                userArray[i].setUserPassword(newPassword);
                System.out.println("Successfully updated info for user ID [" + userId + "]!");

                // Save to file if file path is set
                if (filePath != null) {
                    saveUsersToFile();
                }

                return true;
            }
        }

        System.out.println("No user found with ID [" + userId + "], update failed!");
        return false;
    }

    /**
     * View details of all users in the system
     */
    public void listAllUsers() {
        if (userCount == 0) {
            System.out.println("User array is empty, no users to display!");
            return;
        }
        System.out.println("\n===== All Users List =====");
        for (int i = 0; i < userCount; i++) {
            User user = userArray[i];
            System.out.println("No.: " + (i + 1) +
                    " | User ID: " + user.getUserId() +
                    " | User Name: " + user.getUserName() +
                    " | Password: " + user.getUserPassword());
        }
    }

    /**
     * Search for a user by exact ID match
     * @param userId ID of the user to search for
     */
    public void searchByUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            System.out.println("Error: User ID cannot be empty!");
            return;
        }
        for (int i = 0; i < userCount; i++) {
            User user = userArray[i];
            if (user.getUserId().equals(userId)) {
                System.out.println("\n===== User Details =====");
                System.out.println("User ID: " + user.getUserId());
                System.out.println("User Name: " + user.getUserName());
                System.out.println("Password: " + user.getUserPassword());
                return;
            }
        }
        System.out.println("No user found with ID [" + userId + "]!");
    }

    /**
     * Search for users by fuzzy name match
     * @param nameKeyword Keyword to search in user names (case-insensitive)
     */
    public void searchByUserName(String nameKeyword) {
        if (nameKeyword == null || nameKeyword.trim().isEmpty()) {
            System.out.println("Error: User name keyword cannot be empty!");
            return;
        }
        // Store matched users
        User[] matchedUsers = new User[userCount];
        int matchCount = 0;

        // Traverse array to find matches
        for (int i = 0; i < userCount; i++) {
            User user = userArray[i];
            // Fuzzy match (case-insensitive)
            if (user.getUserName().toLowerCase().contains(nameKeyword.toLowerCase())) {
                matchedUsers[matchCount++] = user;
            }
        }

        // Output results
        if (matchCount == 0) {
            System.out.println("No users found containing [" + nameKeyword + "]!");
            return;
        }

        System.out.println("\n===== Search Results (Name contains: " + nameKeyword + ") =====");
        System.out.println("Found " + matchCount + " related users:");
        for (int i = 0; i < matchCount; i++) {
            User user = matchedUsers[i];
            System.out.println((i + 1) + ". User ID: " + user.getUserId() +
                    " | User Name: " + user.getUserName() +
                    " | Password: " + user.getUserPassword());
        }
    }

    /**
     * Set the file path for saving user data
     * @param filePath Path to the user data file
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Public method to save all users to file
     * This method can be called externally to force save
     */
    public void saveDataToFile() {
        saveUsersToFile();
    }

    /**
     * Save all users to file
     * <p>
     * Writes all users in the array to the specified file.
     * File format: userName,userId,userPassword
     * Preserves the header comments from the original file.
     */
    private void saveUsersToFile() {
        if (filePath == null || filePath.trim().isEmpty()) {
            System.out.println("Warning: File path is not set. Cannot save user data.");
            return;
        }

        try {
            Path path = Paths.get(filePath);
            // Create parent directories if they don't exist
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                // Write header comments
                writer.write("# users.txt — User Data (UTF-8). Each line: user name, user ID, password");
                writer.newLine();
                writer.write("# Empty rows and lines starting with # are ignored");
                writer.newLine();
                writer.newLine();

                // Write all users
                for (int i = 0; i < userCount; i++) {
                    User user = userArray[i];
                    writer.write(user.getUserName() + "," +
                            user.getUserId() + "," +
                            user.getUserPassword());
                    writer.newLine();
                }
            }
            // Save successful - show message for debugging
            System.out.println("User data saved successfully to: " + filePath);
        } catch (IOException e) {
            System.out.println("Warning: Failed to save user data to file: " + e.getMessage());
            System.out.println("File path was: " + filePath);
            e.printStackTrace();
        }
    }
}