package com.library.controller;

import com.library.service.BookService;
import com.library.service.UserService;
import com.library.service.BorrowService;
import java.util.Scanner;

public class MenuController {
    private final Scanner scanner;
    private final BookService bookService;
    private final UserService userService;
    private final BorrowService borrowService;

    // Constructor: Inject service dependencies
    public MenuController(Scanner scanner, BookService bookService,
                          UserService userService, BorrowService borrowService) {
        this.scanner = scanner;
        this.bookService = bookService;
        this.userService = userService;
        this.borrowService = borrowService;
    }

    // Control main menu flow
    public void startMainMenu() {
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = getIntInput("Please enter function number: ");
            switch (choice) {
                case 1 -> handleBookOperations();
                case 2 -> handleUserOperations();
                case 3 -> handleBorrowOperations();
                case 4 -> saveAllData();
                case 0 -> {
                    System.out.println("Thank you for using, goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid function number, please re-enter!");
            }
        }
    }

    // Handle book management operations
    private void handleBookOperations() {
        boolean back = false;
        while (!back) {
            printBookMenu();
            int choice = getIntInput("Please enter book operation number: ");
            switch (choice) {
                case 1 -> addNewBook();
                case 2 -> deleteBook();
                case 3 -> updateBook();
                case 4 -> bookService.listAllBooks();
                case 5 -> searchBookByIsbn();
                case 6 -> searchBookByName();
                case 7 -> searchBookByAuthor();
                case 0 -> back = true;
                default -> System.out.println("Invalid operation number, please re-enter!");
            }
        }
    }

    // Handle user management operations
    private void handleUserOperations() {
        boolean back = false;
        while (!back) {
            printUserMenu();
            int choice = getIntInput("Please enter user operation number: ");
            switch (choice) {
                case 1 -> addNewUser();
                case 2 -> deleteUser();
                case 3 -> updateUser();
                case 4 -> userService.listAllUsers();
                case 5 -> searchUserById();
                case 6 -> searchUserByName();
                case 0 -> back = true;
                default -> System.out.println("Invalid operation number, please re-enter!");
            }
        }
    }

    // Handle borrow/return management operations
    private void handleBorrowOperations() {
        boolean back = false;
        while (!back) {
            printBorrowMenu();
            int choice = getIntInput("Please enter borrow/return operation number: ");
            switch (choice) {
                case 1 -> addBorrowRecord();
                case 2 -> deleteBorrowRecord();
                case 3 -> updateBorrowStatus();
                case 4 -> borrowService.listAllBorrowRecords();
                case 5 -> searchBorrowById();
                case 6 -> searchBorrowByStatus();
                case 0 -> back = true;
                default -> System.out.println("Invalid operation number, please re-enter!");
            }
        }
    }

    // Print main menu
    private void printMainMenu() {
        System.out.println("\n===== Library Management System =====");
        System.out.println("1. Book Management");
        System.out.println("2. User Management");
        System.out.println("3. Borrow/Return Management");
        System.out.println("4. Save All Data");
        System.out.println("0. Exit System");
        System.out.println("======================================");
    }

    // Print book management menu
    private void printBookMenu() {
        System.out.println("\n===== Book Management =====");
        System.out.println("1. Add Book");
        System.out.println("2. Delete Book");
        System.out.println("3. Update Book");
        System.out.println("4. View All Books");
        System.out.println("5. Search by ISBN");
        System.out.println("6. Search by Book Name");
        System.out.println("7. Search by Author");
        System.out.println("0. Return to Main Menu");
        System.out.println("===========================");
    }

    // Print user management menu
    private void printUserMenu() {
        System.out.println("\n===== User Management =====");
        System.out.println("1. Add User");
        System.out.println("2. Delete User");
        System.out.println("3. Update User Information");
        System.out.println("4. View All Users");
        System.out.println("5. Search by User ID");
        System.out.println("6. Search by User Name");
        System.out.println("0. Return to Main Menu");
        System.out.println("===========================");
    }

    // Print borrow/return management menu
    private void printBorrowMenu() {
        System.out.println("\n===== Borrow/Return Management =====");
        System.out.println("1. Add Borrow/Return Record");
        System.out.println("2. Delete Borrow/Return Record");
        System.out.println("3. Update Borrow/Return Status");
        System.out.println("4. View All Records");
        System.out.println("5. Search Record by ID");
        System.out.println("6. Search Records by Status");
        System.out.println("0. Return to Main Menu");
        System.out.println("====================================");
    }

    // Book operation methods
    private void addNewBook() {
        System.out.println("\n----- Add New Book -----");
        String name = getStringInput("Please enter book title: ");
        String author = getStringInput("Please enter author: ");
        String isbn = getStringInput("Please enter ISBN: ");
        int quantity = getIntInput("Please enter total quantity: ");
        int borrowed = getIntInput("Please enter borrowed quantity: ");
        com.library.model.Book book = new com.library.model.Book(name, author, isbn, quantity, borrowed);
        bookService.addBook(book);
    }

    private void updateBook() {
        System.out.println("\n----- Update Book Information -----");
        String isbn = getStringInput("Please enter ISBN of the book to update: ");
        
        // Show current book information first
        System.out.println("\nCurrent book information:");
        bookService.searchByISBN(isbn);
        
        System.out.println("\nEnter new information (leave empty to keep current):");
        String newName = getStringInput("New book title: ");
        String newAuthor = getStringInput("New author: ");
        
        System.out.println("Enter new quantities (enter -1 to keep current):");
        int newQuantity = getIntInput("New total quantity: ");
        int newBorrowedQuantity = getIntInput("New borrowed quantity: ");
        
        // Convert empty strings to null for name/author
        newName = newName.isEmpty() ? null : newName;
        newAuthor = newAuthor.isEmpty() ? null : newAuthor;
        
        bookService.updateBook(isbn, newName, newAuthor, newQuantity, newBorrowedQuantity);
    }

    private void deleteBook() {
        System.out.println("\n----- Delete Book -----");
        String isbn = getStringInput("Please enter ISBN of the book to delete: ");
        bookService.deleteBook(isbn);
    }

    private void searchBookByIsbn() {
        System.out.println("\n----- Search by ISBN -----");
        String isbn = getStringInput("Please enter ISBN: ");
        bookService.searchByISBN(isbn);
    }

    private void searchBookByName() {
        System.out.println("\n----- Search by Book Name -----");
        String keyword = getStringInput("Please enter book name keyword: ");
        bookService.searchByBookName(keyword);
    }

    private void searchBookByAuthor() {
        System.out.println("\n----- Search by Author -----");
        String authorName = getStringInput("Please enter author name: ");
        bookService.searchByAuthor(authorName);
    }

    // User operation methods
    private void addNewUser() {
        System.out.println("\n----- Add New User -----");
        String id = getStringInput("Please enter user ID: ");
        String name = getStringInput("Please enter user name: ");
        String password = getStringInput("Please enter password: ");
        com.library.model.User user = new com.library.model.User(name, id, password);
        userService.addUser(user);
    }

    private void deleteUser() {
        System.out.println("\n----- Delete User -----");
        String userId = getStringInput("Please enter ID of the user to delete: ");
        userService.deleteUser(userId);
    }

    private void updateUser() {
        System.out.println("\n----- Update User Information -----");
        String userId = getStringInput("Please enter ID of the user to update: ");
        String newName = getStringInput("Please enter new user name: ");
        String newPassword = getStringInput("Please enter new password: ");
        userService.updateUser(userId, newName, newPassword);
    }

    private void searchUserById() {
        System.out.println("\n----- Search by User ID -----");
        String userId = getStringInput("Please enter user ID: ");
        userService.searchByUserId(userId);
    }

    private void searchUserByName() {
        System.out.println("\n----- Search by User Name -----");
        String keyword = getStringInput("Please enter user name keyword: ");
        userService.searchByUserName(keyword);
    }

    // Borrow/return operation methods
    private void addBorrowRecord() {
        System.out.println("\n----- Add Borrow/Return Record -----");
        String recordId = getStringInput("Please enter record ID: ");
        String date = getStringInput("Please enter borrow/return date (yyyy-MM-dd): ");
        int status = getIntInput("Please enter status (0-Checked Out, 1-Returned): ");
        com.library.model.BorrowRecord record = new com.library.model.BorrowRecord(recordId, date, status);
        borrowService.addBorrowRecord(record);
    }

    private void deleteBorrowRecord() {
        System.out.println("\n----- Delete Borrow/Return Record -----");
        String recordId = getStringInput("Please enter ID of the record to delete: ");
        borrowService.deleteBorrowRecord(recordId);
    }

    private void updateBorrowStatus() {
        System.out.println("\n----- Update Borrow/Return Status -----");
        String recordId = getStringInput("Please enter ID of the record to update: ");
        int newStatus = getIntInput("Please enter new status (0-Checked Out, 1-Returned): ");
        borrowService.updateBorrowStatus(recordId, newStatus);
    }

    private void searchBorrowById() {
        System.out.println("\n----- Search Record by ID -----");
        String recordId = getStringInput("Please enter record ID: ");
        borrowService.searchByBorrowId(recordId);
    }

    private void searchBorrowByStatus() {
        System.out.println("\n----- Search Records by Status -----");
        int status = getIntInput("Please enter status (0-Checked Out, 1-Returned): ");
        borrowService.searchByStatus(status);
    }

    private void saveAllData() {
        System.out.println("\n----- Save All Data -----");
        try {
            bookService.saveDataToFile();
            userService.saveDataToFile();
            borrowService.saveDataToFile();
            System.out.println("All data saved successfully!");
        } catch (Exception e) {
            System.out.println("Error occurred while saving data: " + e.getMessage());
        }
    }

    // Input utility methods
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Input error, please enter a valid number!");
            }
        }
    }
}
