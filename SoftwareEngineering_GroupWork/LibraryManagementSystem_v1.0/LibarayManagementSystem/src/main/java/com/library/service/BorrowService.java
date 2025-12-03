package main.java.com.library.service;

import main.java.com.library.model.BorrowRecord;

/**
 * Borrow Service
 * Handles borrow/return operations and record management.
 */
public class BorrowService {
    private static final int MAX_CAPACITY = 200;
    private BorrowRecord[] borrowArray = new BorrowRecord[MAX_CAPACITY];
    private int recordCount = 0;

    /**
     * Add a new borrow record.
     * @param newRecord Record to add
     * @return true if successful, false otherwise
     */
    public boolean addBorrowRecord(BorrowRecord newRecord) {
        if (newRecord == null) {
            System.out.println("Error: Record cannot be null!");
            return false;
        }
        if (newRecord.getBorrowId() == null || newRecord.getBorrowId().trim().isEmpty()) {
            System.out.println("Error: Record ID cannot be empty!");
            return false;
        }
        if (newRecord.getBorrowDate() == null || newRecord.getBorrowDate().trim().isEmpty()) {
            System.out.println("Error: Borrow date cannot be empty!");
            return false;
        }
        if (newRecord.getBorrowStatus() != 0 && newRecord.getBorrowStatus() != 1) {
            System.out.println("Error: Invalid status! (0 = Checked Out, 1 = Returned)");
            return false;
        }

        if (recordCount >= MAX_CAPACITY) {
            System.out.println("Error: Record storage is full!");
            return false;
        }

        for (int i = 0; i < recordCount; i++) {
            if (borrowArray[i].getBorrowId().equals(newRecord.getBorrowId())) {
                System.out.println("Error: Record ID " + newRecord.getBorrowId() + " already exists!");
                return false;
            }
        }

        borrowArray[recordCount++] = newRecord;
        System.out.println("Record added successfully (ID: " + newRecord.getBorrowId() + ")");
        return true;
    }

    /**
     * Delete a borrow record by ID.
     * @param borrowId ID of record to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteBorrowRecord(String borrowId) {
        if (borrowId == null || borrowId.trim().isEmpty()) {
            System.out.println("Error: Record ID cannot be empty!");
            return false;
        }
        for (int i = 0; i < recordCount; i++) {
            if (borrowArray[i].getBorrowId().equals(borrowId)) {
                for (int j = i; j < recordCount - 1; j++) {
                    borrowArray[j] = borrowArray[j + 1];
                }
                borrowArray[--recordCount] = null;
                System.out.println("Record " + borrowId + " deleted successfully!");
                return true;
            }
        }
        System.out.println("Error: Record " + borrowId + " not found!");
        return false;
    }

    /**
     * Update borrow record status.
     * @param borrowId ID of record to update
     * @param newStatus New status (0 = Checked Out, 1 = Returned)
     * @return true if successful, false otherwise
     */
    public boolean updateBorrowStatus(String borrowId, int newStatus) {
        if (borrowId == null || borrowId.trim().isEmpty()) {
            System.out.println("Error: Record ID cannot be empty!");
            return false;
        }
        if (newStatus != 0 && newStatus != 1) {
            System.out.println("Error: Invalid status! (0 = Checked Out, 1 = Returned)");
            return false;
        }

        for (int i = 0; i < recordCount; i++) {
            if (borrowArray[i].getBorrowId().equals(borrowId)) {
                borrowArray[i].setBorrowStatus(newStatus);
                String statusStr = (newStatus == 0) ? "Checked Out" : "Returned";
                System.out.println("Record " + borrowId + " status updated to: " + statusStr);
                return true;
            }
        }

        System.out.println("Error: Record " + borrowId + " not found!");
        return false;
    }

    /**
     * List all borrow records.
     */
    public void listAllBorrowRecords() {
        if (recordCount == 0) {
            System.out.println("No borrow records available.");
            return;
        }
        System.out.println("\n===== All Borrow Records =====");
        for (int i = 0; i < recordCount; i++) {
            BorrowRecord record = borrowArray[i];
            String statusStr = (record.getBorrowStatus() == 0) ? "Checked Out" : "Returned";
            System.out.println("No. " + (i + 1) +
                    " | Record ID: " + record.getBorrowId() +
                    " | Date: " + record.getBorrowDate() +
                    " | Status: " + statusStr);
        }
    }

    /**
     * Search borrow record by ID.
     * @param borrowId ID to search
     */
    public void searchByBorrowId(String borrowId) {
        if (borrowId == null || borrowId.trim().isEmpty()) {
            System.out.println("Error: Record ID cannot be empty!");
            return;
        }
        for (int i = 0; i < recordCount; i++) {
            BorrowRecord record = borrowArray[i];
            if (record.getBorrowId().equals(borrowId)) {
                System.out.println("\n===== Record Details =====");
                System.out.println("Record ID: " + record.getBorrowId());
                System.out.println("Date: " + record.getBorrowDate());
                System.out.println("Status: " + (record.getBorrowStatus() == 0 ? "Checked Out" : "Returned"));
                return;
            }
        }
        System.out.println("Error: Record " + borrowId + " not found!");
    }

    /**
     * Search borrow records by status.
     * @param status Status to search (0 = Checked Out, 1 = Returned)
     */
    public void searchByStatus(int status) {
        if (status != 0 && status != 1) {
            System.out.println("Error: Invalid status! (0 = Checked Out, 1 = Returned)");
            return;
        }

        BorrowRecord[] matchedRecords = new BorrowRecord[recordCount];
        int matchCount = 0;
        String statusStr = (status == 0) ? "Checked Out" : "Returned";

        for (int i = 0; i < recordCount; i++) {
            if (borrowArray[i].getBorrowStatus() == status) {
                matchedRecords[matchCount++] = borrowArray[i];
            }
        }

        if (matchCount == 0) {
            System.out.println("No records found with status: " + statusStr);
            return;
        }

        System.out.println("\n===== Search Results (Status: " + statusStr + ") =====");
        System.out.println("Found " + matchCount + " record(s):");
        for (int i = 0; i < matchCount; i++) {
            BorrowRecord record = matchedRecords[i];
            System.out.println((i + 1) + ". Record ID: " + record.getBorrowId() +
                    " | Date: " + record.getBorrowDate());
        }
    }
}