package com.library.model;

/**
 * User entity class
 * Stores user information: name, ID, password
 */
public class User {
    // User attributes
    private String userName;
    private String userId;
    private String userPassword;

    // Constructors
    public User() {
    }

    /**
     * Full-parameter constructor
     * @param userName User's name
     * @param userId User's unique ID
     * @param userPassword User's password
     */
    public User(String userName, String userId, String userPassword) {
        this.userName = userName;
        this.userId = userId;
        this.userPassword = userPassword;
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}