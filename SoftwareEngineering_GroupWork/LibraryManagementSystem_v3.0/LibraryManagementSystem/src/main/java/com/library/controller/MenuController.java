package com.library.controller;

import com.library.Main;
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

    public static void printMenu() {
        System.out.println(Main.ConsoleColor.BOLD + Main.ConsoleColor.BLUE +
                "╔════════════════════════════════════════╗" + Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.BOLD + Main.ConsoleColor.BLUE +
                "║       LibraryManagementSystem V3.0     ║" + Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.BOLD + Main.ConsoleColor.BLUE +
                "╚════════════════════════════════════════╝" + Main.ConsoleColor.RESET);
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
        System.out.println(Main.ConsoleColor.CYAN +"\n===== Library Management System ====="+ Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE + "1. Book Management"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE +"2. User Management"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE +"3. Borrow/Return Management"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE +"4. Save All Data"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.RED +"0. Exit System"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.CYAN + "======================================" + Main.ConsoleColor.RESET);
    }

    // Print book management menu
    private void printBookMenu() {
        System.out.println(Main.ConsoleColor.CYAN +"\n===== Library Management System ====="+ Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE+"1. Add Book"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE+"2. Delete Book"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE+"3. Update Book"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE+"4. View All Books"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE+"5. Search by ISBN"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE+"6. Search by Book Name"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE+"7. Search by Author"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.RED+"0. Return to Main Menu"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.CYAN + "======================================" + Main.ConsoleColor.RESET);
    }

    // Print user management menu
    private void printUserMenu() {
        System.out.println(Main.ConsoleColor.CYAN +"\n===== Library Management System ====="+ Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE+"1. Add User"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE+"2. Delete User"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE+"3. Update User Information"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE+"4. View All Users"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE+"5. Search by User ID"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE+"6. Search by User Name"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.RED+"0. Return to Main Menu"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.CYAN + "======================================" + Main.ConsoleColor.RESET);
    }

    // Print borrow/return management menu
    private void printBorrowMenu() {
        System.out.println(Main.ConsoleColor.CYAN +"\n===== Library Management System ====="+ Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE+"1. Add Borrow/Return Record"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE+"2. Delete Borrow/Return Record"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE+"3. Update Borrow/Return Status"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE+"4. View All Records"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE+"5. Search Record by ID"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.PURPLE+"6. Search Records by Status"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.RED+"0. Return to Main Menu"+Main.ConsoleColor.RESET);
        System.out.println(Main.ConsoleColor.CYAN + "======================================" + Main.ConsoleColor.RESET);
    }

    // Book operation methods
    private void addNewBook() {
        System.out.println(Main.ConsoleColor.YELLOW+"\n----- Add New Book -----"+ Main.ConsoleColor.RESET);
        String name = getStringInput(Main.ConsoleColor.PURPLE+"Please enter book title: "+ Main.ConsoleColor.RESET);
        String author = getStringInput(Main.ConsoleColor.PURPLE+"Please enter author: "+ Main.ConsoleColor.RESET);
        String isbn = getStringInput(Main.ConsoleColor.PURPLE+"Please enter ISBN: "+ Main.ConsoleColor.RESET);
        int quantity = getIntInput(Main.ConsoleColor.PURPLE+"Please enter total quantity: "+ Main.ConsoleColor.RESET);
        int borrowed = getIntInput(Main.ConsoleColor.PURPLE+"Please enter borrowed quantity: "+ Main.ConsoleColor.RESET);
        com.library.model.Book book = new com.library.model.Book(name, author, isbn, quantity, borrowed);
        bookService.addBook(book);
    }

    private void updateBook() {
        System.out.println(Main.ConsoleColor.YELLOW+"\n----- Update Book Information -----"+ Main.ConsoleColor.RESET);
        String isbn = getStringInput(Main.ConsoleColor.BLUE+"Please enter ISBN of the book to update: ");
        
        // Show current book information first
        System.out.println(Main.ConsoleColor.YELLOW+"\nCurrent book information:"+ Main.ConsoleColor.RESET);
        bookService.searchByISBN(isbn);
        
        System.out.println(Main.ConsoleColor.YELLOW+"\nEnter new information (leave empty to keep current):"+ Main.ConsoleColor.RESET);
        String newName = getStringInput(Main.ConsoleColor.BLUE+"New book title: "+ Main.ConsoleColor.RESET);
        String newAuthor = getStringInput(Main.ConsoleColor.BLUE+"New author: "+ Main.ConsoleColor.RESET);
        
        System.out.println(Main.ConsoleColor.YELLOW+"Enter new quantities (enter -1 to keep current):"+ Main.ConsoleColor.RESET);
        int newQuantity = getIntInput(Main.ConsoleColor.BLUE+"New total quantity: "+ Main.ConsoleColor.RESET);
        int newBorrowedQuantity = getIntInput(Main.ConsoleColor.BLUE+"New borrowed quantity: "+ Main.ConsoleColor.RESET);
        
        // Convert empty strings to null for name/author
        newName = newName.isEmpty() ? null : newName;
        newAuthor = newAuthor.isEmpty() ? null : newAuthor;
        
        bookService.updateBook(isbn, newName, newAuthor, newQuantity, newBorrowedQuantity);
    }

    private void deleteBook() {
        System.out.println(Main.ConsoleColor.YELLOW+"\n----- Delete Book -----"+ Main.ConsoleColor.RESET);
        String isbn = getStringInput("Please enter ISBN of the book to delete: ");
        bookService.deleteBook(isbn);
    }

    private void searchBookByIsbn() {
        System.out.println(Main.ConsoleColor.YELLOW+"\n----- Search by ISBN -----"+ Main.ConsoleColor.RESET);
        String isbn = getStringInput("Please enter ISBN: ");
        bookService.searchByISBN(isbn);
    }

    private void searchBookByName() {
        System.out.println(Main.ConsoleColor.YELLOW+"\n----- Search by Book Name -----"+ Main.ConsoleColor.RESET);
        String keyword = getStringInput("Please enter book name keyword: ");
        bookService.searchByBookName(keyword);
    }

    private void searchBookByAuthor() {
        System.out.println(Main.ConsoleColor.YELLOW+"\n----- Search by Author -----"+ Main.ConsoleColor.RESET);
        String authorName = getStringInput("Please enter author name: ");
        bookService.searchByAuthor(authorName);
    }

    // User operation methods
    private void addNewUser() {
        System.out.println(Main.ConsoleColor.YELLOW+"\n----- Add New User -----"+ Main.ConsoleColor.RESET);
        String id = getStringInput("Please enter user ID: ");
        String name = getStringInput("Please enter user name: ");
        String password = getStringInput("Please enter password: ");
        com.library.model.User user = new com.library.model.User(name, id, password);
        userService.addUser(user);
    }

    private void deleteUser() {
        System.out.println(Main.ConsoleColor.YELLOW+"\n----- Delete User -----"+ Main.ConsoleColor.RESET);
        String userId = getStringInput("Please enter ID of the user to delete: ");
        userService.deleteUser(userId);
    }

    private void updateUser() {
        System.out.println(Main.ConsoleColor.YELLOW+"\n----- Update User Information -----"+ Main.ConsoleColor.RESET);
        String userId = getStringInput("Please enter ID of the user to update: ");
        String newName = getStringInput("Please enter new user name: ");
        String newPassword = getStringInput("Please enter new password: ");
        userService.updateUser(userId, newName, newPassword);
    }

    private void searchUserById() {
        System.out.println(Main.ConsoleColor.YELLOW+"\n----- Search by User ID -----"+ Main.ConsoleColor.RESET);
        String userId = getStringInput("Please enter user ID: ");
        userService.searchByUserId(userId);
    }

    private void searchUserByName() {
        System.out.println(Main.ConsoleColor.YELLOW+"\n----- Search by User Name -----"+ Main.ConsoleColor.RESET);
        String keyword = getStringInput("Please enter user name keyword: ");
        userService.searchByUserName(keyword);
    }

    // Borrow/return operation methods
    private void addBorrowRecord() {
        System.out.println(Main.ConsoleColor.YELLOW+"\n----- Add Borrow/Return Record -----"+ Main.ConsoleColor.RESET);
        String recordId = getStringInput("Please enter record ID: ");
        String date = getStringInput("Please enter borrow/return date (yyyy-MM-dd): ");
        int status = getIntInput("Please enter status (0-Checked Out, 1-Returned): ");
        com.library.model.BorrowRecord record = new com.library.model.BorrowRecord(recordId, date, status);
        borrowService.addBorrowRecord(record);
    }

    private void deleteBorrowRecord() {
        System.out.println(Main.ConsoleColor.YELLOW+"\n----- Delete Borrow/Return Record -----"+ Main.ConsoleColor.RESET);
        String recordId = getStringInput("Please enter ID of the record to delete: ");
        borrowService.deleteBorrowRecord(recordId);
    }

    private void updateBorrowStatus() {
        System.out.println(Main.ConsoleColor.YELLOW+"\n----- Update Borrow/Return Status -----"+ Main.ConsoleColor.RESET);
        String recordId = getStringInput("Please enter ID of the record to update: ");
        int newStatus = getIntInput("Please enter new status (0-Checked Out, 1-Returned): ");
        borrowService.updateBorrowStatus(recordId, newStatus);
    }

    private void searchBorrowById() {
        System.out.println(Main.ConsoleColor.YELLOW+"\n----- Search Record by ID -----"+ Main.ConsoleColor.RESET);
        String recordId = getStringInput("Please enter record ID: ");
        borrowService.searchByBorrowId(recordId);
    }

    private void searchBorrowByStatus() {
        System.out.println(Main.ConsoleColor.YELLOW+"\n----- Search Records by Status -----"+ Main.ConsoleColor.RESET);
        int status = getIntInput("Please enter status (0-Checked Out, 1-Returned): ");
        borrowService.searchByStatus(status);
    }

    private void saveAllData() {
        System.out.println(Main.ConsoleColor.YELLOW+"\n----- Save All Data -----"+ Main.ConsoleColor.RESET);
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
