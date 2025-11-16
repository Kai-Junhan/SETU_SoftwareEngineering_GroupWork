package main.java.com.library.service;

import main.java.com.library.model.BorrowRecord;

/**
 * Borrow/Return business logic service class.
 * Core responsibility: Encapsulates all business operations related to borrow records, including adding, deleting, querying, and modifying records.
 * Uses an array to store and manage borrow record data, ensuring the legality and integrity of data operations.
 */
public class BorrowService {
    //add a borrow
    public boolean addBorrowRecord(BorrowRecord newRecord) {return false;}
    //delete a record by borrow ID
    public boolean deleteBorrowRecord(String borrowId) {
        return false;
    }
    //update the status by borrow ID
    public boolean updateBorrowStatus(String borrowId, int newStatus) {
        return false;
    }
    //list all borrow records
    public void listAllBorrowRecords() {}
    //search for a borrow record by borrow ID
    public void searchByBorrowId(String borrowId) {}
}