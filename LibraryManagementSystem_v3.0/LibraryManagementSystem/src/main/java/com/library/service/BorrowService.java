package com.library.service;

import com.library.model.BorrowRecord;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Borrow business logic service class
 *
 * Main job: Handle all borrow record operations like adding, deleting, querying, updating.
 * Store and manage records with an array. Ensure valid and complete data operations.
 */
public class BorrowService {
    // Max array capacity: maximum number of borrow records the system can store
    private static final int MAX_CAPACITY = 200;
    // Borrow record array: stores all borrow record objects
    private BorrowRecord[] borrowArray = new BorrowRecord[MAX_CAPACITY];
    // Actual record count: number of records stored (≤ MAX_CAPACITY)
    private int recordCount = 0;
    // File path for saving borrow record data
    private String filePath = null;

    /**
     * Add a new borrow record to the system
     *
     * Check if record info is valid (not empty, valid status, etc.), array capacity, and unique record ID.
     * Add the record to the array and update the count.
     *
     * @param newRecord Record to add (has ID, date, status, etc.)
     * @return true if added successfully; false if info invalid, array full, or ID duplicate
     */
    public boolean addBorrowRecord(BorrowRecord newRecord) {
        // 1. Check if parameter is valid
        if (newRecord == null) {
            System.out.println("Error: Borrow record is empty!");
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
            System.out.println("Error: Invalid status (0 - checked out, 1 - returned)!");
            return false;
        }

        // 2. Check if array is full
        if (recordCount >= MAX_CAPACITY) {
            System.out.println("Error: Borrow record array is full. Cannot add new records!");
            return false;
        }

        // 3. Check if record ID already exists (ID is unique)
        for (int i = 0; i < recordCount; i++) {
            if (borrowArray[i].getBorrowId().equals(newRecord.getBorrowId())) {
                System.out.println("Error: Borrow record with ID [" + newRecord.getBorrowId() + "] already exists. Cannot add again!");
                return false;
            }
        }

        // 4. All checks passed, add to array
        borrowArray[recordCount++] = newRecord;
        System.out.println("Successfully added borrow record (ID: " + newRecord.getBorrowId() + ")");

        // 5. Save to file if file path is set
        if (filePath != null) {
            saveBorrowRecordsToFile();
        }

        return true;
    }

    /**
     * Delete a borrow record by ID
     *
     * Find the record by ID. If found, move subsequent elements forward to cover it.
     * Update the record count to keep the array continuous.
     *
     * @param borrowId ID of the record to delete (unique ID)
     * @return true if deleted successfully; false if ID is empty or record not found
     */
    public boolean deleteBorrowRecord(String borrowId) {
        if (borrowId == null || borrowId.trim().isEmpty()) {
            System.out.println("Error: Record ID cannot be empty!");
            return false;
        }
        for (int i = 0; i < recordCount; i++) {
            if (borrowArray[i].getBorrowId().equals(borrowId)) {
                // Move elements forward to cover the deleted one
                for (int j = i; j < recordCount - 1; j++) {
                    borrowArray[j] = borrowArray[j + 1];
                }
                borrowArray[--recordCount] = null; // Empty the last element for GC
                System.out.println("Successfully deleted borrow record with ID [" + borrowId + "]!");

                // Save to file if file path is set
                if (filePath != null) {
                    saveBorrowRecordsToFile();
                }

                return true;
            }
        }
        System.out.println("No borrow record found with ID [" + borrowId + "]!");
        return false;
    }

    /**
     * Update status of a borrow record
     *
     * Find the record by ID. Update its status (0 - checked out, 1 - returned).
     * Record ID and date cannot be modified.
     *
     * @param borrowId ID of the record to modify
     * @param newStatus New status (0 - checked out, 1 - returned)
     * @return true if updated successfully; false if parameters invalid or record not found
     */
    public boolean updateBorrowStatus(String borrowId, int newStatus) {
        // 1. Check if parameters are valid
        if (borrowId == null || borrowId.trim().isEmpty()) {
            System.out.println("Error: Record ID cannot be empty!");
            return false;
        }
        if (newStatus != 0 && newStatus != 1) {
            System.out.println("Error: Invalid status (0 - checked out, 1 - returned)!");
            return false;
        }

        // 2. Find the record and update status
        for (int i = 0; i < recordCount; i++) {
            if (borrowArray[i].getBorrowId().equals(borrowId)) {
                borrowArray[i].setBorrowStatus(newStatus);
                System.out.println("Successfully updated status of record ID [" + borrowId + "] to: " + (newStatus == 0 ? "checked out" : "returned"));

                // Save to file if file path is set
                if (filePath != null) {
                    saveBorrowRecordsToFile();
                }

                return true;
            }
        }

        System.out.println("No borrow record found with ID [" + borrowId + "]. Update failed!");
        return false;
    }

    /**
     * View details of all borrow records in the system
     *
     * Go through the array and show ID, date and status of all records.
     * Show a message if there are no records.
     */
    public void listAllBorrowRecords() {
        if (recordCount == 0) {
            System.out.println("Borrow record array is empty. No records to show!");
            return;
        }
        System.out.println("\n===== All Borrow Records List =====");
        for (int i = 0; i < recordCount; i++) {
            BorrowRecord record = borrowArray[i];
            System.out.println("No.: " + (i + 1) +
                    " | Record ID: " + record.getBorrowId() +
                    " | Borrow Date: " + record.getBorrowDate() +
                    " | Status: " + (record.getBorrowStatus() == 0 ? "checked out" : "returned"));
        }
    }

    /**
     * Search a borrow record by exact ID
     *
     * Find the record by exact ID match. Show its details.
     * Show a message if not found.
     *
     * @param borrowId ID of the record to search (unique ID)
     */
    public void searchByBorrowId(String borrowId) {
        if (borrowId == null || borrowId.trim().isEmpty()) {
            System.out.println("Error: Record ID cannot be empty!");
            return;
        }
        for (int i = 0; i < recordCount; i++) {
            BorrowRecord record = borrowArray[i];
            if (record.getBorrowId().equals(borrowId)) {
                System.out.println("\n===== Borrow Record Details =====");
                System.out.println("Record ID: " + record.getBorrowId());
                System.out.println("Borrow Date: " + record.getBorrowDate());
                System.out.println("Status: " + (record.getBorrowStatus() == 0 ? "checked out" : "returned"));
                return;
            }
        }
        System.out.println("No borrow record found with ID [" + borrowId + "]!");
    }

    /**
     * Search borrow records by status
     *
     * Find records by status (0 - checked out, 1 - returned). Return all records with the same status.
     * Show a message if none found.
     *
     * @param status Status value (0 - checked out, 1 - returned)
     */
    public void searchByStatus(int status) {
        if (status != 0 && status != 1) {
            System.out.println("Error: Invalid status (0 - checked out, 1 - returned)!");
            return;
        }

        // Store matching records
        BorrowRecord[] matchedRecords = new BorrowRecord[recordCount];
        int matchCount = 0;

        // Search for matches in the array
        for (int i = 0; i < recordCount; i++) {
            BorrowRecord record = borrowArray[i];
            if (record.getBorrowStatus() == status) {
                matchedRecords[matchCount++] = record;
            }
        }

        // Output results
        if (matchCount == 0) {
            System.out.println("No borrow records found with status [" + (status == 0 ? "checked out" : "returned") + "]!");
            return;
        }

        System.out.println("\n===== Search Results (Status: " + (status == 0 ? "checked out" : "returned") + ") =====");
        System.out.println("Found " + matchCount + " related records:");
        for (int i = 0; i < matchCount; i++) {
            BorrowRecord record = matchedRecords[i];
            System.out.println((i + 1) + ". Record ID: " + record.getBorrowId() +
                    " | Borrow Date: " + record.getBorrowDate());
        }
    }

    /**
     * Set the file path for saving borrow record data
     * @param filePath Path to the borrow record data file
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Public method to save all borrow records to file
     * This method can be called externally to force save
     */
    public void saveDataToFile() {
        saveBorrowRecordsToFile();
    }

    /**
     * Save all borrow records to file
     * <p>
     * Writes all borrow records in the array to the specified file.
     * File format: borrowId,borrowDate,borrowStatus
     * Preserves the header comments from the original file.
     */
    private void saveBorrowRecordsToFile() {
        if (filePath == null || filePath.trim().isEmpty()) {
            System.out.println("Warning: File path is not set. Cannot save borrow record data.");
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
                writer.write("# borrow_records.txt — Borrow Record Data (UTF-8). Each line: record ID, borrow date, status (0-checked out, 1-returned)");
                writer.newLine();
                writer.write("# Empty rows and lines starting with # are ignored");
                writer.newLine();
                writer.newLine();

                // Write all borrow records
                for (int i = 0; i < recordCount; i++) {
                    BorrowRecord record = borrowArray[i];
                    writer.write(record.getBorrowId() + "," +
                            record.getBorrowDate() + "," +
                            record.getBorrowStatus());
                    writer.newLine();
                }
            }
            // Save successful - show message for debugging
            System.out.println("Borrow record data saved successfully to: " + filePath);
        } catch (IOException e) {
            System.out.println("Warning: Failed to save borrow record data to file: " + e.getMessage());
            System.out.println("File path was: " + filePath);
            e.printStackTrace();
        }
    }
}