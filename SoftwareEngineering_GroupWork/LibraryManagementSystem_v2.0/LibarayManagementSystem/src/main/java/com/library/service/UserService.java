package main.java.com.library.service;

import main.java.com.library.model.User;

/**
 * User business logic service class.
 * Core responsibility: Encapsulates all business operations related to users, including adding, deleting, querying, and modifying users.
 * Uses an array to store and manage user data, ensuring the legality and integrity of data operations.
 */
public class UserService {
    public boolean addUser(User newUser) {
        return false;
    }
    public boolean deleteUser(String userId) {
        return false;
    }
    public boolean updateUser(String userId, String newUserName, String newPassword) {
        return false;
    }
    public void listAllUsers() {}
    public void searchByUserId(String userId) {}
    public void searchByUserName(String nameKeyword) {}
    }