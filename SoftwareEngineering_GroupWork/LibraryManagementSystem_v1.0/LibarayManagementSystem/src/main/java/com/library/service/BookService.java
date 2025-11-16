package main.java.com.library.service;

import main.java.com.library.model.Book;

/**
 * Book service class
 * Handles book operations: add, delete, query, search.
 * Uses array to store data.
 */
public class BookService {

    //Max number of books the system can hold
    private static final int MAX_CAPACITY = 100;
    //Array to store all books
    private Book[] bookArray = new Book[MAX_CAPACITY];
    //Current number of books
    private int bookCount = 0;

    /**
     * Add a new book
     * @param newBook Book to add
     * @return true if successful, false otherwise
     */
    public boolean addBook(Book newBook) {
        //Check if input is valid
        if (newBook == null) {
            System.out.println("Error: Book information is empty!");
            return false;
        }
        if (newBook.getBookName() == null || newBook.getBookName().trim().isEmpty()) {
            System.out.println("Error: Book title cannot be empty!");
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
            System.out.println("Error: Borrowed quantity is invalid!");
            return false;
        }

        //Check if array is full
        if (bookCount >= MAX_CAPACITY) {
            System.out.println("Error: Book storage is full, cannot add new books!");
            return false;
        }

        //Check if ISBN already exists
        for (int i = 0; i < bookCount; i++) {
            if (bookArray[i].getBookISBN().equals(newBook.getBookISBN())) {
                System.out.println("Error: Book with ISBN [" + newBook.getBookISBN() + "] already exists!");
                return false;
            }
        }

        //Add book
        bookArray[bookCount++] = newBook;
        System.out.println("Successfully added book:《" + newBook.getBookName() + "》（ISBN：" + newBook.getBookISBN() + "）");
        return true;
    }

    /**
     * Delete book by ISBN
     * @param isbn ISBN of book to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteBook(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            System.out.println("Error: ISBN cannot be empty!");
            return false;
        }
        for (int i = 0; i < bookCount; i++) {
            if (bookArray[i].getBookISBN().equals(isbn)) {
                //Move elements forward to cover the deleted one
                for (int j = i; j < bookCount - 1; j++) {
                    bookArray[j] = bookArray[j + 1];
                }
                bookArray[--bookCount] = null;
                System.out.println("Successfully deleted book with ISBN ["+isbn+"]!");
                return true;
            }
        }
        System.out.println("Book with ISBN [" + isbn + "] not found!");
        return false;
    }

    /**
     * Show all books
     */
    public void listAllBooks() {
        if (bookCount == 0) {
            System.out.println("No books in the system!");
            return;
        }
        System.out.println("\n===== All Books =====");
        for (int i = 0; i < bookCount; i++) {
            Book book = bookArray[i];
            int remaining = book.getBookQuantity() - book.getBookBorrowedQuantity();
            System.out.println("No.: " + (i + 1) +
                    " | ISBN：" + book.getBookISBN() +
                    " | Title：" + book.getBookName() +
                    " | Author：" + book.getBookAuthor() +
                    " | Total：" + book.getBookQuantity() +
                    " | Borrowed：" + book.getBookBorrowedQuantity() +
                    " | Remaining：" + remaining);
        }
    }

    /**
     * Search book by ISBN
     * @param isbn ISBN to search
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
                System.out.println("ISBN：" + book.getBookISBN());
                System.out.println("Title：" + book.getBookName());
                System.out.println("Author：" + book.getBookAuthor());
                System.out.println("Total：" + book.getBookQuantity());
                System.out.println("Borrowed：" + book.getBookBorrowedQuantity());
                System.out.println("Remaining：" + remaining);
                return;
            }
        }
        System.out.println("Book with ISBN [" + isbn + "] not found!");
    }

    /**
     * Search books by name keyword
     * @param nameKeyword Keyword to search
     */
    public void searchByBookName(String nameKeyword) {
        if (nameKeyword == null || nameKeyword.trim().isEmpty()) {
            System.out.println("Error: Name keyword cannot be empty!");
            return;
        }
        //Store matching books
        Book[] matchedBooks = new Book[bookCount];
        int matchCount = 0;
        //Count totals
        int totalQuantity = 0;
        int totalBorrowed = 0;

        //Find matches
        for (int i = 0; i < bookCount; i++) {
            Book book = bookArray[i];
            //Case-insensitive search
            if (book.getBookName().toLowerCase().contains(nameKeyword.toLowerCase())) {
                matchedBooks[matchCount++] = book;
                totalQuantity += book.getBookQuantity();
                totalBorrowed += book.getBookBorrowedQuantity();
            }
        }

        //Show results
        if (matchCount == 0) {
            System.out.println("No books found containing [" + nameKeyword + "]!");
            return;
        }

        System.out.println("\n===== Search Results (Title includes: " + nameKeyword + ") =====");
        System.out.println("Found " + matchCount + " book(s):");
        for (int i = 0; i < matchCount; i++) {
            Book book = matchedBooks[i];
            int remaining = book.getBookQuantity() - book.getBookBorrowedQuantity();
            System.out.println((i + 1) + ". ISBN：" + book.getBookISBN() +
                    " | Title：" + book.getBookName() +
                    " | Author：" + book.getBookAuthor() +
                    " | Total：" + book.getBookQuantity() +
                    " | Borrowed：" + book.getBookBorrowedQuantity() +
                    " | Remaining：" + remaining);
        }

        //Show summary
        System.out.println("\n===== Summary =====");
        System.out.println("Total books with this name: " + totalQuantity);
        System.out.println("Total borrowed: " + totalBorrowed);
    }

    /**
     * Search books by author
     * @param authorName Author to search
     */
    public void searchByAuthor(String authorName) {
        if (authorName == null || authorName.trim().isEmpty()) {
            System.out.println("Error: Author name cannot be empty！");
            return;
        }
        //Store matching books
        Book[] matchedBooks = new Book[bookCount];
        int matchCount = 0;

        //Find matches
        for (int i = 0; i < bookCount; i++) {
            Book book = bookArray[i];
            if (book.getBookAuthor().toLowerCase().equals(authorName.toLowerCase())) {
                matchedBooks[matchCount++] = book;
            }
        }

        //Show results
        if (matchCount == 0) {
            System.out.println("No books by author [" + authorName + "] found!");
            return;
        }

        System.out.println("\n===== Search Results (Author: " + authorName + ") =====");
        System.out.println("Found " + matchCount + " book(s):");
        for (int i = 0; i < matchCount; i++) {
            Book book = matchedBooks[i];
            int remaining = book.getBookQuantity() - book.getBookBorrowedQuantity();
            System.out.println((i + 1) + ". ISBN：" + book.getBookISBN() +
                    " | Title：" + book.getBookName() +
                    " | Total：" + book.getBookQuantity() +
                    " | Borrowed：" + book.getBookBorrowedQuantity() +
                    " | Remaining：" + remaining);
        }
    }
}