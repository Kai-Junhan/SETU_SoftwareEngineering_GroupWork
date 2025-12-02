package main.java.com.library;


import main.java.com.library.service.BookService;
import main.java.com.library.service.BorrowService;
import main.java.com.library.service.UserService;

import java.util.Scanner;

/**
 * main program entry class: user interface.
 * core responsibility: receives numeric selections from users through the terminal and calls the corresponding service classes to process business logic.
 */
public class Main {
    //scanner for receiving user input
    private static final Scanner scanner = new Scanner(System.in);
    //book service instance
    private static final BookService bookService = new BookService();
    //user service instance
    private static final UserService userService = new UserService();
    //borrow service instance
    private static final BorrowService borrowService = new BorrowService();

    //main program entry method
    public static void main(String[] args) {}

    private static void printMainMenu() {}

    private static void handleBookOperations() {}

    private static void printBookMenu() {}

    private static void addNewBook() {}

    private static void deleteBook() {}

    private static void searchBookByIsbn() {}

    private static void searchBookByName() {}

    private static void handleUserOperations() {}

    private static void printUserMenu() {}

    private static void addNewUser() {}

    private static void deleteUser() {}

    private static void updateUser() {}

    private static void searchUserById() {}

    private static void searchUserByName() {}

    private static void handleBorrowOperations() {}

    private static void printBorrowMenu() {}

    private static void addBorrowRecord() {}

    private static void deleteBorrowRecord() {}

    private static void updateBorrowStatus() {}

    private static void searchBorrowById() {}

    private static void searchBorrowByStatus() {}

    //utility method:get string input from the user
    private static String getStringInput(String prompt) {
        return prompt;
    }

   //utility method:get integer input from the user
    private static int getIntInput(String prompt) {
        return 0;
    }
}