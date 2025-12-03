package main.java.com.library.service;

import main.java.com.library.model.User;

/**
 * User service class
 * Handles user operations: add, delete, query, update.
 * Uses array to store data.
 */
public class UserService {
    //Max number of users the system can hold
    private static final int MAX_CAPACITY = 50;
    //Array to store all users
    private User[] userArray = new User[MAX_CAPACITY];
    //Current number of users
    private int userCount = 0;

    /**
     * Add a new user
     * @param newUser User to add
     * @return true if successful, false otherwise
     */
    public boolean addUser(User newUser) {
        //Check if input is valid
        if (newUser == null) {
            System.out.println("Error: User information is empty!");
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
            System.out.println("Error: Password cannot be empty!");
            return false;
        }

        //Check if array is full
        if (userCount >= MAX_CAPACITY) {
            System.out.println("Error: User storage is full, cannot add new users!");
            return false;
        }

        //Check if user ID already exists
        for (int i = 0; i < userCount; i++) {
            if (userArray[i].getUserId().equals(newUser.getUserId())) {
                System.out.println("Error: User with ID [" + newUser.getUserId() + "] already exists!");
                return false;
            }
        }

        //Add user
        userArray[userCount++] = newUser;
        System.out.println("Successfully added user: " + newUser.getUserName() + " (ID: " + newUser.getUserId() + ")");
        return true;
    }

    /**
     * Delete user by ID
     * @param userId ID of user to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            System.out.println("Error: User ID cannot be empty!");
            return false;
        }
        for (int i = 0; i < userCount; i++) {
            if (userArray[i].getUserId().equals(userId)) {
                //Move elements forward to cover the deleted one
                for (int j = i; j < userCount - 1; j++) {
                    userArray[j] = userArray[j + 1];
                }
                userArray[--userCount] = null;
                System.out.println("Successfully deleted user with ID [" + userId + "]!");
                return true;
            }
        }
        System.out.println("User with ID [" + userId + "] not found!");
        return false;
    }

    /**
     * Update user information
     * @param userId User ID
     * @param newUserName New name
     * @param newPassword New password
     * @return true if successful, false otherwise
     */
    public boolean updateUser(String userId, String newUserName, String newPassword) {
        //Check input validity
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

        //Find user and update
        for (int i = 0; i < userCount; i++) {
            if (userArray[i].getUserId().equals(userId)) {
                userArray[i].setUserName(newUserName);
                userArray[i].setUserPassword(newPassword);
                System.out.println("Successfully updated user with ID [" + userId + "]!");
                return true;
            }
        }

        System.out.println("User with ID [" + userId + "] not found, update failed!");
        return false;
    }

    /**
     * Show all users
     */
    public void listAllUsers() {
        if (userCount == 0) {
            System.out.println("No users in the system!");
            return;
        }
        System.out.println("\n===== All Users =====");
        for (int i = 0; i < userCount; i++) {
            User user = userArray[i];
            System.out.println("No.: " + (i + 1) +
                    " | User ID: " + user.getUserId() +
                    " | Name: " + user.getUserName() +
                    " | Password: " + user.getUserPassword());
        }
    }

    /**
     * Search user by ID
     * @param userId ID to search
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
                System.out.println("Name: " + user.getUserName());
                System.out.println("Password: " + user.getUserPassword());
                return;
            }
        }
        System.out.println("User with ID [" + userId + "] not found!");
    }

    /**
     * Search users by name keyword
     * @param nameKeyword Keyword to search
     */
    public void searchByUserName(String nameKeyword) {
        if (nameKeyword == null || nameKeyword.trim().isEmpty()) {
            System.out.println("Error: Name keyword cannot be empty!");
            return;
        }
        //Store matching users
        User[] matchedUsers = new User[userCount];
        int matchCount = 0;

        //Find matches
        for (int i = 0; i < userCount; i++) {
            User user = userArray[i];
            //Case-insensitive search
            if (user.getUserName().toLowerCase().contains(nameKeyword.toLowerCase())) {
                matchedUsers[matchCount++] = user;
            }
        }

        //Show results
        if (matchCount == 0) {
            System.out.println("No users found containing [" + nameKeyword + "]!");
            return;
        }

        System.out.println("\n===== Search Results (Name contains: " + nameKeyword + ") =====");
        System.out.println("Found " + matchCount + " user(s):");
        for (int i = 0; i < matchCount; i++) {
            User user = matchedUsers[i];
            System.out.println((i + 1) + ". User ID: " + user.getUserId() +
                    " | Name: " + user.getUserName() +
                    " | Password: " + user.getUserPassword());
        }
    }
}