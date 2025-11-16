package com.library;

import com.library.controller.MenuController;
import com.library.service.BookService;
import com.library.service.UserService;
import com.library.service.BorrowService;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    // Static instances for service classes and scanner
    private static final Scanner scanner = new Scanner(System.in);
    private static final BookService bookService = new BookService();
    private static final UserService userService = new UserService();
    private static final BorrowService borrowService = new BorrowService();
    // Data file names (in resources folder)
    private static final String RESOURCE_BASE = "";
    private static final String BOOK_DATA_FILE = RESOURCE_BASE + "books.txt";
    private static final String USER_DATA_FILE = RESOURCE_BASE + "users.txt";
    private static final String BORROW_RECORD_DATA_FILE = RESOURCE_BASE + "borrow_records.txt";

    // Static block: Load data when class is initialized
    static {
        loadBooksFromFile(BOOK_DATA_FILE);
        loadUsersFromFile(USER_DATA_FILE);
        loadBorrowRecordsFromFile(BORROW_RECORD_DATA_FILE);
        // Set file paths for saving (try to get absolute path to resources folder)
        setBookDataFilePath();
        setUserDataFilePath();
        setBorrowRecordDataFilePath();

        // Add shutdown hook to save data when program exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nSaving data...");
            saveAllData();
            System.out.println("Data saved, program exiting.");
        }));
    }

    public static void main(String[] args) {
        // Create menu controller with dependencies
        MenuController menuController = new MenuController(scanner, bookService, userService, borrowService);
        // Start main menu
        menuController.startMainMenu();
        // Close scanner to prevent resource leak
        scanner.close();
    }

    /**
     * Load book data from a text file.
     * File format: bookTitle,author,ISBN,totalQuantity,borrowedQuantity
     * Skips invalid lines (wrong format, non-numeric quantities).
     *
     * @param fileName Name of the book data file (relative to resources folder)
     */
    private static void loadBooksFromFile(String fileName) {
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream(fileName);
                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                // Skip empty lines or comment lines (starting with #)
                String trimmedLine = line.trim();
                if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                    continue;
                }

                String[] parts = trimmedLine.split(",");
                if (parts.length == 5) {
                    try {
                        String name = parts[0].trim();
                        String author = parts[1].trim();
                        String isbn = parts[2].trim();
                        int quantity = Integer.parseInt(parts[3].trim());
                        int borrowed = Integer.parseInt(parts[4].trim());
                        // Add book to service
                        bookService.addBook(new com.library.model.Book(name, author, isbn, quantity, borrowed));
                    } catch (NumberFormatException e) {
                        System.out.println("Skip invalid line: " + line + ". Reason: " + e.getMessage());
                    }
                } else {
                    System.out.println("Skip line with wrong format: " + line);
                }
            }
            System.out.println("Book data loaded successfully!");
        } catch (IOException e) {
            System.out.println("Failed to load book data: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Book data file not found: " + fileName);
        }
    }

    /**
     * Get the absolute path to a file in resources folder
     * Uses multiple strategies to find the project root directory
     *
     * @param fileName Name of the file in resources folder
     * @return Absolute path to the file, or null if cannot determine
     */
    private static String getResourceFilePath(String fileName) {
        // Strategy 1: Try to get the actual path of resource file from classpath first
        try {
            URL resourceUrl = Main.class.getClassLoader().getResource(fileName);
            if (resourceUrl != null) {
                if ("file".equals(resourceUrl.getProtocol())) {
                    // If it's file protocol, return absolute path directly
                    try {
                        File resourceFile = new File(resourceUrl.toURI());
                        return resourceFile.getAbsolutePath();
                    } catch (Exception e) {
                        // If URI conversion fails, try to get directly from URL path
                        String path = resourceUrl.getPath();
                        if (path != null && !path.isEmpty()) {
                            return path;
                        }
                    }
                } else if ("jar".equals(resourceUrl.getProtocol())) {
                    // If it's jar protocol, file is inside jar package, need to extract to temp
                    // directory or use other strategy
                    // Here we fall back to using resources folder in project directory
                }
            }
        } catch (Exception e) {
            // Continue to next strategy
        }

        // Strategy 2: If not found in classpath, or unable to get file path, search for
        // resources folder in project
        String userDir = System.getProperty("user.dir");
        File currentDir = new File(userDir);

        // First check if current directory is NUIST-SETU-HW project directory
        if ("NUIST-SETU-HW".equals(currentDir.getName())) {
            File targetFile = new File(currentDir, "src/main/resources/" + fileName);
            if (targetFile.exists()) {
                return targetFile.getAbsolutePath();
            }
        }

        // Check if parent directory is NUIST-SETU-HW project directory
        File parentDir = currentDir.getParentFile();
        if (parentDir != null && "NUIST-SETU-HW".equals(parentDir.getName())) {
            File targetFile = new File(parentDir, "src/main/resources/" + fileName);
            if (targetFile.exists()) {
                return targetFile.getAbsolutePath();
            }
        }

        // Strategy 3: Check resources files in compiled output directory
        File searchDir = currentDir;
        // Go up to 5 levels to find the compiled resources
        for (int i = 0; i < 5 && searchDir != null; i++) {
            File outDir = new File(searchDir, "out");
            if (outDir.exists() && outDir.isDirectory()) {
                File productionDir = new File(outDir, "production");
                if (productionDir.exists() && productionDir.isDirectory()) {
                    // Find NUIST-SETU-HW directory
                    File[] projectDirs = productionDir.listFiles(File::isDirectory);
                    if (projectDirs != null) {
                        for (File projectDir : projectDirs) {
                            File targetFile = new File(projectDir, fileName);
                            if (targetFile.exists()) {
                                return targetFile.getAbsolutePath();
                            }
                        }
                    }
                }
            }
            searchDir = searchDir.getParentFile();
        }


        // Strategy 4: Finally try to create file in current project's resources
        // directory
        // Prioritize using NUIST-SETU-HW project directory
        if (parentDir != null && "NUIST-SETU-HW".equals(parentDir.getName())) {
            File targetFile = new File(parentDir, "src/main/resources/" + fileName);
            // Ensure parent directory exists
            targetFile.getParentFile().mkdirs();
            return targetFile.getAbsolutePath();
        }

        // Fallback - use current directory + src/main/resources
        File targetFile = new File(currentDir, "src/main/resources/" + fileName);
        targetFile.getParentFile().mkdirs();
        return targetFile.getAbsolutePath();
    }

    /**
     * Set the file path for saving book data
     * Always saves to the source directory (src/main/resources/books.txt) for easy
     * editing
     */
    private static void setBookDataFilePath() {
        String filePath = getResourceFilePath(BOOK_DATA_FILE);
        bookService.setFilePath(filePath);
        System.out.println("Book data will be saved to: " + filePath);
    }

    /**
     * Load user data from a text file.
     * File format: userName,userId,userPassword
     * Skips invalid lines (wrong format).
     *
     * @param fileName Name of the user data file (relative to resources folder)
     */
    private static void loadUsersFromFile(String fileName) {
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream(fileName);
                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                // Skip empty lines or comment lines (starting with #)
                String trimmedLine = line.trim();
                if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                    continue;
                }

                String[] parts = trimmedLine.split(",");
                if (parts.length == 3) {
                    try {
                        String userName = parts[0].trim();
                        String userId = parts[1].trim();
                        String userPassword = parts[2].trim();
                        // Add user to service
                        userService.addUser(new com.library.model.User(userName, userId, userPassword));
                    } catch (Exception e) {
                        System.out.println("Skip invalid line: " + line + ". Reason: " + e.getMessage());
                    }
                } else {
                    System.out.println("Skip line with wrong format: " + line);
                }
            }
            System.out.println("User data loaded successfully!");
        } catch (IOException e) {
            System.out.println("Failed to load user data: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("User data file not found: " + fileName);
        }
    }

    /**
     * Load borrow record data from a text file.
     * File format: borrowId,borrowDate,borrowStatus
     * Skips invalid lines (wrong format, non-numeric status).
     *
     * @param fileName Name of the borrow record data file (relative to resources
     *                 folder)
     */
    private static void loadBorrowRecordsFromFile(String fileName) {
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream(fileName);
                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                // Skip empty lines or comment lines (starting with #)
                String trimmedLine = line.trim();
                if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                    continue;
                }

                String[] parts = trimmedLine.split(",");
                if (parts.length == 3) {
                    try {
                        String borrowId = parts[0].trim();
                        String borrowDate = parts[1].trim();
                        int borrowStatus = Integer.parseInt(parts[2].trim());
                        // Add borrow record to service
                        borrowService.addBorrowRecord(
                                new com.library.model.BorrowRecord(borrowId, borrowDate, borrowStatus));
                    } catch (NumberFormatException e) {
                        System.out.println("Skip invalid line: " + line + ". Reason: " + e.getMessage());
                    }
                } else {
                    System.out.println("Skip line with wrong format: " + line);
                }
            }
            System.out.println("Borrow record data loaded successfully!");
        } catch (IOException e) {
            System.out.println("Failed to load borrow record data: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Borrow record data file not found: " + fileName);
        }
    }

    /**
     * Set the file path for saving user data
     * Always saves to the source directory (src/main/resources/users.txt) for easy
     * editing
     */
    private static void setUserDataFilePath() {
        String filePath = getResourceFilePath(USER_DATA_FILE);
        userService.setFilePath(filePath);
        System.out.println("User data will be saved to: " + filePath);
    }

    /**
     * Save all data to files (books, users, borrow records)
     * This method can be called manually or by shutdown hook
     */
    public static void saveAllData() {
        try {
            bookService.saveDataToFile();
            userService.saveDataToFile();
            borrowService.saveDataToFile();
            System.out.println("All data saved successfully!");
        } catch (Exception e) {
            System.out.println("Error occurred while saving data: " + e.getMessage());
        }
    }

    /**
     * Set the file path for saving borrow record data
     * Always saves to the source directory (src/main/resources/borrow_records.txt)
     * for easy editing
     */
    private static void setBorrowRecordDataFilePath() {
        String filePath = getResourceFilePath(BORROW_RECORD_DATA_FILE);
        borrowService.setFilePath(filePath);
        System.out.println("Borrow record data will be saved to: " + filePath);
    }
}