package main.java.com.library.model;

public class BorrowRecord {
    private String borrowId;
    private String borrowDate;
    private int borrowStatus;

    public BorrowRecord() {}

    public BorrowRecord(String borrowId, String borrowDate, int borrowStatus) {
        this.borrowId = borrowId;
        this.borrowDate = borrowDate;
        this.borrowStatus = borrowStatus;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public int getBorrowStatus() {
        return borrowStatus;
    }

    public void setBorrowStatus(int borrowStatus) {
        this.borrowStatus = borrowStatus;
    }
}