package main.java.com.library.model;

//User entity class
public class User {
    //1. Attributes
    private String userName;
    private String userId;
    private String userPassword;

    //2. Constructors
    public User() {
    }
    public User(String userName, String userId, String userPassword) {
        this.userName = userName;
        this.userId = userId;
        this.userPassword = userPassword;
    }

    //3. Getters and Setters
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