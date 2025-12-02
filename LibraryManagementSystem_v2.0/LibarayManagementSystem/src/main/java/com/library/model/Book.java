package main.java.com.library.model;

/**
 * Book Model.
 * Encapsulates the information of a single book in the library system.
 * It includes basic book properties like title, author, and ISBN,
 * inventory management properties like total copies and borrowed copies.
 */
public class Book {

    // The name of the book.
    private String bookName;

    // The author of the book.
    private String bookAuthor;

    // ISBN
    private String bookISBN;

    // The total number of this book
    private int bookQuantity;

    // The number of this book that are borrowed.
    private int bookBorrowedQuantity;
}