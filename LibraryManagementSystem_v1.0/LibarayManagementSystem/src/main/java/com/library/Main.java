package main.java.com.library;

import main.java.com.library.model.Book;
import main.java.com.library.model.BorrowRecord;
import main.java.com.library.model.User;
import main.java.com.library.service.BookService;
import main.java.com.library.service.BorrowService;
import main.java.com.library.service.UserService;
import java.util.Scanner;

/**
 * User interface
 * Lets users choose functions by entering numbers
 */
public class Main {

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = getIntInput("Please enter function number:");
            switch (choice) {
                case 1 -> handleBookOperations();
                case 2 -> handleUserOperations();
                case 3 -> handleBorrowOperations();
                case 0 -> {
                    System.out.println("Thank you for using, goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid function number, please re-enter!");
            }
        }
        scanner.close();
    }

    private static final Scanner scanner = new Scanner(System.in);
    private static final BookService bookService = new BookService();
    private static final UserService userService = new UserService();
    private static final BorrowService borrowService = new BorrowService();

    private static void printMainMenu() {
        System.out.println("\n===== Library Management System =====");
        System.out.println("1. Book Management");
        System.out.println("2. User Management");
        System.out.println("3. Borrow/Return Management");
        System.out.println("0. Exit System");
        System.out.println("====================================");
    }

    private static void handleBookOperations() {
        boolean back = false;
        while (!back) {
            printBookMenu();
            int choice = getIntInput("Please enter book operation number:");
            switch (choice) {
                case 1 -> addNewBook();
                case 2 -> deleteBook();
                case 3 -> bookService.listAllBooks();
                case 4 -> searchBookByIsbn();
                case 5 -> searchBookByName();
                case 0 -> back = true;
                default -> System.out.println("Invalid operation number, please re-enter!");
            }
        }
    }

    private static void printBookMenu() {
        System.out.println("\n===== Book Management =====");
        System.out.println("1. Add Book");
        System.out.println("2. Delete Book");
        System.out.println("3. View All Books");
        System.out.println("4. Search Book by ISBN");
        System.out.println("5. Search Book by Name");
        System.out.println("0. Return to Main Menu");
        System.out.println("===========================");
    }

    private static void addNewBook() {
        System.out.println("\n----- Add New Book -----");
        String name = getStringInput("Please enter book title:");
        String author = getStringInput("Please enter author:");
        String isbn = getStringInput("Please enter ISBN:");
        int quantity = getIntInput("Please enter total quantity:");
        int borrowed = getIntInput("Please enter borrowed quantity:");

        Book book = new Book(name, author, isbn, quantity, borrowed);
        bookService.addBook(book);
    }

    private static void deleteBook() {
        System.out.println("\n----- Delete Book -----");
        String isbn = getStringInput("Please enter ISBN of book to delete:");
        bookService.deleteBook(isbn);
    }

    private static void searchBookByIsbn() {
        System.out.println("\n----- Search by ISBN -----");
        String isbn = getStringInput("Please enter ISBN:");
        bookService.searchByISBN(isbn);
    }

    private static void searchBookByName() {
        System.out.println("\n----- Search by Title -----");
        String keyword = getStringInput("Please enter title keyword:");
        bookService.searchByBookName(keyword);
    }

    private static void handleUserOperations() {
        boolean back = false;
        while (!back) {
            printUserMenu();
            int choice = getIntInput("Please enter user operation number:");
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

    private static void printUserMenu() {
        System.out.println("\n===== User Management =====");
        System.out.println("1. Add User");
        System.out.println("2. Delete User");
        System.out.println("3. Update User Info");
        System.out.println("4. View All Users");
        System.out.println("5. Search User by ID");
        System.out.println("6. Search User by Name");
        System.out.println("0. Return to Main Menu");
        System.out.println("===========================");
    }

    private static void addNewUser() {
        System.out.println("\n----- Add New User -----");
        String id = getStringInput("Please enter user ID:");
        String name = getStringInput("Please enter user name:");
        String password = getStringInput("Please enter password:");

        User user = new User(name, id, password);
        userService.addUser(user);
    }

    private static void deleteUser() {
        System.out.println("\n----- Delete User -----");
        String userId = getStringInput("Please enter ID of user to delete:");
        userService.deleteUser(userId);
    }

    private static void updateUser() {
        System.out.println("\n----- Update User Info -----");
        String userId = getStringInput("Please enter ID of user to update:");
        String newName = getStringInput("Please enter new user name:");
        String newPassword = getStringInput("Please enter new password:");
        userService.updateUser(userId, newName, newPassword);
    }

    private static void searchUserById() {
        System.out.println("\n----- Search User by ID -----");
        String userId = getStringInput("Please enter user ID:");
        userService.searchByUserId(userId);
    }

    private static void searchUserByName() {
        System.out.println("\n----- Search User by Name -----");
        String keyword = getStringInput("Please enter name keyword:");
        userService.searchByUserName(keyword);
    }

    private static void handleBorrowOperations() {
        boolean back = false;
        while (!back) {
            printBorrowMenu();
            int choice = getIntInput("Please enter borrow/return operation number:");
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


    private static void printBorrowMenu() {
        System.out.println("\n===== Borrow/Return Management =====");
        System.out.println("1. Add Record");
        System.out.println("2. Delete Record");
        System.out.println("3. Update Status");
        System.out.println("4. View All Records");
        System.out.println("5. Search Record by ID");
        System.out.println("6. Search Records by Status");
        System.out.println("0. Return to Main Menu");
        System.out.println("====================================");
    }

    private static void addBorrowRecord() {
        System.out.println("\n----- Add Borrow Record -----");
        String recordId = getStringInput("Please enter record ID:");
        String date = getStringInput("Please enter date(yyyy-MM-dd):");
        int status = getIntInput("Please enter status(0-Checked Out, 1-Returned):");

        BorrowRecord record = new BorrowRecord(recordId, date, status);
        borrowService.addBorrowRecord(record);
    }

    private static void deleteBorrowRecord() {
        System.out.println("\n----- Delete Borrow Record -----");
        String recordId = getStringInput("Please enter ID of record to delete:");
        borrowService.deleteBorrowRecord(recordId);
    }

    private static void updateBorrowStatus() {
        System.out.println("\n----- Update Borrow Status -----");
        String recordId = getStringInput("Please enter ID of record to update:");
        int newStatus = getIntInput("Please enter new status(0-Checked Out, 1-Returned):");
        borrowService.updateBorrowStatus(recordId, newStatus);
    }

    private static void searchBorrowById() {
        System.out.println("\n----- Search Record by ID -----");
        String recordId = getStringInput("Please enter record ID:");
        borrowService.searchByBorrowId(recordId);
    }

    private static void searchBorrowByStatus() {
        System.out.println("\n----- Search Records by Status -----");
        int status = getIntInput("Please enter status(0-Checked Out, 1-Returned):");
        borrowService.searchByStatus(status);
    }

    // Get string input
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // Get integer input
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Input format error, please enter an integer!");
            }
        }
    }
}