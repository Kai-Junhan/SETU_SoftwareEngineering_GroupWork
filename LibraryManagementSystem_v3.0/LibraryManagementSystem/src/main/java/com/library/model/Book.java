package com.library.model;

// Book entity class
public class Book {
    // Private attributes
    private String bookName;
    private String bookAuthor;
    private String bookISBN;
    private int bookQuantity;
    private int bookBorrowedQuantity;

    // Constructors
    public Book() {
    }
    public Book(String bookName, String bookAuthor, String bookISBN, int bookQuantity, int bookBorrowedQuantity) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookISBN = bookISBN;
        this.bookQuantity = bookQuantity;
        this.bookBorrowedQuantity = bookBorrowedQuantity;
    }

    // Getters and Setters
    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }

    public int getBookQuantity() {
        return bookQuantity;
    }

    public void setBookQuantity(int bookQuantity) {
        this.bookQuantity = bookQuantity;
    }

    public int getBookBorrowedQuantity() {
        return bookBorrowedQuantity;
    }

    public void setBookBorrowedQuantity(int bookBorrowedQuantity) {
        this.bookBorrowedQuantity = bookBorrowedQuantity;
    }
}