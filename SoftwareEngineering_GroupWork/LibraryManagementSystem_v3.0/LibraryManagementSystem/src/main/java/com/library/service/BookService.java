package com.library.service;

import com.library.model.Book;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Book business logic service class
 *
 * Main job: Handle all book-related operations like adding, deleting, querying, searching.
 * Store and manage books with an array. Ensure valid and complete data operations.
 */
public class BookService {

    // Max array capacity: maximum number of books the system can store
    private static final int MAX_CAPACITY = 100;
    // Book array: stores all book objects
    private Book[] bookArray = new Book[MAX_CAPACITY];
    // Actual book count: number of books stored (≤ MAX_CAPACITY)
    private int bookCount = 0;
    // File path for saving book data
    private String filePath = null;

    /**
     * Set the file path for saving book data
     * @param filePath Path to the book data file
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Add a new book to the system
     * <p>
     * Check if book info is valid (not empty, valid quantities, etc.), array capacity, and unique ISBN.
     * Add the book to the array and update the count.
     *
     * @param newBook Book to add (has name, author, ISBN, total quantity, borrowed quantity)
     * @return true if added successfully; false if info invalid, array full, or ISBN duplicate
     */
    public boolean addBook(Book newBook) {
        // 1. Check if parameter is valid (prevent invalid data)
        if (newBook == null) {
            System.out.println("Error: Book info is empty!");
            return false;
        }
        if (newBook.getBookName() == null || newBook.getBookName().trim().isEmpty()) {
            System.out.println("Error: Book name cannot be empty!");
            return false;
        }
        if (newBook.getBookAuthor() == null || newBook.getBookAuthor().trim().isEmpty()) {
            System.out.println("Error: Author cannot be empty!");
            return false;
        }
        if (newBook.getBookISBN() == null || newBook.getBookISBN().trim().isEmpty()) {
            System.out.println("Error: ISBN cannot be empty!");
            return false;
        }
        if (newBook.getBookQuantity() < 0) {
            System.out.println("Error: Total quantity cannot be negative!");
            return false;
        }
        if (newBook.getBookBorrowedQuantity() < 0 || newBook.getBookBorrowedQuantity() > newBook.getBookQuantity()) {
            System.out.println("Error: Borrowed quantity is invalid (cannot be negative or exceed total quantity)!");
            return false;
        }

        // 2. Check if array is full
        if (bookCount >= MAX_CAPACITY) {
            System.out.println("Error: Book array is full. Cannot add new books!");
            return false;
        }

        // 3. Check if ISBN already exists (ISBN is unique)
        for (int i = 0; i < bookCount; i++) {
            if (bookArray[i].getBookISBN().equals(newBook.getBookISBN())) {
                System.out.println("Error: Book with ISBN [" + newBook.getBookISBN() + "] already exists. Cannot add again!");
                return false;
            }
        }

        // 4. All checks passed, add to array
        bookArray[bookCount++] = newBook;
        System.out.println("Successfully added book: \"" + newBook.getBookName() + "\" (ISBN: " + newBook.getBookISBN() + ")");

        // 5. Save to file if file path is set
        if (filePath != null) {
            saveBooksToFile();
        }

        return true;
    }

    /**
     * Update book information by ISBN
     * <p>
     * Find the book by ISBN and update its information.
     * ISBN cannot be changed as it serves as the unique identifier.
     *
     * @param isbn ISBN of the book to update
     * @param newName New book name (can be null to keep current)
     * @param newAuthor New author name (can be null to keep current)
     * @param newQuantity New total quantity (can be -1 to keep current)
     * @param newBorrowedQuantity New borrowed quantity (can be -1 to keep current)
     * @return true if updated successfully; false if parameters invalid or book not found
     */
    public boolean updateBook(String isbn, String newName, String newAuthor, int newQuantity, int newBorrowedQuantity) {
        // 1. Check if ISBN is valid
        if (isbn == null || isbn.trim().isEmpty()) {
            System.out.println("Error: ISBN cannot be empty!");
            return false;
        }

        // 2. Find the book by ISBN
        for (int i = 0; i < bookCount; i++) {
            if (bookArray[i].getBookISBN().equals(isbn)) {
                Book book = bookArray[i];
                
                // 3. Update fields if new values are provided
                boolean updated = false;
                
                if (newName != null && !newName.trim().isEmpty()) {
                    book.setBookName(newName.trim());
                    updated = true;
                }
                
                if (newAuthor != null && !newAuthor.trim().isEmpty()) {
                    book.setBookAuthor(newAuthor.trim());
                    updated = true;
                }
                
                if (newQuantity >= 0) {
                    if (newBorrowedQuantity >= 0) {
                        // Both quantities are provided
                        if (newBorrowedQuantity > newQuantity) {
                            System.out.println("Error: Borrowed quantity cannot exceed total quantity!");
                            return false;
                        }
                        book.setBookQuantity(newQuantity);
                        book.setBookBorrowedQuantity(newBorrowedQuantity);
                        updated = true;
                    } else {
                        // Only total quantity is provided, keep borrowed quantity
                        if (book.getBookBorrowedQuantity() > newQuantity) {
                            System.out.println("Error: New total quantity is less than current borrowed quantity!");
                            return false;
                        }
                        book.setBookQuantity(newQuantity);
                        updated = true;
                    }
                } else if (newBorrowedQuantity >= 0) {
                    // Only borrowed quantity is provided
                    if (newBorrowedQuantity > book.getBookQuantity()) {
                        System.out.println("Error: Borrowed quantity cannot exceed total quantity!");
                        return false;
                    }
                    book.setBookBorrowedQuantity(newBorrowedQuantity);
                    updated = true;
                }
                
                if (!updated) {
                    System.out.println("Warning: No changes made to book with ISBN [" + isbn + "]");
                    return false;
                }
                
                System.out.println("Successfully updated book: \"" + book.getBookName() + "\" (ISBN: " + isbn + ")");
                
                // 4. Save to file if file path is set
                if (filePath != null) {
                    saveBooksToFile();
                }
                
                return true;
            }
        }
        
        System.out.println("No book found with ISBN [" + isbn + "]!");
        return false;
    }

    /**
     * Delete a book by ISBN
     * <p>
     * Find the book by ISBN. If found, move subsequent elements forward to cover it.
     * Update the book count to keep the array continuous.
     *
     * @param isbn ISBN of the book to delete (unique ID)
     * @return true if deleted successfully; false if ISBN is empty or book not found
     */
    public boolean deleteBook(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            System.out.println("Error: ISBN cannot be empty!");
            return false;
        }
        for (int i = 0; i < bookCount; i++) {
            if (bookArray[i].getBookISBN().equals(isbn)) {
                // Move elements forward to cover the deleted one
                for (int j = i; j < bookCount - 1; j++) {
                    bookArray[j] = bookArray[j + 1];
                }
                bookArray[--bookCount] = null; // Empty the last element for GC
                System.out.println("Successfully deleted book with ISBN [" + isbn + "]!");

                // Save to file if file path is set
                if (filePath != null) {
                    saveBooksToFile();
                }

                return true;
            }
        }
        System.out.println("No book found with ISBN [" + isbn + "]!");
        return false;
    }

    /**
     * View details of all books in the system
     * <p>
     * Go through the array and show ISBN, name, author, total quantity, borrowed quantity, and remaining quantity.
     * Show a message if there are no books.
     */
    public void listAllBooks() {
        if (bookCount == 0) {
            System.out.println("Book array is empty. No books to show!");
            return;
        }
        System.out.println("\n===== All Books List =====");
        for (int i = 0; i < bookCount; i++) {
            Book book = bookArray[i];
            int remaining = book.getBookQuantity() - book.getBookBorrowedQuantity(); // Remaining = total - borrowed
            System.out.println("No.: " + (i + 1) +
                    " | ISBN: " + book.getBookISBN() +
                    " | Name: " + book.getBookName() +
                    " | Author: " + book.getBookAuthor() +
                    " | Total: " + book.getBookQuantity() +
                    " | Borrowed: " + book.getBookBorrowedQuantity() +
                    " | Remaining: " + remaining);
        }
    }

    /**
     * Search a book by exact ISBN
     * <p>
     * Find the book by exact ISBN match. Show its details including remaining quantity.
     * Show a message if not found.
     *
     * @param isbn ISBN of the book to search (unique ID)
     */
    public void searchByISBN(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            System.out.println("Error: ISBN cannot be empty!");
            return;
        }
        for (int i = 0; i < bookCount; i++) {
            Book book = bookArray[i];
            if (book.getBookISBN().equals(isbn)) {
                int remaining = book.getBookQuantity() - book.getBookBorrowedQuantity();
                System.out.println("\n===== Book Details =====");
                System.out.println("ISBN: " + book.getBookISBN());
                System.out.println("Name: " + book.getBookName());
                System.out.println("Author: " + book.getBookAuthor());
                System.out.println("Total: " + book.getBookQuantity());
                System.out.println("Borrowed: " + book.getBookBorrowedQuantity());
                System.out.println("Remaining: " + remaining);
                return;
            }
        }
        System.out.println("No book found with ISBN [" + isbn + "]!");
    }

    /**
     * Search books by fuzzy name match
     * <p>
     * Match book names with the keyword (case-insensitive). Return all matching books.
     * Show total quantity and borrowed quantity of these books. Show message if none found.
     *
     * @param nameKeyword Keyword for book name (e.g., "program" matches "Java program")
     */
    public void searchByBookName(String nameKeyword) {
        if (nameKeyword == null || nameKeyword.trim().isEmpty()) {
            System.out.println("Error: Book name keyword cannot be empty!");
            return;
        }
        // Store matching books
        Book[] matchedBooks = new Book[bookCount];
        int matchCount = 0;
        // Count total quantity and borrowed quantity of same-name books
        int totalQuantity = 0;
        int totalBorrowed = 0;

        // Search for matches in the array
        for (int i = 0; i < bookCount; i++) {
            Book book = bookArray[i];
            // Fuzzy match (include keyword, case-insensitive)
            if (book.getBookName().toLowerCase().contains(nameKeyword.toLowerCase())) {
                matchedBooks[matchCount++] = book;
                totalQuantity += book.getBookQuantity();
                totalBorrowed += book.getBookBorrowedQuantity();
            }
        }

        // Output results
        if (matchCount == 0) {
            System.out.println("No books found containing [" + nameKeyword + "]!");
            return;
        }

        System.out.println("\n===== Search Results (Name contains: " + nameKeyword + ") =====");
        System.out.println("Found " + matchCount + " related books:");
        for (int i = 0; i < matchCount; i++) {
            Book book = matchedBooks[i];
            int remaining = book.getBookQuantity() - book.getBookBorrowedQuantity();
            System.out.println((i + 1) + ". ISBN: " + book.getBookISBN() +
                    " | Name: " + book.getBookName() +
                    " | Author: " + book.getBookAuthor() +
                    " | Total: " + book.getBookQuantity() +
                    " | Borrowed: " + book.getBookBorrowedQuantity() +
                    " | Remaining: " + remaining);
        }

        // Output summary of same-name books
        System.out.println("\n===== Summary =====");
        System.out.println("Total quantity of same-name books: " + totalQuantity);
        System.out.println("Total borrowed quantity of same-name books: " + totalBorrowed);
    }

    /**
     * Search books by author
     * <p>
     * Exact match of author name (case-insensitive). Return all books by this author.
     * Include remaining quantity of each book. Show message if none found.
     *
     * @param authorName Author name (exact match, e.g., "Lu Xun" only matches books by "Lu Xun")
     */
    public void searchByAuthor(String authorName) {
        if (authorName == null || authorName.trim().isEmpty()) {
            System.out.println("Error: Author name cannot be empty!");
            return;
        }

        // Store matching books
        Book[] matchedBooks = new Book[bookCount];
        int matchCount = 0;

        // Search for matches in the array (exact match, case-insensitive)
        for (int i = 0; i < bookCount; i++) {
            Book book = bookArray[i];
            if (book.getBookAuthor().equalsIgnoreCase(authorName)) {
                matchedBooks[matchCount++] = book;
            }
        }

        // Output results
        if (matchCount == 0) {
            System.out.println("No books found by author [" + authorName + "]!");
            return;
        }

        System.out.println("\n===== Search Results (Author: " + authorName + ") =====");
        System.out.println("Found " + matchCount + " book(s) by this author:");
        for (int i = 0; i < matchCount; i++) {
            Book book = matchedBooks[i];
            int remaining = book.getBookQuantity() - book.getBookBorrowedQuantity();
            System.out.println((i + 1) + ". ISBN: " + book.getBookISBN() +
                    " | Name: " + book.getBookName() +
                    " | Total: " + book.getBookQuantity() +
                    " | Borrowed: " + book.getBookBorrowedQuantity() +
                    " | Remaining: " + remaining);
        }
    }

    /**
     * Public method to save all books to file
     * This method can be called externally to force save
     */
    public void saveDataToFile() {
        saveBooksToFile();
    }

    /**
     * Save all books to file
     * <p>
     * Writes all books in the array to the specified file.
     * File format: bookTitle,author,ISBN,totalQuantity,borrowedQuantity
     * Preserves the header comments from the original file.
     */
    private void saveBooksToFile() {
        if (filePath == null || filePath.trim().isEmpty()) {
            System.out.println("Warning: File path is not set. Cannot save book data.");
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
                writer.write("# books.txt — Book Data (UTF-8). Each line: book title, author, ISBN, total quantity, number lent");
                writer.newLine();
                writer.write("# Empty rows and lines starting with # are ignored");
                writer.newLine();
                writer.newLine();

                // Write all books
                for (int i = 0; i < bookCount; i++) {
                    Book book = bookArray[i];
                    writer.write(book.getBookName() + "," +
                            book.getBookAuthor() + "," +
                            book.getBookISBN() + "," +
                            book.getBookQuantity() + "," +
                            book.getBookBorrowedQuantity());
                    writer.newLine();
                }
            }
            // Save successful - show message for debugging
            System.out.println("Book data saved successfully to: " + filePath);
        } catch (IOException e) {
            System.out.println("Warning: Failed to save book data to file: " + e.getMessage());
            System.out.println("File path was: " + filePath);
            e.printStackTrace();
        }
    }
}